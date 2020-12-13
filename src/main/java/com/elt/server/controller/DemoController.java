package com.elt.server.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("demo")
@RestController
public class DemoController {

    @RequestMapping("test")
    public Map<String, Object> test(String key) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(key, new Date());
        System.out.println(map);
        return map;
    }

}
