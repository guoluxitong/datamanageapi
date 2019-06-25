package com.sdcsoft.datamanage.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sdcsoft.datamanage.client.TemplateClient;
import com.sdcsoft.datamanage.mapper.EmployeeMapper;
import com.sdcsoft.datamanage.mapper.EmployeeRoleMapper;
import com.sdcsoft.datamanage.model.Employee;
import com.sdcsoft.datamanage.service.EmployeeService;
import com.sdcsoft.datamanage.utils.Result;
import com.sdcsoft.datamanage.utils.ResultGenerator;
import feign.Feign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/employee")
public class EmployeeController {

    private static String dataUrlUrl;
    @Value("${com.sdcsoft.datamanage.dataurl}")
    public void setDataUrl(String dataUrlUrl) {
        this.dataUrlUrl = dataUrlUrl;
    }
//    @PostMapping(value = "/getemployeeinfo")
//    public Result getEmployeeInfo(@RequestParam(name = "id") String id) {
//        TemplateClient dataClient = Feign.builder().target(TemplateClient.class, String.format("%s%s",dataUrlUrl,"/employee/find"));
//        Map<String,String> map=new HashMap<>();
//        map.put("loginId",id.toString());
//        JSONObject jobj = JSON.parseObject(dataClient.get(map));
//        System.out.println(jobj);
//        return ResultGenerator.genSuccessResult(jobj.get("data"));
//    }
//    @PostMapping(value = "/getemployeeinfo")
//    public Result getEmployeeInfo(@RequestParam(name = "id") Integer id) {
//        return ResultGenerator.genSuccessResult(employeeService.findOneByLoginId(id));
//    }
    /**
     * 查询用户列表-分页
     * @param employee
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/employeelistbyconditionandpage")
    public Result getEmployeeListByConditionAndPage(Employee employee, int pageNum, int pageSize) {
        TemplateClient dataClient = Feign.builder().target(TemplateClient.class, String.format("%s%s",dataUrlUrl,"/employee/list"));

        JSONObject jobj = JSON.parseObject(dataClient.get());
        JSONArray ja = jobj.getJSONArray("data");
        return ResultGenerator.genSuccessResult(new PageInfo(ja));
    }

    /**
     * 编辑用户
     * @param employee
     * @return
     */
    @PostMapping("/editemployee")
    public Result editEmployee(@RequestBody Employee employee){
        if(employee.getId()!=null){
            TemplateClient dataClient = Feign.builder().target(TemplateClient.class, String.format("%s%s",dataUrlUrl,"/employee/modify"));
            Map<String,String> map=new HashMap<>();
            map.put("id",employee.getId().toString());
            map.put("password",employee.getPassword());
            map.put("email",employee.getEmail());
            map.put("mark",employee.getMark());
            map.put("mobile",employee.getMobile());
            map.put("qQ",employee.getqQ());
            map.put("realName",employee.getRealName());
            map.put("weiXin",employee.getWeiXin());
            map.put("orgId",employee.getOrgId().toString());
            map.put("orgType",employee.getOrgType().toString());
            map.put("lastLoginDatetime",employee.getLastLoginDatetime().toString());
            JSONObject jobj = JSON.parseObject(dataClient.post(map));
            return ResultGenerator.genSuccessResult(jobj.get("msg")+"");
        }else{
            TemplateClient dataClient = Feign.builder().target(TemplateClient.class, String.format("%s%s",dataUrlUrl,"/employee/create"));
            Map<String,String> map=new HashMap<>();
            map.put("password",employee.getPassword());
            map.put("email",employee.getEmail());
            map.put("mark",employee.getMark());
            map.put("mobile",employee.getMobile());
            map.put("qQ",employee.getqQ());
            map.put("realName",employee.getRealName());
            map.put("weiXin",employee.getWeiXin());
            map.put("orgId",employee.getOrgId().toString());
            map.put("orgType",employee.getOrgType().toString());
            JSONObject jobj = JSON.parseObject(dataClient.post(map));
            return ResultGenerator.genSuccessResult(jobj.get("msg")+"");
        }
    }

//    /**
//     * 编辑用户密码
//     * @param employee
//     * @return
//     */
//    @PostMapping("/editemployeepass")
//    public Result editUserPass(@RequestBody Employee employee){
//        employeeMapper.updateEmployeePass(employee);
//        return ResultGenerator.genSuccessResult();
//    }
}
