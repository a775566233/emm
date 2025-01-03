package com.emm.util.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.emm.config.AppConfig;
import com.emm.entity.User;
import com.emm.entity.token.TokenInfo;
import com.emm.util.uuid.UUIDTools;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;

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

    public static String createUserJWT(User user, long expire, Operation operation) {
        try {
            Date expireDate = new Date(System.currentTimeMillis() + expire * 1000);
            return JWT.create()
                    .withIssuer("auth0")
                    .withClaim("UserName", user.getUserName())
                    .withClaim("UserUUID", user.getUserUUID())
                    .withClaim("UserId", user.getUserId())
                    .withClaim("UserDuties", user.getUserDuties())
                    .withClaim("Operation", operation.toString())
                    .withExpiresAt(expireDate)
                    .sign(getAlgorithm());
        } catch (Exception e) {
            throw new RuntimeException("Token creation failed:  \n" + e);
        }
    }

    public static String createUserAccessJWT(User user) {
        return createUserJWT(user, appConfig.getACCESS_TOKEN_EXPIRE(), Operation.READ);
    }

    public static String createUserRefreshJWT(User user) {
        return createUserJWT(user, appConfig.getREFRESH_TOKEN_EXPIRE(), Operation.READ);
    }

    public static String createUserUpdateJWT(User user) {
        return createUserJWT(user, appConfig.getACCESS_TOKEN_EXPIRE(), Operation.WRITE);
    }

    public static String createUserVerificationCodeJWT(User user) {
        return createUserJWT(user, appConfig.getVerificationCodeEmailExpire(), Operation.ADD);
    }

    @Deprecated
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
        long userId = verify.getClaim("UserId").asLong();
        if (userUUID == null || userUUID.isEmpty() || userName == null || userName.isEmpty()) {
            throw new RuntimeException("Token is illegal");
        }
        user.setUserUUID(userUUID);
        user.setUserName(userName);
        user.setUserId(userId);
        return user;
    }

    public static HashMap<String, Object> parseJWTMap(@NonNull String jwt) {
        DecodedJWT verify;
        try {
            verify = getDecodedJWT(jwt);
        } catch (Exception e) {
            throw new RuntimeException("Token authentication failed:  \n" + e);
        }
        String userName = verify.getClaim("UserName").asString();
        String userUUID = verify.getClaim("UserUUID").asString();
        long userId = verify.getClaim("UserId").asLong();
        Operation operation = Operation.valueOf(verify.getClaim("Operation").asString());
        HashMap<String, Object> map = new HashMap<>();
        map.put("UserName", userName);
        map.put("UserUUID", userUUID);
        map.put("Operation", operation.toString());
        map.put("UserDuties", verify.getClaim("UserDuties").asString());
        map.put("UserId", userId);
        return map;
    }

    public static TokenInfo<User> parseUserJWT(@NonNull String jwt) {
        DecodedJWT verify;
        try {
            verify = getDecodedJWT(jwt);
        } catch (Exception e) {
            throw new RuntimeException("Token authentication failed:  \n" + e);
        }
        User user = new User(
                verify.getClaim("UserId").asLong(),
                verify.getClaim("UserName").asString(),
                verify.getClaim("UserUUID").asString()
        );
        user.setUserDuties(verify.getClaim("UserDuties").asString());
        return new TokenInfo<>(user, verify.getClaim("Operation").as(Operation.class));
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
