package com.elt.server;

import com.elt.server.manager.UserManager;
import com.elt.server.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserTests {

    @Autowired
    private UserManager userManager;

    @Test
    public void findById() {
        User user = userManager.findById("923793F596C011EAA8363D80CE873B8F");
        System.out.println(user);
    }

}
