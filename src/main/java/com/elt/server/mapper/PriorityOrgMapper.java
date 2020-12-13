package com.elt.server.mapper;

import com.elt.server.model.PriorityOrg;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PriorityOrgMapper {
    int deleteByPrimaryKey(String idPriorityOrg);

    int insert(PriorityOrg record);

    int insertSelective(PriorityOrg record);

    PriorityOrg selectByPrimaryKey(String idPriorityOrg);

    int updateByPrimaryKeySelective(PriorityOrg record);

    int updateByPrimaryKey(PriorityOrg record);

    int batchMerge(List<PriorityOrg> list);

    List<PriorityOrg> listAfter(@Param("id") String id, int limit);
}