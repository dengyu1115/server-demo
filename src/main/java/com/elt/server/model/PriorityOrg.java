package com.elt.server.model;

public class PriorityOrg extends BaseDto {
    private String idPriorityOrg;

    private String bizModel;

    private String orgCode;

    private String priority;

    public String getIdPriorityOrg() {
        return idPriorityOrg;
    }

    public void setIdPriorityOrg(String idPriorityOrg) {
        this.idPriorityOrg = idPriorityOrg;
    }

    public String getBizModel() {
        return bizModel;
    }

    public void setBizModel(String bizModel) {
        this.bizModel = bizModel;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

}