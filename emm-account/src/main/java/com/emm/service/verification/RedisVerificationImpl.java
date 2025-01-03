package com.emm.service.verification;

import com.emm.entity.User;
import com.emm.entity.token.TokenInfo;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public class RedisVerificationImpl implements Verification {
    @Override
    public int clear() {
        return 0;
    }

    @Override
    public TokenInfo<User> check(String token, String code) {
        return null;
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
        return null;
    }

    /**
     * 添加验证邮箱
     * @param user 用户信息（带邮箱地址）
     * @param url 验证url
     * @param header 标题
     * @param content 具体邮件信息
     * @return 邮件
     */
    @Override
    public SimpleMailMessage addEmail(User user, String url, String header, String content) {
        return null;
    }
}
