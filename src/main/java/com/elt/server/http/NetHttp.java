package com.elt.server.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.elt.server.model.PriceNet;
import com.elt.server.util.HttpUtil;
import com.elt.server.util.TextUtil;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * etf day detail net
 * @author nature
 * @version 1.0.0
 * @since 2020/4/4 18:20
 */
public class NetHttp {
    /**
     * 链接地址：所有项目列表
     */
    private static final String URL_GZ = "http://api.fund.eastmoney.com/FundGuZhi/GetFundGZList?" +
            "type=%s&sort=1&orderType=asc&canbuy=0&pageIndex=1&pageSize=20000";

    /**
     * CHARSET
     */
    private static final String CHARSET = "utf8";
    private static final Map<String, String> HEADER = new HashMap<>();

    static {
        HEADER.put("Referer", "http://fund.eastmoney.com/fundguzhi.html");
    }

    private String date;

    /**
     * 获取k线数据
     * @param type type
     * @return list
     */
    public List<PriceNet> listGz(String type) {
        String uri = String.format(URL_GZ, type);
        String response = HttpUtil.doGet(uri, CHARSET, HEADER, lines -> lines.collect(Collectors.toList()).get(0));
        JSONObject jo = JSON.parseObject(response);
        JSONObject data = jo.getJSONObject("Data");
        if (data == null) throw new RuntimeException("数据缺失：" + type);
        this.date = data.getString("gxrq");
        JSONArray ks = data.getJSONArray("list");
        if (ks == null) throw new RuntimeException("数据缺失：" + type);
        List<PriceNet> list = new ArrayList<>();
        for (int i = 0; i < ks.size(); i++) {
            PriceNet net = this.genNet(ks.getJSONObject(i));
            if (net != null) list.add(net);
        }
        return list;
    }

    private PriceNet genNet(JSONObject jo) {
        String date = jo.getString("gxrq");
        if (!Objects.equals(date, this.date)) return null;
        Double net = TextUtil.getDouble(jo.getString("dwjz"));
        if (net == null) return null;
        Double rate = TextUtil.getPercent(jo.getString("jzzzl"));
        if (rate == null) return null;
        Double netGz = TextUtil.getDouble(jo.getString("gsz"));
        if (netGz == null) return null;
        Double rateGz = TextUtil.getPercent(jo.getString("gszzl"));
        if (rateGz == null) return null;
        Double rateDiff = TextUtil.getPercent(jo.getString("gspc"));
        if (rateDiff == null) return null;
        PriceNet i = new PriceNet();
        i.setCode(jo.getString("bzdm"));
        i.setNetLast(net);
        i.setNetLatest(netGz);
        return i;
    }


    public static void main(String[] args) {
        System.out.println(new Date(1595835243000l));
        NetHttp http = new NetHttp();
        PriceHttp priceHttp = new PriceHttp();
        List<PriceNet> nets = new CopyOnWriteArrayList<>();
        List<PriceNet> prices = new CopyOnWriteArrayList<>();
        Runnable[] runnables = new Runnable[]{
                () -> nets.addAll(http.listGz("8")),
                () -> nets.addAll(http.listGz("9")),
                () -> prices.addAll(priceHttp.list("2020-07-24"))
        };
        IntStream.range(0, 3).parallel().forEach(i -> {
            runnables[i].run();
        });
        System.out.println(new Date());
        Map<String, PriceNet> mapPrice = prices.stream().collect(Collectors.toMap(PriceNet::getCode, i -> i));
        Map<String, PriceNet> mapNet = nets.stream().collect(Collectors.toMap(PriceNet::getCode, i -> i));
        System.out.println(mapPrice.size());
        System.out.println(mapNet.size());
        mapPrice.keySet().retainAll(mapNet.keySet());
        mapPrice.values().forEach(p -> {
            PriceNet n = mapNet.get(p.getCode());
            p.setNetLast(n.getNetLast());
            p.setNetLatest(n.getNetLatest());
            p.setRateNet((p.getNetLatest() - p.getNetLast()) / p.getNetLast());
            p.setRatePrice((p.getPriceLatest() - p.getPriceLast()) / p.getPriceLast());
            p.setRateDiff((p.getPriceLatest() - p.getNetLatest()) / p.getNetLatest());
            p.setRateAmount(p.getAmount() / p.getScale());
        });
        System.out.println(mapPrice.size());
        List<PriceNet> list = new ArrayList<>(mapPrice.values()).stream().filter(i -> i.getScale() > 0)
                .sorted(Comparator.comparing(PriceNet::getRateAmount).reversed())
                .collect(Collectors.toList());
        System.out.println(list.size());
        for (PriceNet priceNet : list) {
            System.out.println(priceNet);
        }
    }
}
