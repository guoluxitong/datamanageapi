package com.sdcsoft.datamanage.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sdcsoft.datamanage.client.TemplateClient;
import com.sdcsoft.datamanage.mapper.EmployeeMapper;
import com.sdcsoft.datamanage.model.Employee;
import com.sdcsoft.datamanage.service.EmployeeService;
import com.sdcsoft.datamanage.utils.Result;
import com.sdcsoft.datamanage.utils.ResultGenerator;
import feign.Feign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {


    private static String dataUrlUrl;
    @Value("${com.sdcsoft.datamanage.dataurl}")
    public void setDataUrl(String dataUrlUrl) {
        this.dataUrlUrl = dataUrlUrl;
    }

    @PostMapping(value = "/login")
    public Result login(@RequestParam(name = "account") String account, @RequestParam(name = "passWord") String passWord) {
        TemplateClient dataClient = Feign.builder().target(TemplateClient.class, String.format("%s%s",dataUrlUrl,"/employee/find"));
        Map<String,String> map=new HashMap<>();
        map.put("loginId",account);
        JSONObject jobj = JSON.parseObject(dataClient.get(map));
        JSONObject employee=jobj.getJSONObject("data");

        if (null == employee)
            return ResultGenerator.genFailResult(0,"用户名或者密码输入错误");
        if (employee.getString("password").equals(passWord)) {
            if (Employee.STATUS_ENABLE == employee.getIntValue("status")) {
                return ResultGenerator.genSuccessResult(1,"success",employee.getIntValue("id"));
            } else {
                return ResultGenerator.genFailResult(0,"您的用户账号被禁用，请联系系统管理人员！");
            }
        } else {
            return ResultGenerator.genFailResult(0,"用户名或者密码输入错误");
        }
    }

}
