package com.emm.entity.template;

import com.emm.util.json.Serialize;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Getter
@Setter
public class CheckTemplate {
    @JsonProperty("fieldName")
    private String fieldName;
    @JsonProperty("isNullable")
    private boolean isNullable;
    @JsonProperty("minLength")
    private int minLength;
    @JsonProperty("maxLength")
    private int maxLength;
    @JsonProperty("regexString")
    private String regexString;

    public CheckTemplate() {}

    public CheckTemplate(String fieldName, boolean isNullable, int minLength, int maxLength, String regexString) {
        this.fieldName = fieldName;
        this.isNullable = isNullable;
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.regexString = regexString;
    }

    public Pattern getRegex() {
        if (regexString == null) {
            return null;
        }
        return Pattern.compile(regexString);
    }

    @Override
    public String toString() {
        try {
            return Serialize.toJson(this, SerializationFeature.INDENT_OUTPUT);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
