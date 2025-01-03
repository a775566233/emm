package com.emm.service.verification;

import com.emm.entity.User;
import com.emm.entity.VerificationCode;
import com.emm.entity.token.TokenInfo;
import org.springframework.mail.SimpleMailMessage;

import java.util.concurrent.ConcurrentHashMap;

public interface Verification {
    public int clear();
    public TokenInfo<User> check(String token, String code);
    public SimpleMailMessage addEmail(User user, String url, String receiver, String header, String content);

    public static ConcurrentHashMap<String, String> verificationCodeMap = new ConcurrentHashMap<>();
    public SimpleMailMessage addEmail(User user, String url, String header, String content);
}
