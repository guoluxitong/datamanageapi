package com.sdcsoft.datamanage.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sdcsoft.datamanage.client.TemplateClient;
import com.sdcsoft.datamanage.model.Customer;
import com.sdcsoft.datamanage.model.OrgType;
import com.sdcsoft.datamanage.utils.Result;
import com.sdcsoft.datamanage.utils.ResultGenerator;
import feign.Feign;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/org")
public class OrgController {


    private static String dataUrlUrl;
    @Value("${com.sdcsoft.datamanage.dataurl}")
    public void setDataUrl(String dataUrlUrl) {
        this.dataUrlUrl = dataUrlUrl;
    }
    /**
     * 查询客户列表-分页
     * @param org
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/orglist")
    public Result orglist(OrgType org, int pageNum, int pageSize) {
        TemplateClient dataClient = Feign.builder().target(TemplateClient.class, String.format("%s%s",dataUrlUrl,"/org/list"));
        JSONObject jobj = JSON.parseObject(dataClient.get());
        JSONArray ja = jobj.getJSONArray("data");
        return ResultGenerator.genSuccessResult(ja);
    }

    /**
     * 编辑客户
     * @param org
     * @return
     */
    @PostMapping("/editorg")
    public Result editorg(@RequestBody OrgType org){
        if(org.getId()!=null){
            TemplateClient dataClient = Feign.builder().target(TemplateClient.class, String.format("%s%s",dataUrlUrl,"/org/modify"));
            Map<String,String> map=new HashMap<>();
            map.put("id",org.getId().toString());
            map.put("orgType",org.getOrgType().toString());
            map.put("orgTypeName",org.getOrgTypeName());
            JSONObject jobj = JSON.parseObject(dataClient.post(map));
            return ResultGenerator.genSuccessResult(jobj.get("msg")+"");
        }else{
            TemplateClient dataClient = Feign.builder().target(TemplateClient.class, String.format("%s%s",dataUrlUrl,"/org/create"));
            Map<String,String> map=new HashMap<>();
            map.put("orgType",org.getOrgType().toString());
            map.put("orgTypeName",org.getOrgTypeName());
            JSONObject jobj = JSON.parseObject(dataClient.post(map));
            System.out.println(jobj);
            return ResultGenerator.genSuccessResult(jobj.get("msg")+"");
        }
    }

}
