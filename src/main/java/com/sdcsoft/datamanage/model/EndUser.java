package com.sdcsoft.datamanage.model;

import java.io.Serializable;

/**
 * 终端用户数据结构
 */
public class EndUser implements Serializable {
    private Integer id, status;
    private String endUserName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getEndUserName() {
        return endUserName;
    }

    public void setEndUserName(String endUserName) {
        this.endUserName = endUserName;
    }

}
