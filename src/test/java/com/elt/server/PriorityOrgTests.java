package com.elt.server;

import com.elt.server.manager.PriorityOrgManager;
import com.elt.server.model.PriorityOrg;
import com.elt.server.util.CommUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

@SpringBootTest
class PriorityOrgTests {

    @Autowired
    private PriorityOrgManager priorityOrgManager;

    @Test
    public void createPriorityOrg() {
        Set<String> set = new HashSet<>();
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            int n = random.nextInt(100);
            if (n < 20) {
                set.add(String.format("BM%0,3d:OC%0,3d", i, random.nextInt(100)));
            }
        }
        System.out.println(set);
        List<PriorityOrg> list = new ArrayList<>();
        for (String s : set) {
            String[] split = s.split(":");
            list.add(this.genPriorityOrg(split[0], split[1]));
        }
        System.out.println(priorityOrgManager.batchMerge(list));
    }

    private PriorityOrg genPriorityOrg(String bizModel, String orgCode) {
        PriorityOrg d = new PriorityOrg();
        d.setIdPriorityOrg(CommUtil.uuid());
        Random random = new Random();
        d.setPriority(String.format("%0,3d", random.nextInt(100)));
        d.setBizModel(bizModel);
        d.setOrgCode(orgCode);
        CommUtil.model(d);
        return d;
    }

}
