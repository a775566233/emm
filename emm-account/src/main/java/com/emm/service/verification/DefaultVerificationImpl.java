package com.emm.service.verification;

import com.emm.config.AppConfig;
import com.emm.entity.Email;
import com.emm.entity.User;
import com.emm.entity.VerificationCode;
import com.emm.util.generate.RandomTools;
import com.emm.util.token.JWTTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class DefaultVerificationImpl implements Verification {
    private final AppConfig appConfig;

    public DefaultVerificationImpl(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    @Override
    public int clear() {
        List<String> removeTokens = new ArrayList<>();
        for (String token : Verification.verificationCodeMap.keySet()) {
            try {
                JWTTools.tokenVerify(token);
            } catch (Exception e) {
                removeTokens.add(token);
            }
        }
        for (String token : removeTokens) {
            Verification.verificationCodeMap.remove(token);
        }
        return removeTokens.size();
    }

    @Override
    public boolean check(String token, String code) {
        if (Verification.verificationCodeMap.containsKey(token)) {
            try {
                JWTTools.tokenVerify(token);
                if (Verification.verificationCodeMap.get(token).equals(code)) {
                    Verification.verificationCodeMap.remove(token);
                    log.info("token = {}, code = {} is valid", token, code);
                    return true;
                } else {
                    log.error("token = {}, code = {} is expire", token, code);
                    Verification.verificationCodeMap.remove(token);
                    return false;
                }
            } catch (Exception e) {
                log.error("token = {}, code = {} is invalid", token, code);
                return false;
            }
        } else {
            log.error("token = {}, code = {} is not null", token, code);
            return false;
        }
    }

    /**
     * 添加验证邮箱
     * @param user 用户信息
     * @param url 验证url
     * @param receiver 接收人
     * @param header 标题
     * @param content 具体邮件信息
     * @return 邮件
     */
    @Override
    public SimpleMailMessage addEmail(User user, String url, String receiver, String header, String content) {
        String token;
        try {
            token = JWTTools.createVerificationCodeJWT(user);
            String code = RandomTools.randomIntString(0, 9, appConfig.getVerificationCodeEmailLength());
            Verification.verificationCodeMap.put(token, code);
            Email email = new Email(
                    appConfig.getVerificationCodeEmailSender(),
                    receiver,
                    header,
                    content + url + token + " \n"
            );
            VerificationCode verificationCode = new VerificationCode(
                    appConfig.getVerificationCodeEmailPrefixContent(),
                    code,
                    appConfig.getVerificationCodeEmailSuffixContent(),
                    token
            );
            return email.generateEmail(verificationCode);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    /**
     * 添加验证邮箱
     * @param user 用户信息（带邮箱地址）
     * @param url 验证url
     * @param header 标题
     * @param content 具体邮件信息
     * @return 邮件
     */
    public SimpleMailMessage addEmail(User user, String url, String header, String content) {
        return this.addEmail(user, url, user.getUserEmail(), header, content);
    }
}
