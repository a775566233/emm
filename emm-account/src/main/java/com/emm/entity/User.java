package com.emm.entity;

import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
public class User {
    private long userId = 0;
    private String userName;
    private String userPassword;
    private String userEmail;
    private String userArea;
    private String userPhone;
    private String userDuties;
    private int status;
    private String userUUID;
    private long registerStamp;

    public User() {}
    public User(String userName, String userUUID) {
        this.userName = userName;
        this.userUUID = userUUID;
    }

    public User(long userId, String userName, String userUUID) {
        this.userId = userId;
        this.userName = userName;
        this.userUUID = userUUID;
    }
}
