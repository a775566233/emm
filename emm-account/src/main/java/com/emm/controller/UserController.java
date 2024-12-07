package com.emm.controller;

import com.emm.config.AppConfig;
import com.emm.controller.jwt.JwtController;
import com.emm.entity.User;
import com.emm.entity.information.ResponseEnum;
import com.emm.entity.information.StandardResponseBody;
import com.emm.service.user.UserService;
import com.emm.util.encryption.Encipher;
import com.emm.util.token.JWTTools;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    private UserService userService;
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    private static AppConfig appConfig;
    @Autowired
    public UserController(AppConfig appConfig) {
        UserController.appConfig = appConfig;
    }

    @PostMapping("/getUsers")
    public String getUsers() {
        return "success";
    }

    @PostMapping("/login")
    public Object login(HttpServletResponse response, @RequestBody User user) {
        log.info("login");
        StandardResponseBody<User> responseBody;
        User reponseUser = null;
        String userInfo = "";
        if (user == null) {
            log.error("user is null");
            return StandardResponseBody.customInfo(ResponseEnum.REQUEST_NULL);
        } else if (user.getUserPassword() == null || user.getUserPassword().isEmpty()) {
            log.error("userPassword is null");
            return StandardResponseBody.customInfo(ResponseEnum.USER_NOT_FOUND);
        }
        try {
            user.setUserPassword(Encipher.encryptUserPassword(user.getUserPassword()));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            log.error(e.getMessage());
            return StandardResponseBody.customInfo(ResponseEnum.SERVER_ERROR);
        }
        if (user.getUserName() != null && !user.getUserName().isEmpty()) {
            userInfo = user.getUserName();
            reponseUser = userService.findUserByLoginName(user.getUserName(), user.getUserPassword());
        } else if (user.getUserEmail() == null || user.getUserEmail().isEmpty()) {
            userInfo = user.getUserEmail();
            reponseUser = userService.findUserByEmail(user.getUserEmail());
        } else if (user.getUserPhone() == null || user.getUserPhone().isEmpty()) {
            try {
                user.setUserPhone(Encipher.encryptUserPhone(user.getUserPhone()));
            } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
                log.error(e.getMessage());
                return StandardResponseBody.customInfo(ResponseEnum.SERVER_ERROR);
            }
            userInfo = user.getUserPhone();
            reponseUser = userService.findUserByLoginPhone(user.getUserArea(), user.getUserPhone(), user.getUserEmail());
        }
        if (reponseUser != null) {
            log.info("user {} login success", user.getUserName());
            response.addHeader(appConfig.getWebHeaderAccessToken(), JWTTools.createAccessJWT(reponseUser));
            response.addHeader(appConfig.getWebHeaderRefreshToken(), JWTTools.createRefreshJWT(reponseUser));
            responseBody = StandardResponseBody.customInfo(ResponseEnum.SUCCESS, reponseUser);
        } else {
            log.info("user {} login fail", userInfo);
            responseBody = StandardResponseBody.customInfo(ResponseEnum.USER_NOT_FOUND);
        }
        return responseBody;
    }

}
