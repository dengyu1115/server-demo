package com.elt.server.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.elt.server.model.PriceNet;
import com.elt.server.util.HttpUtil;
import com.elt.server.util.TextUtil;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * etf day detail net
 * @author nature
 * @version 1.0.0
 * @since 2020/4/4 18:20
 */
public class PriceHttp {
    /**
     * 链接地址：所有项目列表
     */
    private static final String URL_LATEST = "https://push2.eastmoney.com/api/qt/clist/get?pn=1&pz=10000&np=1&fltt=2" +
            "&fs=b:MK0021,b:MK0022,b:MK0023,b:MK0024&fid=f12&fields=f12,f14,f2,f18,f6,f21,f124";

    /**
     * CHARSET
     */
    private static final String CHARSET = "utf8";
    private static final String FORMAT_DATE = "yyyy-MM-dd";

    private String date;

    public List<PriceNet> list(String date) {
        String response = HttpUtil.doGet(URL_LATEST, CHARSET, lines -> lines.collect(Collectors.toList()).get(0));
        JSONObject jo = JSON.parseObject(response);
        JSONObject data = jo.getJSONObject("data");
        if (data == null) throw new RuntimeException("网路数据缺失");
        JSONArray ks = data.getJSONArray("diff");
        this.date = date;
        List<PriceNet> list = new ArrayList<>();
        for (int i = 0; i < ks.size(); i++) {
            PriceNet kline = this.genKline(ks.getJSONObject(i));
            if (kline != null) list.add(kline);
        }
        return list;
    }

    private PriceNet genKline(JSONObject jo) {
        Double time = TextUtil.getDouble(jo.getString("f124"));
        if (time == null) return null;
        String date = DateFormatUtils.format(new Date((long) (time * 1000L)), FORMAT_DATE);
        if (!Objects.equals(date, this.date)) return null;
        String code = jo.getString("f12");
        if (code == null) return null;
        String name = jo.getString("f14");
        if (name == null) return null;
        Double priceLast = TextUtil.getDouble(jo.getString("f18"));
        if (priceLast == null) return null;
        Double priceLatest = TextUtil.getDouble(jo.getString("f2"));
        if (priceLatest == null) return null;
        Double amount = TextUtil.getDouble(jo.getString("f6"));
        if (amount == null) return null;
        Double scale = TextUtil.getDouble(jo.getString("f21"));
        if (scale == null) return null;
        PriceNet i = new PriceNet();
        i.setCode(code);
        i.setName(name);
        i.setPriceLast(priceLast);
        i.setPriceLatest(priceLatest);
        i.setAmount(amount);
        i.setScale(scale);
        return i;
    }

}
