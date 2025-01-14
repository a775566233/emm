package com.emm.config;

import com.emm.entity.permission.PermissionModel;
import com.emm.entity.template.CheckTemplate;
import com.emm.util.file.FileTools;
import com.emm.util.json.Deserialize;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Component
@Setter
@Getter
@Slf4j
public class DataConfig {
    private final AppConfig appConfig;

    private PermissionModel permissionModel;
    private HashMap<String, CheckTemplate> checkTemplateMap;

    @Autowired
    public DataConfig(AppConfig appConfig) {
        this.appConfig = appConfig;

    }

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        String jsonString;
        try {
            jsonString = FileTools.getFileContent(appConfig.getPermissionConfigPath());
            this.permissionModel = Deserialize.toObject(jsonString, PermissionModel.class);
        } catch (IOException e) {
            log.error("权限配置设置出错，读取相关json文件异常：{}", e.getMessage());
            throw new RuntimeException();
        }
        try {
            jsonString = FileTools.getFileContent(appConfig.getCheckTemplateConfigPath());
            List<CheckTemplate> checkTemplates = Deserialize.toList(jsonString, new TypeReference<List<CheckTemplate>>() {});
            checkTemplateMap = new HashMap<>();
            for (CheckTemplate checkTemplate : checkTemplates) {
                checkTemplateMap.put(checkTemplate.getFieldName(), checkTemplate);
            }
        } catch (IOException e) {
            log.error("输入检查模板设置出错，读取相关json文件异常：{}", e.getMessage());
            throw new RuntimeException();
        }
    }
}
