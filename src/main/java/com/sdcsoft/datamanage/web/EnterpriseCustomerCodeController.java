package com.sdcsoft.datamanage.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sdcsoft.datamanage.client.TemplateClient;
import com.sdcsoft.datamanage.model.EnterpriseCode;
import com.sdcsoft.datamanage.model.EnterpriseCustomerCode;
import com.sdcsoft.datamanage.utils.Result;
import com.sdcsoft.datamanage.utils.ResultGenerator;
import feign.Feign;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/enterprisecustomercode")
public class EnterpriseCustomerCodeController {

    private static String dataUrlUrl;
    @Value("${com.sdcsoft.datamanage.dataurl}")
    public void setDataUrl(String dataUrlUrl) {
        this.dataUrlUrl = dataUrlUrl;
    }
    /**
     * 查询客户列表-分页
     * @param enterpriseCustomerCode
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/enterprisecustomercodelist")
    public Result enterprisecustomercodelist(EnterpriseCustomerCode enterpriseCustomerCode, int pageNum, int pageSize) {
        TemplateClient dataClient = Feign.builder().target(TemplateClient.class, String.format("%s%s",dataUrlUrl,"/enterprise/customer/prefix/list"));
        JSONObject jobj = JSON.parseObject(dataClient.get());
        JSONArray ja = jobj.getJSONArray("data");
        return ResultGenerator.genSuccessResult(ja);
    }

    /**
     * 编辑客户
     * @param enterpriseCustomerCode
     * @return
     */
    @PostMapping("/editenterprisecustomercode")
    public Result editenterprisecustomercode(@RequestBody EnterpriseCustomerCode enterpriseCustomerCode){
        if(enterpriseCustomerCode.getId()!=null){
            TemplateClient dataClient = Feign.builder().target(TemplateClient.class, String.format("%s%s",dataUrlUrl,"/enterprise/prefix/create"));
            Map<String,String> map=new HashMap<>();
            map.put("enterpriseCustomerId",enterpriseCustomerCode.getId().toString());
            map.put("code",enterpriseCustomerCode.getCode());
            JSONObject jobj = JSON.parseObject(dataClient.post(map));
            return ResultGenerator.genSuccessResult(jobj.get("msg")+"");
        }
        return null;
    }
}
