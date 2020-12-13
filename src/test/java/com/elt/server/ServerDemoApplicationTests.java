package com.elt.server;

import com.elt.server.mapper.UserMapper;
import com.elt.server.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ServerDemoApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void contextLoads() {
        User user = userMapper.selectByPrimaryKey("923793F596C011EAA8363D80CE873B8F");
        System.out.println(user);
    }

}
