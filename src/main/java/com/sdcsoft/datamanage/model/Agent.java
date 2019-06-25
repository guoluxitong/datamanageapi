package com.sdcsoft.datamanage.model;

import java.io.Serializable;

/**
 * 代理商数据结构
 */
public class Agent implements Serializable {
    private Integer id, status;

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    private String agentName;


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


}
