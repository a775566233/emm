package com.emm.util.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class Serialize {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String toJson(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }

    public static String toJson(Object obj, SerializationFeature serializationFeature) throws JsonProcessingException {
        ObjectMapper customObjectMapper = new ObjectMapper();
        customObjectMapper.enable(serializationFeature);
        //customObjectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return customObjectMapper.writeValueAsString(obj);
    }
}
