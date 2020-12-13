package com.elt.server.mapper;

import org.apache.ibatis.annotations.Param;

public interface LockMapper {

    int lock(@Param("idLock") String idLock);
}
