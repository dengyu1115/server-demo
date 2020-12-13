package com.elt.server.manager;

import com.elt.server.mapper.PriorityBizMapper;
import com.elt.server.model.PoolPriority;
import com.elt.server.model.PriorityBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.LinkedList;
import java.util.List;

@Service
public class PriorityBizManager {

    @Autowired
    private PriorityBizMapper priorityBizMapper;

    public int batchMerge(List<PriorityBiz> list) {
        if (CollectionUtils.isEmpty(list)) return 0;
        return priorityBizMapper.batchMerge(list);
    }

    public List<PriorityBiz> listAll() {
        int limit = 10000;
        String id = "";
        List<PriorityBiz> all = new LinkedList<>();
        do {
            List<PriorityBiz> list = priorityBizMapper.listAfter(id, limit);
            if (list.isEmpty()) break;
            all.addAll(list);
            id = list.get(list.size() - 1).getIdPriorityBiz();
        } while (true);
        return all;
    }
}
