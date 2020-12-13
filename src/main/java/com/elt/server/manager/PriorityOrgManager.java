package com.elt.server.manager;

import com.elt.server.mapper.PriorityOrgMapper;
import com.elt.server.model.PriorityOrg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.LinkedList;
import java.util.List;

@Service
public class PriorityOrgManager {

    @Autowired
    private PriorityOrgMapper priorityOrgMapper;

    public int batchMerge(List<PriorityOrg> list) {
        if (CollectionUtils.isEmpty(list)) return 0;
        return priorityOrgMapper.batchMerge(list);
    }

    public List<PriorityOrg> listAll() {
        int limit = 10000;
        String id = "";
        List<PriorityOrg> all = new LinkedList<>();
        do {
            List<PriorityOrg> list = priorityOrgMapper.listAfter(id, limit);
            if (list.isEmpty()) break;
            all.addAll(list);
            id = list.get(list.size() - 1).getIdPriorityOrg();
        } while (true);
        return all;
    }
}
