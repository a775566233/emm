package com.emm.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Component
public class AppConfig {
    //基本信息
    @Value("${app_name}")
    private String app_name;
    @Value("${version}")
    private String version;
    @Value("${author}")
    private String author;
    private List<String> features = new ArrayList<>(){};
    @Value("${settings.analytics}")
    private Boolean analytics;
    @Value("${encoding-type}")
    private String encodingType;

    //jwt配置
    @Value("${jwt.secret}")
    private String SECRET;
    @Value("${jwt.access-token-expire}")
    private long ACCESS_TOKEN_EXPIRE;
    @Value("${jwt.refresh-token-expire}")
    private long REFRESH_TOKEN_EXPIRE;
    @Value("${jwt.algorithm}")
    private String ALGORITHM;
    @Value("${jwt.allow-update-refresh-tokens}")
    private boolean ALLOW_UPDATE_REFRESH_TOKENS;

    //加密配置
    @Value("${storage.user-password.encryption}")
    private String userPasswordEncryption;
    @Value("${storage.user-password.frequency}")
    private int userPasswordEncryptionFrequency;
    @Value("${storage.user-password.prefix-salt}")
    private String userPasswordPrefixSalt;
    @Value("${storage.user-password.suffix-salt}")
    private String userPasswordSuffixSalt;
    @Value("${storage.user-phone.encryption}")
    private String userPhoneEncryption;
    @Value("${storage.user-phone.frequency}")
    private int userPhoneFrequency;
    @Value("${storage.user-phone.prefix-salt}")
    private String userPhonePrefixSalt;
    @Value("${storage.user-phone.suffix-salt}")
    private String userPhoneSuffixSalt;

    //请求头、响应头配置
    @Value("${web.header.access-token}")
    private String webHeaderAccessToken;
    @Value("${web.header.refresh-token}")
    private String webHeaderRefreshToken;
    @Value("${web.header.email-verification-token}")
    private String webHeaderEmailVerificationToken;
    @Value("${web.cors.mapping}")
    private String webCorsMapping;
    @Value("${web.cors.allowed-origins}")
    private String webCorsAllowedOrigins;
    @Value("${web.cors.allow-credentials}")
    private boolean webCorsAllowCredentials;
    @Value("${web.cors.allowed-origin-patterns}")
    private String webCorsAllowedOriginPatterns;
    @Value("${web.cors.allowed-methods}")
    private String[] webCorsAllowedMethods;
    @Value("${web.cors.allowed-headers}")
    private String webCorsAllowedHeaders;
    @Value("${web.cors.exposed-headers}")
    private String webCorsExposedHeaders;

    //计划任务
    @Value("${schedule.clear-email-verification-code-map-period}")
    private long clearEmailVerificationCodeMapPeriod;

    //邮箱验证
    @Value("${verification-code.email.expire}")
    private long verificationCodeEmailExpire;
    @Value("${verification-code.email.length}")
    private int verificationCodeEmailLength;
    @Value("${verification-code.email.prefix-content}")
    private String verificationCodeEmailPrefixContent;
    @Value("${verification-code.email.suffix-content}")
    private String verificationCodeEmailSuffixContent;
    @Value("${verification-code.email.url}")
    private String verificationCodeEmailUrl;
    @Value("${spring.mail.username}")
    private String verificationCodeEmailSender;

    //持久数据目录
    @Value("${data.dir}")
    private String dataDir;
    @Value("${data.config.permission-config}")
    private String dataConfigPermissionConfig;
    @Value("${data.config.check-template-config}")
    private String dataConfigCheckTemplateConfig;

    public String getPermissionConfigPath() {
        return this.getDataDir() + this.getDataConfigPermissionConfig();
    }
    public String getCheckTemplateConfigPath() {
        return this.getDataDir() + this.getDataConfigCheckTemplateConfig();
    }
}
