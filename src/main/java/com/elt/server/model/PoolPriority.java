package com.elt.server.model;

public class PoolPriority extends Pool {

    private String priorityBiz;


    private String priorityOrg;

    public String getPriorityBiz() {
        return priorityBiz;
    }

    public void setPriorityBiz(String priorityBiz) {
        this.priorityBiz = priorityBiz;
    }

    public String getPriorityOrg() {
        return priorityOrg;
    }

    public void setPriorityOrg(String priorityOrg) {
        this.priorityOrg = priorityOrg;
    }
}