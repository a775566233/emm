package com.emm.entity.information;

public enum ResponseEnum {
    SUCCESS("200", "success"),
    SERVER_BUSY("500","server busy"),
    SERVER_ERROR("500","server error"),
    REQUEST_ILLEGAL("400","request illegal"),
    REQUEST_NULL("400","request null"),
    USER_NOT_FOUND("404","user not found"),
    ADD_USER_ILLEGAL("400","add user illegal"),
    NOT_FOUND("404","not found"),
    ACCESS_TOKEN_EXPIRED("401","access_token expired"),
    ACCESS_TOKEN_INVALID("401","access_token invalid"),
    ACCESS_TOKEN_ALGORITHM_MISMATCH("401","access_token algorithm mismatch"),
    ACCESS_TOKEN_SIGNATURE_VERIFICATION("401","access_token signature verification exception"),
    ACCESS_TOKEN_NOT_FOUND("401","access_token not found"),
    REFRESH_TOKEN_EXPIRED("401","refresh_token expired"),
    REFRESH_TOKEN_INVALID("401","refresh_token invalid"),
    REFRESH_TOKEN_NOT_FOUND("401","refresh_token not found"),
    REFRESH_TOKEN_ALGORITHM_MISMATCH("401","refresh_token algorithm mismatch"),
    REFRESH_TOKEN_SIGNATURE_VERIFICATION("401","refresh_token signature verification exception"),
    REFRESH_TOKEN_UPDATE_NOT_ALLOWED("401","refresh_token update not allowed"),
    VERIFICATION_CODE_EXPIRED("401","verification code expired"),
    VERIFICATION_CODE_INVALID("401","verification code invalid"),
    ;
    private String code;
    private String message;

    ResponseEnum(String code, String message) {
        this.setCode(code);
        this.setMessage(message);
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
