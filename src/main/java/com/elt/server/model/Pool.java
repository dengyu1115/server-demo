package com.elt.server.model;

public class Pool extends BaseDto {
    private String idPool;

    private String idRelation;

    private String idUser;

    private String priority;

    private String bizModel;

    private String orgCode;


    public String getIdPool() {
        return idPool;
    }

    public void setIdPool(String idPool) {
        this.idPool = idPool;
    }

    public String getIdRelation() {
        return idRelation;
    }

    public void setIdRelation(String idRelation) {
        this.idRelation = idRelation;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
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
}