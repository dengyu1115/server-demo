package com.elt.server.res;

import com.elt.server.model.BaseModel;

public class PoolRes extends BaseModel {

    private String idRelation;

    private String timeStart;

    private String timeEnd;

    public String getIdRelation() {
        return idRelation;
    }

    public void setIdRelation(String idRelation) {
        this.idRelation = idRelation;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }
}
