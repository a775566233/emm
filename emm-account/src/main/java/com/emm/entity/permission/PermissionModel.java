package com.emm.entity.permission;

import com.emm.util.json.Serialize;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Getter
@Setter
public class PermissionModel {
    private HashMap<String, List<Integer>> permissionsConfig;
    private List<String> permissions;

    @Override
    public String toString() {
        try {
            return Serialize.toJson(this, SerializationFeature.INDENT_OUTPUT);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
