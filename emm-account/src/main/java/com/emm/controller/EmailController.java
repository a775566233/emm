package com.emm.controller;

import com.emm.config.AppConfig;
import com.emm.entity.Email;
import com.emm.entity.User;
import com.emm.entity.information.ResponseEnum;
import com.emm.entity.information.StandardResponseBody;
import com.emm.entity.token.TokenInfo;
import com.emm.service.user.UserService;
import com.emm.service.verification.DefaultVerificationImpl;
import com.emm.util.token.JWTTools;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/email")
public class EmailController {
    private final JavaMailSender javaMailSender;
    private static AppConfig appConfig;
    private final DefaultVerificationImpl defaultVerificationCodeImpl;
    private UserService userService;

    @Autowired
    public EmailController(JavaMailSender javaMailSender, AppConfig appConfig, DefaultVerificationImpl defaultVerificationCodeImpl, UserService userService) {
        this.javaMailSender = javaMailSender;
        EmailController.appConfig = appConfig;
        this.defaultVerificationCodeImpl = defaultVerificationCodeImpl;
        this.userService = userService;
    }

    @PostMapping("/code")
    public Object sendVerificationCodeEmail(@RequestBody User user) {
        String content = "打开链接并输入此邮件的验证码，以激活您的账户：";
        String url;
        if (user == null) {
            return StandardResponseBody.customInfo(ResponseEnum.REQUEST_NULL);
        }
        User checkUser = userService.findUserByEmail(user.getUserEmail());
        if (checkUser == null) {
            return StandardResponseBody.customInfo(ResponseEnum.USER_NOT_FOUND);
        }
        if (checkUser.getStatus() == 0) {
            url = "/email/register/";
        } else {
            url = "/email/verify/";
        }
        user.setUserId(checkUser.getUserId());
        SimpleMailMessage message = defaultVerificationCodeImpl.addEmail(
                user,
                appConfig.getVerificationCodeEmailUrl() + url,
                "emm验证邮箱",
                content
        );
        if (message != null) {
            javaMailSender.send(message);
            return StandardResponseBody.success();
        } else {
            return StandardResponseBody.customInfo(ResponseEnum.SERVER_ERROR);
        }
    }

    @GetMapping("/register/{VerificationCodeToken}/{code}")
    public Object checkRegisterCode(@PathVariable("VerificationCodeToken") String VerificationCodeToken
            , @PathVariable("code") String code) {
        log.info("VerificationCodeToken:{}, RegisterCode = {}", VerificationCodeToken, code);
        TokenInfo<User> userTokenInfo = defaultVerificationCodeImpl.check(VerificationCodeToken, code);
        if (userTokenInfo == null || userTokenInfo.getData() == null) {
            return StandardResponseBody.customInfo(ResponseEnum.VERIFICATION_CODE_INVALID);
        }
        userTokenInfo.getData().setStatus(1);
        if (userService.updateUser(userTokenInfo.getData()) > 0) {
            return StandardResponseBody.customInfo(ResponseEnum.SUCCESS);
        } else {
            return StandardResponseBody.customInfo(ResponseEnum.SERVER_ERROR);
        }
    }

    @GetMapping("/verify/{VerificationCodeToken}/{code}")
    public Object checkVerifyCode(HttpServletResponse response, @PathVariable("VerificationCodeToken") String VerificationCodeToken
            , @PathVariable("code") String code) {
        log.info("VerificationCodeToken:{}, VerifyCode = {}", VerificationCodeToken, code);
        TokenInfo<User> userTokenInfo = defaultVerificationCodeImpl.check(VerificationCodeToken, code);
        String token;
        if (userTokenInfo.getData() == null) {
            return StandardResponseBody.customInfo(ResponseEnum.VERIFICATION_CODE_INVALID);
        }
        token = JWTTools.createUserUpdateJWT(userTokenInfo.getData());
        if (token == null) {
            return StandardResponseBody.customInfo(ResponseEnum.SERVER_ERROR);
        }
        response.addHeader(appConfig.getWebHeaderAccessToken(), token);
        return StandardResponseBody.customInfo(ResponseEnum.SUCCESS);
    }

}
