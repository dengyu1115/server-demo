package com.elt.server.controller;

import com.elt.server.manager.PoolPriorityManager;
import com.elt.server.model.Pool;
import com.elt.server.res.PoolRes;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RequestMapping("poolPriority")
@RestController
public class PoolPriorityController {

    private static final Logger LOG = LoggerFactory.getLogger(PoolPriorityController.class);

    private static final String PATTERN = "yyyy-MM-dd HH:mm:ss";

    @Autowired
    private PoolPriorityManager poolPriorityManager;

    @PostMapping("initContext")
    public PoolRes initContext() {
        PoolRes res = new PoolRes();
        res.setTimeStart(DateFormatUtils.format(new Date(), PATTERN));
        poolPriorityManager.initContext();
        res.setTimeEnd(DateFormatUtils.format(new Date(), PATTERN));
        return res;
    }

    @PostMapping("mergeByTime")
    public PoolRes mergeByTime() {
        PoolRes res = new PoolRes();
        res.setTimeStart(DateFormatUtils.format(new Date(), PATTERN));
        poolPriorityManager.mergeByTime();
        res.setTimeEnd(DateFormatUtils.format(new Date(), PATTERN));
        return res;
    }

    @PostMapping("checkFetched")
    public PoolRes checkFetched() {
        PoolRes res = new PoolRes();
        res.setTimeStart(DateFormatUtils.format(new Date(), PATTERN));
        poolPriorityManager.checkFetched();
        res.setTimeEnd(DateFormatUtils.format(new Date(), PATTERN));
        return res;
    }

    @PostMapping("fetchFromMemory")
    public Pool fetchFromMemory(String idUser) {
        LOG.info("fetch start idUser {}", idUser);
        Pool pool = poolPriorityManager.fetchFromMemory(idUser);
        LOG.info("fetch end pool {}", pool);
        return pool;
    }

}
