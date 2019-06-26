package com.sdcsoft.datamanage.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sdcsoft.datamanage.client.TemplateClient;
import com.sdcsoft.datamanage.mapper.CustomerMapper;
import com.sdcsoft.datamanage.mapper.TokenDictMapper;
import com.sdcsoft.datamanage.model.Customer;
import com.sdcsoft.datamanage.model.TokenDict;
import com.sdcsoft.datamanage.utils.Result;
import com.sdcsoft.datamanage.utils.ResultGenerator;
import feign.Feign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(value = "/customer")
public class CustomerController {


    private static String dataUrlUrl;
    @Value("${com.sdcsoft.datamanage.dataurl}")
    public void setDataUrl(String dataUrlUrl) {
        this.dataUrlUrl = dataUrlUrl;
    }
    /**
     * 查询客户列表-分页
     * @param customer
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/customerlistbyconditionandpage")
    public Result getCustomerListByConditionAndPage(Customer customer) {
        TemplateClient dataClient = Feign.builder().target(TemplateClient.class, String.format("%s%s",dataUrlUrl,"/customer/list"));
        JSONObject jobj = JSON.parseObject(dataClient.get());
        JSONArray ja = jobj.getJSONArray("data");
        return ResultGenerator.genSuccessResult(ja);
    }

    /**
     * 编辑客户
     * @param customer
     * @return
     */
    @PostMapping("/editcustomer")
    public Result editCustomer(@RequestBody Customer customer){
        if(customer.getId()!=null){
            TemplateClient dataClient = Feign.builder().target(TemplateClient.class, String.format("%s%s",dataUrlUrl,"/customer/modify"));
            Map<String,String> map=new HashMap<>();
            map.put("id",customer.getId().toString());
            map.put("status",customer.getStatus().toString());
            map.put("customerName",customer.getCustomerName());
            JSONObject jobj = JSON.parseObject(dataClient.post(map));
            return ResultGenerator.genSuccessResult(jobj.get("msg")+"");
        }else{
            TemplateClient dataClient = Feign.builder().target(TemplateClient.class, String.format("%s%s",dataUrlUrl,"/customer/create"));
            Map<String,String> map=new HashMap<>();
            map.put("status",customer.getStatus().toString());
            map.put("customerName",customer.getCustomerName());
            JSONObject jobj = JSON.parseObject(dataClient.post(map));
            System.out.println(jobj);
            return ResultGenerator.genSuccessResult(jobj.get("msg")+"");
        }
    }

}
