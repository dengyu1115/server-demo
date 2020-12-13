package com.elt.server.controller;

import com.elt.server.manager.PoolManager;
import com.elt.server.res.PoolRes;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RequestMapping("pool")
@RestController
public class PoolController {

    private static final String PATTERN = "yyyy-MM-dd HH:mm:ss";

    @Autowired
    private PoolManager poolManager;

    @PostMapping("fetchNew")
    public PoolRes fetchNew(String idUser) {
        PoolRes res = new PoolRes();
        res.setTimeStart(DateFormatUtils.format(new Date(), PATTERN));
        res.setIdRelation(poolManager.fetchNew(idUser));
        res.setTimeEnd(DateFormatUtils.format(new Date(), PATTERN));
        return res;
    }

    @PostMapping("changeDB")
    public String changeDB(String type) {
        return poolManager.changeDB(type);
    }

}
