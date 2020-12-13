package com.elt.server.manager;

import com.elt.server.mapper.PermissionMapper;
import com.elt.server.model.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class PermissionManager {

    @Autowired
    private PermissionMapper permissionMapper;

    public int batchMerge(List<Permission> list) {
        if (CollectionUtils.isEmpty(list)) return 0;
        return permissionMapper.batchMerge(list);
    }

    public List<Permission> listByIdUser(String idUser) {
        return permissionMapper.listByIdUser(idUser);
    }
}
