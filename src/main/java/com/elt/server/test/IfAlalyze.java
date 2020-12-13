package com.elt.server.test;

import ognl.*;

import java.util.HashMap;
import java.util.Map;

public class IfAlalyze {

    static String PARAM = "(a>10&&b<9)||c>5";

    public static void main(String[] args) throws OgnlException {

        C c = new C();
        c.a = 10d;
        c.b = 5d;
        c.c = 15d;
        ClassResolver resolver = new DefaultClassResolver();
        TypeConverter converter = new DefaultTypeConverter();
        OgnlContext context = new OgnlContext();
        Map<String, Object> map = new HashMap<>();
        map.put("c", c);
        context.setValues(map);
        Object o = Ognl.getValue(PARAM, context, context.getRoot());
        System.out.println(o);
    }

    /**
     * 计算一对括号中结果
     * @param s
     * @param c
     * @return
     */
    private static String calculateALL(String s, C c) {
        String u = s.substring(0, s.indexOf(")") + 1);
        u = u.substring(u.lastIndexOf("("));
        boolean r = calculateUnit(u, c);
        return s.replace(u, String.valueOf(r));
    }

    private static boolean calculateUnit(String u, C c) {
        if (!u.contains(")")) u = u.substring(1, u.length() - 1);

        if (u.indexOf("||") != -1) {

        }
        System.out.println(u);
        return false;
    }

    static class C {
        Double a;
        Double b;
        Double c;
    }
}
