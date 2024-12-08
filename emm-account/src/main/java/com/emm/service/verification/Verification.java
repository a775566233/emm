package com.emm.service.verification;

import com.emm.entity.User;
import com.emm.entity.VerificationCode;
import org.springframework.mail.SimpleMailMessage;

import java.util.concurrent.ConcurrentHashMap;

public interface Verification {
    public int clear();
    public boolean check(String token, String code);
    public SimpleMailMessage addEmail(User user, String url, String receiver, String header, String content);

    public static ConcurrentHashMap<String, String> verificationCodeMap = new ConcurrentHashMap<>();
}
