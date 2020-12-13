package com.elt.server.manager;

import com.elt.server.model.*;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PoolPriorityManager {

    private static final Logger LOG = LoggerFactory.getLogger(PoolPriorityManager.class);

    private static final int CHECK_SIZE = 10000;

    /**
     * 待获取数据context
     */
    private Map<String, List<PoolPriority>> context = new ConcurrentHashMap<>();
    /**
     * 数据分组锁集合
     */
    private final Map<String, Lock> lockMap = new ConcurrentHashMap<>();
    /**
     * 业务模式优先级map
     */
    private Map<String, String> bizPriMap = new HashMap<>();
    /**
     * 机构模式优先级map
     */
    private Map<String, String> orgPriMap = new HashMap<>();
    /**
     * 已获取数据存放，与数据库对数之后移除，对数不通过则再次添加至待获取
     */
    private final List<PoolPriority> fq = new CopyOnWriteArrayList<>();
    /**
     * 数据同步开始时间，初始化context时候初始值，后续数据并入更新值
     */
    private Date syncStart = null;
    /**
     * 优先级排序规则
     */
    private final Comparator<? super PoolPriority> comparator =
            Comparator.comparing((Function<PoolPriority, String>) Pool::getPriority)
                    .thenComparing(PoolPriority::getPriorityBiz)
                    .thenComparing(PoolPriority::getPriorityOrg)
                    .thenComparing(BaseDto::getCreatedDate);

    @Autowired
    private PoolManager poolManager;

    @Autowired
    private PriorityBizManager priorityBizManager;

    @Autowired
    private PriorityOrgManager priorityOrgManager;

    @Autowired
    private PermissionManager permissionManager;

    public void initContext() {
        // --------------jvm资源打印-----------------------------------
        Runtime r = Runtime.getRuntime();
        long totalStart = r.totalMemory();
        long freeStart = r.freeMemory();
        LOG.info(String.format("total %s used %s unused %s", totalStart, r.maxMemory(), freeStart));
        // --------------jvm资源打印-----------------------------------
        // 数据同步开始时间标记，可以取数据库当前时间，这里先用系统时间
        syncStart = new Date();
        this.initPriMap();
        // 查询数据
        List<PoolPriority> list = poolManager.listAll();
        // --------------jvm资源打印-----------------------------------
        long totalEnd = r.totalMemory();
        long freeEnd = r.freeMemory();
        long used = ((totalEnd - freeEnd) - (totalStart - freeStart)) / 1024 / 1024;
        LOG.info(String.format("total %s used %s unused %s", totalEnd, r.maxMemory(), freeEnd));
        LOG.info(String.format("memory %s list size %d", used, list.size()));
        // --------------jvm资源打印-----------------------------------
        // 分组数据
        context = list.stream().collect(Collectors.groupingBy(this::key));
        // 合并后按分组进行数据排序
        context.entrySet().stream().parallel().forEach(this.listResort());
        // --------------jvm资源打印-----------------------------------
        totalEnd = r.totalMemory();
        freeEnd = r.freeMemory();
        used = ((totalEnd - freeEnd) - (totalStart - freeStart)) / 1024 / 1024;
        LOG.info(String.format("total %s used %s unused %s", totalEnd, r.maxMemory(), freeEnd));
        LOG.info(String.format("memory %s list size %d", used, context.size()));
        // --------------jvm资源打印-----------------------------------
    }

    public Pool fetchFromMemory(String idUser) {
        // 用户全部权限查询
        List<Permission> permissions = permissionManager.listByIdUser(idUser);
        // 权限（业务机构）排序
        List<String> keys = permissions.stream().map(this::key).distinct().sorted().collect(Collectors.toList());
        for (String key : keys) { // 按次序加锁（排序是为了避免死锁）
            Lock lock = lockMap.computeIfAbsent(key, i -> new ReentrantLock());
            lock.lock();
        }
        try {
            Map<PoolPriority, String> map = new HashMap<>();
            List<PoolPriority> results = new ArrayList<>();
            for (String key : keys) {
                List<PoolPriority> list = context.get(key); // 获取对应业务机构的资源集合
                if (list == null || list.isEmpty()) continue;
                PoolPriority pool = list.get(0);    // 获取优先级最高的一条数据
                map.put(pool, key);
                results.add(pool);
            }
            if (results.isEmpty()) return null; // 没有获取到资源直接返回null
            LOG.info("results {} size {}", results, results.size());
            results.sort(this.comparator);  // 所有业务机构最高优先级资源排序
            PoolPriority result = results.get(0);   // 取最高优先级数据
            String oKey = map.get(result);  // 找到改数据所在集合
            List<PoolPriority> list = context.get(oKey);
            list.remove(result);    // 从该集合移除该数据
            fq.add(result);         // 数据放入已获取集合供核数
            result.setUpdatedDate(new Date());
            result.setUpdatedBy(idUser);
            LOG.info("fetch end {}", result);
            return result;
        } finally { // 释放相应业务机构分组的锁
            for (String key : keys) {
                Lock lock = lockMap.get(key);
                lock.unlock();
            }
        }
    }

    public void mergeByTime() {
        if (syncStart == null) throw new RuntimeException("请先进行容器初始化");
        // now可以从数据库获取当前时间
        Date start = syncStart, end = DateUtils.addHours(start, 1), now = new Date();
        LOG.info("merge by time start {} end {}", start, end);
        if (end.after(now)) end = now;
        List<PoolPriority> list = poolManager.listByTime(start, end);
        this.mergeNewData(list);
        syncStart = end;
        LOG.info("merge by time sync date {}", syncStart);
    }

    public void checkFetched() {
        int size = fq.size();
        int checkSize = Math.min(size, CHECK_SIZE);
        List<PoolPriority> list = fq.subList(0, checkSize);
        Date date = DateUtils.addMinutes(new Date(), -5);
        List<PoolPriority> fl = list.stream().filter(i -> i.getUpdatedDate().before(date)).collect(Collectors.toList());
        fq.removeAll(fl);
        List<PoolPriority> nfl = poolManager.checkFetched(fl);
        if (CollectionUtils.isEmpty(nfl)) return;
        this.mergeNewData(nfl);
    }

    private void initPriMap() {
        // biz优先级配置查询
        bizPriMap = priorityBizManager.listAll().stream()
                .collect(Collectors.toMap(PriorityBiz::getBizModel, PriorityBiz::getPriority, (o, n) -> n));
        LOG.info("" + bizPriMap);
        // org优先级配置查询
        orgPriMap = priorityOrgManager.listAll().stream()
                .collect(Collectors.toMap(this::key, PriorityOrg::getPriority, (o, n) -> n));
        LOG.info("" + orgPriMap);
    }

    private void mergeNewData(List<PoolPriority> list) {
        Map<String, List<PoolPriority>> group = list.stream().collect(Collectors.groupingBy(this::key));
        for (Map.Entry<String, List<PoolPriority>> entry : group.entrySet()) {
            String key = entry.getKey();
            List<PoolPriority> ol = context.get(key);
            List<PoolPriority> nl = entry.getValue();
            Lock lock = lockMap.computeIfAbsent(key, i -> new ReentrantLock());
            lock.lock();
            try {
                if (ol != null) {
                    ol.addAll(nl);
                    ol.sort(comparator);
                } else {
                    nl.sort(comparator);
                    context.put(key, nl);
                }
            } finally {
                lock.unlock();
            }
        }
    }

    private Consumer<Map.Entry<String, List<PoolPriority>>> listResort() {
        return i -> {
            String key = i.getKey();
            List<PoolPriority> list = i.getValue();
            for (PoolPriority d : list) {   // 数据优先级填充
                if (d.getPriority() == null) d.setPriority("ZZZ");
                d.setPriorityBiz(bizPriMap.getOrDefault(d.getBizModel(), "ZZZ"));
                d.setPriorityOrg(orgPriMap.getOrDefault(this.key(d), "ZZZ"));
            }
            Lock lock = lockMap.computeIfAbsent(key, k -> new ReentrantLock());
            lock.lock();
            try {
                list.sort(comparator); //按优先级排序资源数据
            } finally {
                lock.unlock();
            }
        };
    }

    private String key(PoolPriority d) {
        return String.join(":", d.getBizModel(), d.getOrgCode());
    }

    private String key(PriorityOrg d) {
        return String.join(":", d.getBizModel(), d.getOrgCode());
    }

    private String key(Permission d) {
        return String.join(":", d.getBizModel(), d.getOrgCode());
    }
}
