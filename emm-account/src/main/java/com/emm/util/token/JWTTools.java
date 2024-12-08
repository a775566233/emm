package com.emm.util.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.emm.config.AppConfig;
import com.emm.entity.User;
import com.emm.util.uuid.UUIDTools;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTTools {
    private static AppConfig appConfig;
    @Autowired
    public JWTTools(AppConfig appConfig) {
        JWTTools.appConfig = appConfig;
    }

    private static Algorithm getAlgorithm() {
        Algorithm algorithm;
        return switch (appConfig.getALGORITHM()) {
            case "HMAC384" -> Algorithm.HMAC384(appConfig.getSECRET());
            case "HMAC512" -> Algorithm.HMAC512(appConfig.getSECRET());
            default -> Algorithm.HMAC256(appConfig.getSECRET());
        };
    }

    public static String createJWT(User user, long expire) {
        try {
            Date expireDate = new Date(System.currentTimeMillis() + expire * 1000);
            return JWT.create()
                    .withIssuer("auth0")
                    .withClaim("UserName", user.getUserName())
                    .withClaim("UserUUID", user.getUserUUID())
                    .withExpiresAt(expireDate)
                    .sign(getAlgorithm());
        } catch (Exception e) {
            throw new RuntimeException("Token creation failed:  \n" + e);
        }
    }

    public static String createAccessJWT(User user) {
        return createJWT(user, appConfig.getACCESS_TOKEN_EXPIRE());
    }

    public static String createRefreshJWT(User user) {
        return createJWT(user, appConfig.getREFRESH_TOKEN_EXPIRE());
    }

    public static String createVerificationCodeJWT(User user) {
        return createJWT(user, appConfig.getVerificationCodeEmailExpire());
    }

    public static User parseJWT(@NonNull String jwt) {
        DecodedJWT verify;
        try {
            verify = getDecodedJWT(jwt);
        } catch (Exception e) {
            throw new RuntimeException("Token authentication failed:  \n" + e);
        }
        User user = new User();
        String userName = verify.getClaim("UserName").asString();
        String userUUID = verify.getClaim("UserUUID").asString();
        if (userUUID == null || userUUID.isEmpty() || userName == null || userName.isEmpty()) {
            throw new RuntimeException("Token is illegal");
        }
        user.setUserUUID(userUUID);
        user.setUserName(userName);
        return user;
    }

    //验证token
    public static JWTVerifier tokenVerify(@NonNull String jwt) {
        return JWT.require(getAlgorithm()).build();
    }

    //获取token信息
    public static DecodedJWT getDecodedJWT(@NonNull String jwt) {
        return tokenVerify(jwt).verify(jwt);
    }
}
