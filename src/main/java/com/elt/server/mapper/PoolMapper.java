package com.elt.server.mapper;

import com.elt.server.model.Pool;
import com.elt.server.model.PoolPriority;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface PoolMapper {

    int deleteByPrimaryKey(String idPool);

    int insert(Pool record);

    int insertSelective(Pool record);

    Pool selectByPrimaryKey(String idPool);

    int updateByPrimaryKeySelective(Pool record);

    int updateByPrimaryKey(Pool record);

    int batchMerge(List<Pool> list);

    List<Pool> listHighestPriorities(@Param("idUser") String idUser);

    int insertCopy(Pool record);

    int bindToUser(Pool record);

    List<PoolPriority> listAfter(@Param("idPool") String idPool, @Param("limit") int limit);

    List<PoolPriority> listByTime(@Param("start") Date start, @Param("end") Date end,
                                  @Param("begin") int begin, @Param("limit") int limit);

    List<String> checkFetched(@Param("ids") List<String> ids);
}