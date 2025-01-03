package com.emm.entity.permission;

import com.emm.util.json.Serialize;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class PermissionModel {
    private HashMap<String, List<Integer>> permissionsConfig;
    @Getter
    private List<String> permissions;

    public PermissionModel() {}

    public PermissionModel(String[] permissions, HashMap<String, List<Integer>> permissionsConfig) {
        this.setPermissions(permissions);
        this.setPermissionsConfig(permissionsConfig);
    }

    public PermissionModel(List<String> permissions, HashMap<String, List<Integer>> permissionsConfig) {
        this.setPermissions(permissions);
        this.setPermissionsConfig(permissionsConfig);
    }

    public HashMap<String, List<Integer>> getPermissionsConfig() {
        adjust();
        return permissionsConfig;
    }

    private void adjust() {
        if (this.getPermissionsConfig() == null) {
            return;
        }
        for (String duties : this.getPermissionsConfig().keySet()) {
            if (this.getPermissions().isEmpty()) {
                this.getPermissionsConfig().replace(duties, new ArrayList<>());
            } else {
                int offset = 0;
                for (int i = 0; i < this.getPermissionsConfig().get(duties).size(); i++) {
                    if (this.getPermissionsConfig().get(duties).get(i) < 1) {
                        this.replacePermissionsConfigMapList(duties, i, 0);
                    } else if (this.getPermissionsConfig().get(duties).get(i) > 1) {
                        if (this.getPermissionsConfig().get(duties).size() < this.getPermissions().size()) {
                            offset = this.getPermissionsConfig().get(duties).get(i);
                        }
                        this.replacePermissionsConfigMapList(duties, i, 1);
                    }
                }
                while (this.getPermissionsConfig().get(duties).size() < this.getPermissions().size()) {
                    if (offset > 0) {
                        this.replacePermissionsConfigMapList(duties, 1);
                        offset--;
                    } else {
                        this.replacePermissionsConfigMapList(duties, 0);
                    }
                }
            }
        }
    }

    private List<Integer> replaceList(List<Integer> list, int index, int value) {
        List<Integer> result = new ArrayList<>(list);
        if (index >= list.size()) {
            result.add(value);
        } else {
            result.set(index, value);
        }
        return result;
    }

    private List<Integer> replaceList(List<Integer> list, int value) {
        List<Integer> result = new ArrayList<>(list);
        result.add(value);
        return result;
    }

    private void replacePermissionsConfigMapList(String key, int index, int value) {
        this.getPermissionsConfig().replace(key, replaceList(this.getPermissionsConfig().get(key), index, value));
    }

    private void replacePermissionsConfigMapList(String key, int value) {
        this.getPermissionsConfig().replace(key, replaceList(this.getPermissionsConfig().get(key), value));
    }

    @JsonProperty("permissionsConfig")
    public void setPermissionsConfig(HashMap<String, List<Integer>> permissionsConfig) {
        this.permissionsConfig = permissionsConfig;
        adjust();
    }

    @JsonProperty("permissions")
    public void setPermissions(List<String> permissions) {
        if (permissions != null && !permissions.isEmpty()) {
            this.permissions = permissions;
        } else {
            this.permissions = new ArrayList<>();
        }
        adjust();
    }

    public void setPermissions(String[] permissions) {
        if (permissions != null && permissions.length > 0) {
            this.setPermissions(Arrays.asList(permissions));
        } else {
            this.permissions = new ArrayList<>();
        }
        adjust();
    }

    @Override
    public String toString() {
        try {
            adjust();
            return Serialize.toJson(this, SerializationFeature.INDENT_OUTPUT);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
