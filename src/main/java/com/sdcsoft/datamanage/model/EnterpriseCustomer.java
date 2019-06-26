package com.sdcsoft.datamanage.model;

import java.io.Serializable;

/**
 * 企业客户数据结构
 */
public class EnterpriseCustomer implements Serializable {
    private Integer id, enterpriseId;
    private String customerName;

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Integer getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(int enterpriseId) {
        this.enterpriseId = enterpriseId;
    }
}
