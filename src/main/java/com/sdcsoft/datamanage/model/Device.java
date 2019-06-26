package com.sdcsoft.datamanage.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sdcsoft.datamanage.utils.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

public class Device implements Serializable {


    public static final int STATUS_SELL = 1;
    public static final int STATUS_READY = 0;
    private Integer id;
    private Integer enterpriseId;
    private Integer customerId;
    private Integer agentId;
    private Integer endUserId;
    private Integer status;
    private Integer runStatus;
    private Integer power;
    private Integer media;
    private String deviceNo;
    private String devicePrefix;
    private String deviceSuffix;
    private String deviceType;
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date importDatetime;




    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Integer enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    public Integer getEndUserId() {
        return endUserId;
    }

    public void setEndUserId(Integer endUserId) {
        this.endUserId = endUserId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getRunStatus() {
        return runStatus;
    }

    public void setRunStatus(Integer runStatus) {
        this.runStatus = runStatus;
    }

    public Integer getPower() {
        return power;
    }

    public void setPower(Integer power) {
        this.power = power;
    }

    public Integer getMedia() {
        return media;
    }

    public void setMedia(Integer media) {
        this.media = media;
    }

    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }

    public String getDevicePrefix() {
        return devicePrefix;
    }

    public void setDevicePrefix(String devicePrefix) {
        this.devicePrefix = devicePrefix;
    }

    public String getDeviceSuffix() {
        return deviceSuffix;
    }

    public void setDeviceSuffix(String deviceSuffix) {
        this.deviceSuffix = deviceSuffix;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }






    public Date getImportDatetime() {
        return importDatetime;
    }

    public void setImportDatetime(Date importDatetime) {
        this.importDatetime = importDatetime;
    }

}
