package com.elt.server;

import com.elt.server.manager.PriorityBizManager;
import com.elt.server.model.PriorityBiz;
import com.elt.server.util.CommUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

@SpringBootTest
class PriorityBizTests {

    @Autowired
    private PriorityBizManager priorityBizManager;

    @Test
    public void createPriorityBiz() {
        Set<String> set = new HashSet<>();
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            int n = random.nextInt(100);
            if (n < 10) {
                set.add(String.format("BM%0,3d", i));
            }
        }
        System.out.println(set);
        List<PriorityBiz> list = new ArrayList<>();
        for (String s : set) {
            list.add(this.genPriorityBiz(s));
        }
        System.out.println(priorityBizManager.batchMerge(list));
    }

    private PriorityBiz genPriorityBiz(String bizModel) {
        PriorityBiz d = new PriorityBiz();
        d.setIdPriorityBiz(CommUtil.uuid());
        Random random = new Random();
        d.setPriority(String.format("%0,3d", random.nextInt(100)));
        d.setBizModel(bizModel);
        CommUtil.model(d);
        return d;
    }

}
