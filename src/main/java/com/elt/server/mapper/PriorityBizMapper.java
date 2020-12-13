package com.elt.server.mapper;

import com.elt.server.model.PriorityBiz;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PriorityBizMapper {

    int deleteByPrimaryKey(String idPriorityBiz);

    int insert(PriorityBiz record);

    int insertSelective(PriorityBiz record);

    PriorityBiz selectByPrimaryKey(String idPriorityBiz);

    int updateByPrimaryKeySelective(PriorityBiz record);

    int updateByPrimaryKey(PriorityBiz record);

    int batchMerge(List<PriorityBiz> list);

    List<PriorityBiz> listAfter(@Param("id") String id, int limit);
}