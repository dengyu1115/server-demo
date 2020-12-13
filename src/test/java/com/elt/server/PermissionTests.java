package com.elt.server;

import com.elt.server.manager.PermissionManager;
import com.elt.server.model.Permission;
import com.elt.server.util.CommUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

@SpringBootTest
class PermissionTests {

    @Autowired
    private PermissionManager permissionManager;

    private static final String ID_USER = "923793F596C011EAA8363D80CE873B8F";

    @Test
    public void createPermission() {
        Set<String> set = new HashSet<>();
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            int n = random.nextInt(100);
            if (n < 20) {
                set.add(String.format("BM%0,3d:OC%0,3d", i, random.nextInt(100)));
            }
        }
        System.out.println(set);
        List<Permission> list = new ArrayList<>();
        for (String s : set) {
            String[] split = s.split(":");
            list.add(this.genPermission(split[0], split[1]));
        }
        System.out.println(permissionManager.batchMerge(list));
    }

    private Permission genPermission(String bizModel, String orgCode) {
        Permission d = new Permission();
        d.setIdPermission(CommUtil.uuid());
        d.setIdUser(ID_USER);
        d.setBizModel(bizModel);
        d.setOrgCode(orgCode);
        CommUtil.model(d);
        return d;
    }

}
