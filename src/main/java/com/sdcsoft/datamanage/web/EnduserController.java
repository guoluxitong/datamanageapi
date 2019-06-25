package com.sdcsoft.datamanage.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sdcsoft.datamanage.client.TemplateClient;
import com.sdcsoft.datamanage.model.Customer;
import com.sdcsoft.datamanage.model.EndUser;
import com.sdcsoft.datamanage.utils.Result;
import com.sdcsoft.datamanage.utils.ResultGenerator;
import feign.Feign;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/enduser")
public class EnduserController {


    private static String dataUrlUrl;
    @Value("${com.sdcsoft.datamanage.dataurl}")
    public void setDataUrl(String dataUrlUrl) {
        this.dataUrlUrl = dataUrlUrl;
    }
    /**
     * 查询客户列表-分页
     * @param enduser
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/enduserlist")
    public Result enduserlist(EndUser enduser, int pageNum, int pageSize) {
        TemplateClient dataClient = Feign.builder().target(TemplateClient.class, String.format("%s%s",dataUrlUrl,"/enduser/list"));
        JSONObject jobj = JSON.parseObject(dataClient.get());
        JSONArray ja = jobj.getJSONArray("data");
        return ResultGenerator.genSuccessResult(ja);
    }

    /**
     * 编辑客户
     * @param enduser
     * @return
     */
    @PostMapping("/editenduser")
    public Result editenduser(@RequestBody EndUser enduser){
        if(enduser.getId()!=null){
            TemplateClient dataClient = Feign.builder().target(TemplateClient.class, String.format("%s%s",dataUrlUrl,"/enduser/modify"));
            Map<String,String> map=new HashMap<>();
            map.put("id",enduser.getId().toString());
            map.put("status",enduser.getStatus().toString());
            map.put("endUserName",enduser.getEndUserName());
            JSONObject jobj = JSON.parseObject(dataClient.post(map));
            return ResultGenerator.genSuccessResult(jobj.get("msg")+"");
        }else{
            TemplateClient dataClient = Feign.builder().target(TemplateClient.class, String.format("%s%s",dataUrlUrl,"/enduser/create"));
            Map<String,String> map=new HashMap<>();
            map.put("status",enduser.getStatus().toString());
            map.put("endUserName",enduser.getEndUserName());
            JSONObject jobj = JSON.parseObject(dataClient.post(map));
            System.out.println(jobj);
            return ResultGenerator.genSuccessResult(jobj.get("msg")+"");
        }
    }
}
