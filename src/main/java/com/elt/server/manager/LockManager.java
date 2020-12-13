package com.elt.server.manager;

import com.elt.server.mapper.LockMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LockManager {

    @Autowired
    private LockMapper lockMapper;

    public int lock(String idLock) {
        return lockMapper.lock(idLock);
    }
}
