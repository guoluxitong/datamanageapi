package com.sdcsoft.datamanage.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sdcsoft.datamanage.client.TemplateClient;
import com.sdcsoft.datamanage.mapper.EnterpriseMapper;
import com.sdcsoft.datamanage.model.Enterprise;
import com.sdcsoft.datamanage.utils.Result;
import com.sdcsoft.datamanage.utils.ResultGenerator;
import feign.Feign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/enterprise")
public class EnterpriseController {
    private static String dataUrlUrl;
    @Value("${com.sdcsoft.datamanage.dataurl}")
    public void setDataUrl(String dataUrlUrl) {
        this.dataUrlUrl = dataUrlUrl;
    }

    /**
     * 查询字典列表-分页
     * @param enterprise
     * @param
     * @param
     * @return
     */
    @GetMapping(value = "/enterpriselistbyconditionandpage")
    public Result getEnterpriseListByConditionAndPage(Enterprise enterprise) {
        TemplateClient dataClient = Feign.builder().target(TemplateClient.class, String.format("%s%s",dataUrlUrl,"/enterprise/list"));
        JSONObject jobj = JSON.parseObject(dataClient.get());
        JSONArray ja = jobj.getJSONArray("data");
        return ResultGenerator.genSuccessResult(ja);
    }


    /**
     * 编辑字典
     * @param enterprise
     * @return
     */
    @PostMapping("/editenterprise")
    public Result editEnterprise(@RequestBody Enterprise enterprise){
        if(enterprise.getId()!=null){
            TemplateClient dataClient = Feign.builder().target(TemplateClient.class, String.format("%s%s",dataUrlUrl,"/enterprise/modify"));
            Map<String,String> map=new HashMap<>();
            map.put("id",enterprise.getId().toString());
            map.put("status",enterprise.getStatus().toString());
            map.put("enterpriseName",enterprise.getEnterpriseName());
            JSONObject jobj = JSON.parseObject(dataClient.post(map));
            return ResultGenerator.genSuccessResult(jobj.get("msg")+"");
        }else{
            TemplateClient dataClient = Feign.builder().target(TemplateClient.class, String.format("%s%s",dataUrlUrl,"/enterprise/create"));
            Map<String,String> map=new HashMap<>();
            map.put("status",enterprise.getStatus().toString());
            map.put("enterpriseName",enterprise.getEnterpriseName());
            JSONObject jobj = JSON.parseObject(dataClient.post(map));
            System.out.println(jobj);
            return ResultGenerator.genSuccessResult(jobj.get("msg")+"");
        }

    }

//    /**
//     * 删除字典
//     * @param id
//     * @return
//     */
//    @PostMapping(value = "/deleteenterprisebyid")
//    public Result deleteEnterpriseById(@RequestParam int id){
//        enterpriseMapper.deleteEnterpriseById(id);
//        return ResultGenerator.genSuccessResult();
//    }
}
