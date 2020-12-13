package com.elt.server;

import com.elt.server.enums.BizModel;
import com.elt.server.enums.OrgCode;
import com.elt.server.manager.PoolManager;
import com.elt.server.model.Pool;
import com.elt.server.util.CommUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

@SpringBootTest
class PoolTests {

    @Autowired
    private PoolManager poolManager;

    @Test
    public void createPool() {
        for (int j = 0; j < 500; j++) {
            List<Pool> list = new ArrayList<>();
            IntStream.range(0, 10000).forEach(i -> {
                list.add(this.genPool());
            });
            int i = poolManager.batchMerge(list);
            System.out.println(i);
        }
    }

    @Test
    public void fetchNew(){
        String idUser = "923793F596C011EAA8363D80CE873B8F";
        String s = poolManager.fetchNew(idUser);
        System.out.println(s);
    }

    private Pool genPool() {
        Pool pool = new Pool();
        pool.setIdPool(CommUtil.uuid());
        Random random = new Random();
        int i = random.nextInt(100);
        if (i < 5) {
            pool.setPriority(String.format("%0,3d", random.nextInt(100)));
        }
        pool.setIdRelation(CommUtil.uuid());
        pool.setBizModel(BizModel.random().toString());
        pool.setOrgCode(OrgCode.random().toString());
        CommUtil.model(pool);
        return pool;
    }

}
