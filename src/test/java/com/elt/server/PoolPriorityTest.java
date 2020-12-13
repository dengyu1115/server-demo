package com.elt.server;

import com.elt.server.manager.PoolPriorityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PoolPriorityTest {

    private static final String ID_USER = "923793F596C011EAA8363D80CE873B8F";

    @Autowired
    private PoolPriorityManager poolPriorityManager;

    @Test
    public void initContext() {
        poolPriorityManager.initContext();
    }

    @Test
    public void fetchFromMemory() {
        poolPriorityManager.initContext();
        poolPriorityManager.fetchFromMemory(ID_USER);
    }

}
