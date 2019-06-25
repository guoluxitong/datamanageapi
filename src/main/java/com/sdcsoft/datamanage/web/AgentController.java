package com.sdcsoft.datamanage.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sdcsoft.datamanage.client.TemplateClient;
import com.sdcsoft.datamanage.model.Agent;
import com.sdcsoft.datamanage.model.Customer;
import com.sdcsoft.datamanage.utils.Result;
import com.sdcsoft.datamanage.utils.ResultGenerator;
import feign.Feign;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import sun.management.resources.agent;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/agent")
public class AgentController{

    private static String dataUrlUrl;
    @Value("${com.sdcsoft.datamanage.dataurl}")
    public void setDataUrl(String dataUrlUrl) {
        this.dataUrlUrl = dataUrlUrl;
    }
    /**
     * 查询客户列表-分页
     * @param agent
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/agentlist")
    public Result agentlist(Agent agent, int pageNum, int pageSize) {
        TemplateClient dataClient = Feign.builder().target(TemplateClient.class, String.format("%s%s",dataUrlUrl,"/agent/list"));
        JSONObject jobj = JSON.parseObject(dataClient.get());
        JSONArray ja = jobj.getJSONArray("data");
        return ResultGenerator.genSuccessResult(ja);
    }

    /**
     * 编辑客户
     * @param agent
     * @return
     */
    @PostMapping("/editagent")
    public Result editagent(@RequestBody Agent agent){
        if(agent.getId()!=null){
            TemplateClient dataClient = Feign.builder().target(TemplateClient.class, String.format("%s%s",dataUrlUrl,"/customer/modify"));
            Map<String,String> map=new HashMap<>();
            map.put("id",agent.getId().toString());
            map.put("status",agent.getStatus().toString());
            map.put("agentName",agent.getAgentName());
            JSONObject jobj = JSON.parseObject(dataClient.post(map));
            return ResultGenerator.genSuccessResult(jobj.get("msg")+"");
        }else{
            TemplateClient dataClient = Feign.builder().target(TemplateClient.class, String.format("%s%s",dataUrlUrl,"/customer/create"));
            Map<String,String> map=new HashMap<>();
            map.put("status",agent.getStatus().toString());
            map.put("agentName",agent.getAgentName());
            JSONObject jobj = JSON.parseObject(dataClient.post(map));
            return ResultGenerator.genSuccessResult(jobj.get("msg")+"");
        }
    }

}
