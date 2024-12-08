package com.emm.service.schedule;

import com.emm.config.AppConfig;
import com.emm.service.verification.DefaultVerificationImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {
    private static AppConfig appConfig;
    private final DefaultVerificationImpl defaultVerificationCodeImpl;
    @Autowired
    public ScheduledTasks(AppConfig appConfig, DefaultVerificationImpl defaultVerificationCodeImpl) {
        ScheduledTasks.appConfig = appConfig;
        this.defaultVerificationCodeImpl = defaultVerificationCodeImpl;
    }

    private long clearEmailVerificationCodeMapNextTime = 0;

//    @Scheduled(fixedRate = 1000)
//    public void executeTask() {
//        long time = System.currentTimeMillis();
//        this.clearEmailVerificationCodeMap(time);
//    }

    private void clearEmailVerificationCodeMap(long time) {
        if (this.clearEmailVerificationCodeMapNextTime < time) {
            clearEmailVerificationCodeMapNextTime = time + appConfig.getClearEmailVerificationCodeMapPeriod() * 1000;
            defaultVerificationCodeImpl.clear();
        }
    }
}
