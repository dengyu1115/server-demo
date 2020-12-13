package com.elt.server.model;

import java.util.Date;

/**
 * model 基类
 * @author nature
 * @version 1.0.0
 * @since 2019/8/6 12:41
 */
public class BaseDto extends BaseModel {

    private Date createdDate;

    private String createdBy;

    private Date updatedDate;

    private String updatedBy;

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

}
