package com.sdcsoft.datamanage.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sdcsoft.datamanage.client.TemplateClient;
import com.sdcsoft.datamanage.model.Agent;
import com.sdcsoft.datamanage.model.EnterpriseCode;
import com.sdcsoft.datamanage.utils.Result;
import com.sdcsoft.datamanage.utils.ResultGenerator;
import feign.Feign;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import sun.management.resources.agent;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/enterprisecode")
public class EnterpriseCodeController {

    private static String dataUrlUrl;
    @Value("${com.sdcsoft.datamanage.dataurl}")
    public void setDataUrl(String dataUrlUrl) {
        this.dataUrlUrl = dataUrlUrl;
    }
    /**
     * 查询客户列表-分页
     * @param enterpriseCode
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/enterprisecodelist")
    public Result enterpriseCodelist(EnterpriseCode enterpriseCode, int pageNum, int pageSize) {
        TemplateClient dataClient = Feign.builder().target(TemplateClient.class, String.format("%s%s",dataUrlUrl,"/enterprise/prefix/list"));
        JSONObject jobj = JSON.parseObject(dataClient.get());
        JSONArray ja = jobj.getJSONArray("data");
        return ResultGenerator.genSuccessResult(ja);
    }

    /**
     * 编辑客户
     * @param enterpriseCode
     * @return
     */
    @PostMapping("/editenterprisecode")
    public Result editenterpriseCode(@RequestBody EnterpriseCode enterpriseCode){
        if(enterpriseCode.getId()!=null){
            TemplateClient dataClient = Feign.builder().target(TemplateClient.class, String.format("%s%s",dataUrlUrl,"/enterprise/prefix/modify"));
            Map<String,String> map=new HashMap<>();
            map.put("enterpriseId",enterpriseCode.getId().toString());
            map.put("status",enterpriseCode.getStatus().toString());
            JSONObject jobj = JSON.parseObject(dataClient.post(map));
            return ResultGenerator.genSuccessResult(jobj.get("msg")+"");
        }else{
            TemplateClient dataClient = Feign.builder().target(TemplateClient.class, String.format("%s%s",dataUrlUrl,"/enterprise/prefix/create"));
            Map<String,String> map=new HashMap<>();
            map.put("enterpriseId",enterpriseCode.getEnterpriseId().toString());
            map.put("prefix",enterpriseCode.getCodePrefix());
            JSONObject jobj = JSON.parseObject(dataClient.post(map));
            return ResultGenerator.genSuccessResult(jobj.get("msg")+"");
        }
    }
}
