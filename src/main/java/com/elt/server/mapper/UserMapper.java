package com.elt.server.mapper;

import com.elt.server.model.User;

public interface UserMapper {
    int deleteByPrimaryKey(String idUser);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(String idUser);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
}