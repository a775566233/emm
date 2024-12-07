package com.emm.entity;

import com.emm.util.json.Deserialization;
import com.emm.util.json.Serialize;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
public class User {
    private long userId;
    private String userName;
    private String userPassword;
    private String userEmail;
    private String userArea;
    private String userPhone;
    private String userDuties;
    private int status;
    private byte[] userUUID;
    private long registerStamp;

    public User() {}
    public User(String userName, byte[] userUUID) {
        this.userName = userName;
        this.userUUID = userUUID;
    }
}
