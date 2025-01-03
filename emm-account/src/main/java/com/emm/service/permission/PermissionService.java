package com.emm.service.permission;

import com.emm.config.AppConfig;
import com.emm.entity.permission.DutiesEnum;
import com.emm.entity.permission.PermissionModel;
import com.emm.entity.permission.PermissionsEnum;
import com.emm.util.file.FileTools;
import com.emm.util.json.Deserialize;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
public class PermissionService {
    private static AppConfig appConfig;
    private final String PermissionConfigDir;
    @Getter
    private PermissionModel permissionModel;

    public PermissionService(AppConfig appConfig) {
        PermissionService.appConfig = appConfig;
        this.PermissionConfigDir = appConfig.getDataDir() + "\\" + appConfig.getDataConfigPermissionConfig();
        this.permissionModel = this.loadingPermissionModel();
    }

    public void setPermissionModel(PermissionModel permissionModel) {}

    public PermissionModel loadingPermissionModel() {
        try {
            return Deserialize.toObject(FileTools.getFileContent(this.PermissionConfigDir), PermissionModel.class);
        } catch (IOException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public boolean checkPermission(DutiesEnum dutiesEnum, PermissionsEnum permissionsEnum) {
        AtomicInteger index = new AtomicInteger(-1);
        this.permissionModel.getPermissions()
                .forEach(permission -> {
                    if (permission.equals(permissionsEnum.toString())) {
                        index.set(index.get() + 1);
                    }
                });
        if (index.get() < 0
                || this.permissionModel.getPermissions() == null
                || this.permissionModel.getPermissionsConfig().get(dutiesEnum.toString()) == null) {
            return false;
        }
        return this.permissionModel.getPermissionsConfig().get(dutiesEnum.toString()).get(index.get()) == 1;
    }
}
