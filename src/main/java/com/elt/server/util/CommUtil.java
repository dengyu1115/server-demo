package com.elt.server.util;

import com.elt.server.model.BaseDto;

import java.util.Date;
import java.util.UUID;

public class CommUtil {

    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }

    public static void model(BaseDto model) {
        model.setCreatedBy("ELT");
        model.setUpdatedBy("ELT");
        model.setCreatedDate(new Date());
        model.setUpdatedDate(new Date());
    }
}
