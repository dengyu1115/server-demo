package com.elt.server.manager;

import com.elt.server.mapper.UserMapper;
import com.elt.server.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserManager {

    @Autowired
    private UserMapper userMapper;

    public User findById(String id) {
        return userMapper.selectByPrimaryKey(id);
    }
}
