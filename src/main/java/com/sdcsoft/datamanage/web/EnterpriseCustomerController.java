package com.sdcsoft.datamanage.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sdcsoft.datamanage.client.TemplateClient;
import com.sdcsoft.datamanage.model.EnterpriseCode;
import com.sdcsoft.datamanage.model.EnterpriseCustomer;
import com.sdcsoft.datamanage.utils.Result;
import com.sdcsoft.datamanage.utils.ResultGenerator;
import feign.Feign;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/enterprisecustomer")
public class EnterpriseCustomerController {

    private static String dataUrlUrl;
    @Value("${com.sdcsoft.datamanage.dataurl}")
    public void setDataUrl(String dataUrlUrl) {
        this.dataUrlUrl = dataUrlUrl;
    }
    /**
     * 查询客户列表-分页

     * @return
     */
    @GetMapping(value = "/enterprisecustomerlist")
    public Result enterpriseCustomerlist(EnterpriseCustomer enterpriseCustomer) {
        TemplateClient dataClient = Feign.builder().target(TemplateClient.class, String.format("%s%s",dataUrlUrl,"/enterprise/customer/list"));
        Map<String,String> map=new HashMap<>();
        map.put("enterpriseId",enterpriseCustomer.getEnterpriseId().toString());
        JSONObject jobj = JSON.parseObject(dataClient.get(map));
        JSONArray ja = jobj.getJSONArray("data");
        return ResultGenerator.genSuccessResult(ja);
    }

    /**
     * 编辑客户
     * @param enterpriseCustomer
     * @return
     */
    @PostMapping("/editenterprisecustomer")
    public Result editenterpriseCustomer(@RequestBody EnterpriseCustomer enterpriseCustomer){
        if(enterpriseCustomer.getId()!=null){
            TemplateClient dataClient = Feign.builder().target(TemplateClient.class, String.format("%s%s",dataUrlUrl,"/enterprise/customer/modify"));
            Map<String,String> map=new HashMap<>();
            map.put("id",enterpriseCustomer.getId().toString());
            map.put("customerName",enterpriseCustomer.getCustomerName());
            JSONObject jobj = JSON.parseObject(dataClient.post(map));
            return ResultGenerator.genSuccessResult(jobj.get("msg")+"");
        }else{
            TemplateClient dataClient = Feign.builder().target(TemplateClient.class, String.format("%s%s",dataUrlUrl,"/enterprise/customer/create"));
            Map<String,String> map=new HashMap<>();
            map.put("enterpriseId",enterpriseCustomer.getEnterpriseId().toString());
            map.put("customerName",enterpriseCustomer.getCustomerName());
            map.put("status",enterpriseCustomer.getEnterpriseId().toString());
            JSONObject jobj = JSON.parseObject(dataClient.post(map));
            return ResultGenerator.genSuccessResult(jobj.get("msg")+"");
        }
    }
}
