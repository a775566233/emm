package com.emm.util.json;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class Deserialization {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    public static <T> T toObject(String json, T object) throws JsonParseException, JsonMappingException, IOException {
        return objectMapper.readValue(json, new TypeReference<>() {});
    }
}
