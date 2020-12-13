package com.elt.server.model;

public class PriorityBiz extends BaseDto {
    private String idPriorityBiz;

    private String bizModel;

    private String priority;

    public String getIdPriorityBiz() {
        return idPriorityBiz;
    }

    public void setIdPriorityBiz(String idPriorityBiz) {
        this.idPriorityBiz = idPriorityBiz;
    }

    public String getBizModel() {
        return bizModel;
    }

    public void setBizModel(String bizModel) {
        this.bizModel = bizModel;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

}