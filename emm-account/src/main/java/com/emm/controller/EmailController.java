package com.emm.controller;

import com.emm.config.AppConfig;
import com.emm.entity.Email;
import com.emm.entity.User;
import com.emm.entity.information.ResponseEnum;
import com.emm.entity.information.StandardResponseBody;
import com.emm.service.verification.DefaultVerificationImpl;
import jakarta.mail.internet.MimeMessage;
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


    @Autowired
    public EmailController(JavaMailSender javaMailSender, AppConfig appConfig, DefaultVerificationImpl defaultVerificationCodeImpl) {
        this.javaMailSender = javaMailSender;
        EmailController.appConfig = appConfig;
        this.defaultVerificationCodeImpl = defaultVerificationCodeImpl;
    }

    @PostMapping("/code")
    public Object sendVerificationCodeEmail(@RequestBody User user) {
        String content = "打开链接并输入此邮件的验证码，以激活您的账户：";
        String url;
        if (user == null) {
            return StandardResponseBody.customInfo(ResponseEnum.REQUEST_NULL);
        }
        if (user.getStatus() == 0) {
            url = "/email/register/";
        } else {
            url = "/email/verify/";
        }
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
        return this.check(VerificationCodeToken, code);
    }

    @GetMapping("/verify/{VerificationCodeToken}/{code}")
    public Object checkVerifyCode(@PathVariable("VerificationCodeToken") String VerificationCodeToken
            , @PathVariable("code") String code) {
        log.info("VerificationCodeToken:{}, VerifyCode = {}", VerificationCodeToken, code);
        return this.check(VerificationCodeToken, code);
    }

    private StandardResponseBody<?> check(String VerificationCodeToken, String code) {
        if (defaultVerificationCodeImpl.check(VerificationCodeToken, code)) {
            return StandardResponseBody.success();
        } else {
            return StandardResponseBody.customInfo(ResponseEnum.VERIFICATION_CODE_INVALID);
        }
    }
}
