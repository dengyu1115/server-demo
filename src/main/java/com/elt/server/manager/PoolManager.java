package com.elt.server.manager;

import com.elt.server.mapper.PoolMapper;
import com.elt.server.model.Pool;
import com.elt.server.model.PoolPriority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PoolManager {

    private static final int CHECK_SIZE = 200;

    @Autowired
    private PoolMapper poolMapper;

    @Autowired
    private PoolManager poolManager;

    @Autowired
    private LockManager lockManager;

    @Autowired
    private PoolPriorityManager poolPriorityManager;

    private static final String URL = "http://localhost:8090/poolPriority/fetchFromMemory?idUser=%s";

    private String fetchFrom = "db";

    public int batchMerge(List<Pool> list) {
        if (CollectionUtils.isEmpty(list)) return 0;
        return poolMapper.batchMerge(list);
    }

    public String fetchNew(String idUser) {
        Pool fetched;
        if ("db".equals(fetchFrom)) {
            fetched = this.fetchFromPool(idUser);
        } else {
            fetched = this.fetchFromRemote(idUser);
        }
        if (fetched == null) return null;
        // 判断该数据是否曾经获取过
        String idPool = fetched.getIdPool();
        int lock = lockManager.lock(idPool);
        if (lock != 1) {
            throw new RuntimeException(idPool + "已经被获取过了");
        }
        poolManager.finishFetch(fetched);
        return idPool;
    }

    private Pool fetchFromRemote(String idUser) {
        return new RestTemplate().postForObject(String.format(URL, idUser), null, Pool.class);
    }

    @Transactional
    public void finishFetch(Pool fetched) {
        int deleted = poolMapper.deleteByPrimaryKey(fetched.getIdPool());
        if (deleted != 1) throw new RuntimeException("资源不存在");
        // 将该数据放入副本池
        int copied = poolMapper.insertCopy(fetched);
        if (copied != 1) throw new RuntimeException("资源复制异常");
    }

    public List<Pool> listHighestPriorities(String idUser) {
        return poolMapper.listHighestPriorities(idUser);
    }

    public int bindToUser(Pool pool) {
        return poolMapper.bindToUser(pool);
    }

    public List<PoolPriority> listAll() {
        int limit = 10000;
        String idPool = "";
        List<PoolPriority> all = new LinkedList<>();
        do {
            List<PoolPriority> list = poolMapper.listAfter(idPool, limit);
            if (list.isEmpty()) break;
            all.addAll(list);
            idPool = list.get(list.size() - 1).getIdPool();
        } while (true);
        return all;
    }


    public List<PoolPriority> listByTime(Date start, Date end) {
        int limit = 10000;
        int begin = 0;
        List<PoolPriority> all = new LinkedList<>();
        do {
            List<PoolPriority> list = poolMapper.listByTime(start, end, begin, limit);
            all.addAll(list);
            if (list.size() < limit) break;
            begin += limit;
        } while (true);
        return all;
    }

    /**
     * 从池子获取一个最高优先级的资源
     * @param idUser 用户ID
     * @return pool
     */
    private Pool fetchFromPool(String idUser) {
        // 查询最高优先级的任务池数据
        List<Pool> list = this.listHighestPriorities(idUser);
        // 更新该数据，更新成功则返回该数据否则重试下一条数据
        Pool fetched = null;
        for (Pool pool : list) {
            pool.setIdUser(idUser);
            int bind = this.bindToUser(pool);
            if (bind == 1) {
                fetched = pool;
                break;
            }
        }
        return fetched;
    }

    public String changeDB(String type) {
        return fetchFrom = type;
    }

    public List<PoolPriority> checkFetched(List<PoolPriority> fl) {
        List<PoolPriority> nfl = new ArrayList<>();
        if (CollectionUtils.isEmpty(fl)) {
            return nfl;
        }
        List<String> ids = fl.stream().map(PoolPriority::getIdPool).distinct().collect(Collectors.toList());
        int size = ids.size();
        int batchSize = Math.min(size, CHECK_SIZE);
        int start = 0;
        Set<String> fIds = new TreeSet<>();
        do {
            if (start > size) start = size;
            List<String> idList = ids.subList(start, batchSize);
            List<String> fsIds = poolMapper.checkFetched(idList);
            fIds.addAll(fsIds);
            if (start == size) break;
            start += batchSize;
        } while (true);
        Set<String> nfIds = new TreeSet<>(ids);
        nfIds.removeAll(fIds);
        return fl.stream().filter(i -> nfIds.contains(i.getIdPool())).collect(Collectors.toList());
    }
}
