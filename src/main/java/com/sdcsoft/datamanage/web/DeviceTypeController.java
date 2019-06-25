package com.sdcsoft.datamanage.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sdcsoft.datamanage.client.TemplateClient;
import com.sdcsoft.datamanage.mapper.DeviceTypeMapper;
import com.sdcsoft.datamanage.mapper.TokenDictMapper;
import com.sdcsoft.datamanage.model.DeviceType;
import com.sdcsoft.datamanage.model.Employee;
import com.sdcsoft.datamanage.utils.Result;
import com.sdcsoft.datamanage.utils.ResultGenerator;
import feign.Feign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/devicetype")
public class DeviceTypeController {

    private static String dataUrlUrl;
    @Value("${com.sdcsoft.datamanage.dataurl}")
    public void setDataUrl(String dataUrlUrl) {
        this.dataUrlUrl = dataUrlUrl;
    }



    /**
     * 查询设备类型列表-分页
     * @param deviceType
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/devicetypelistbyconditionandpage")
    public Result getDeviceTypeListByConditionAndPage(DeviceType deviceType, int pageNum, int pageSize) {
        TemplateClient dataClient = Feign.builder().target(TemplateClient.class, String.format("%s%s",dataUrlUrl,"/device/type/list"));
        JSONObject jobj = JSON.parseObject(dataClient.get());
        JSONArray ja = jobj.getJSONArray("data");
        return ResultGenerator.genSuccessResult(ja);
    }

    /**
     * 编辑用户
     * @param deviceType
     * @return
     */
    @PostMapping("/editdevicetype")
    public Result editdeviceType(@RequestBody DeviceType deviceType){
        if(deviceType.getId()!=null){
            TemplateClient dataClient = Feign.builder().target(TemplateClient.class, String.format("%s%s",dataUrlUrl,"/device/type/modify"));
            Map<String,String> map=new HashMap<>();
            map.put("id",deviceType.getId().toString());
            map.put("typeName",deviceType.getDeviceType());
            JSONObject jobj = JSON.parseObject(dataClient.post(map));
            return ResultGenerator.genSuccessResult(jobj.get("msg")+"");
        }else{
            TemplateClient dataClient = Feign.builder().target(TemplateClient.class, String.format("%s%s",dataUrlUrl,"/device/type/create"));
            Map<String,String> map=new HashMap<>();
            map.put("typeName",deviceType.getDeviceType());
            JSONObject jobj = JSON.parseObject(dataClient.post(map));
            System.out.println(jobj);
            return ResultGenerator.genSuccessResult(jobj.get("msg")+"");
        }
    }
//    /**
//     * 删除用户
//     * @param id
//     * @return
//     */
//    @PostMapping(value = "/deletedevicetypebyid")
//    public Result deletedeviceTypebyid(@RequestParam int id){
//        deviceTypeMapper.deleteDeviceTypeById(id);
//        return ResultGenerator.genSuccessResult();
//    }
}
