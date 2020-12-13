package com.elt.server.mapper;

import com.elt.server.model.Permission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PermissionMapper {

    int deleteByPrimaryKey(String idPermission);

    int insert(Permission record);

    int insertSelective(Permission record);

    Permission selectByPrimaryKey(String idPermission);

    int updateByPrimaryKeySelective(Permission record);

    int updateByPrimaryKey(Permission record);

    int batchMerge(List<Permission> list);

    List<Permission> listByIdUser(@Param("idUser") String idUser);
}