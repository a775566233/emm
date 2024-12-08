package com.emm.entity;

import com.emm.config.AppConfig;
import com.emm.util.generate.RandomTools;
import lombok.Getter;
import lombok.Setter;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

@Setter
@Getter
public class Email {
    private String header;
    private String content;
    private String sender;
    private String receiver;

    public Email(String sender, String receiver) {
        this.sender = sender;
        this.receiver = receiver;
    }

    public Email(String sender, String receiver, String header, String content) {
        this(sender, receiver);
        this.content = content;
        this.header = header;
    }

    public SimpleMailMessage generateEmail() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(receiver);
        message.setFrom(sender);
        message.setSubject(header);
        message.setText(content);
        return message;
    }

    public SimpleMailMessage generateEmail(VerificationCode verificationCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(receiver);
        message.setFrom(sender);
        message.setSubject(header);
        message.setText(content + verificationCode);
        return message;
    }
}
