package com.emm.entity;

import lombok.Getter;


public record VerificationCode(String prefixContent, String code, String suffixContent, String token) {

    @Override
    public String toString() {
        return prefixContent + code + suffixContent;
    }
}
