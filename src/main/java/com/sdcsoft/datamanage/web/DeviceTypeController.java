package com.sdcsoft.datamanage.web;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sdcsoft.datamanage.mapper.DeviceTypeMapper;
import com.sdcsoft.datamanage.mapper.TokenDictMapper;
import com.sdcsoft.datamanage.model.DeviceType;
import com.sdcsoft.datamanage.model.Employee;
import com.sdcsoft.datamanage.utils.Result;
import com.sdcsoft.datamanage.utils.ResultGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/devicetype")
public class DeviceTypeController {
    
    @Autowired
    private DeviceTypeMapper deviceTypeMapper;

    /**
     * 获得设备类型列表
     * @return
     */
    @GetMapping(value = "/getdevicetypelist")
    public Result getDeviceTypeList() {
        return ResultGenerator.genSuccessResult(deviceTypeMapper.getDeviceTypeList());
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
        PageHelper.startPage(pageNum, pageSize);
        return ResultGenerator.genSuccessResult(new PageInfo(deviceTypeMapper.getDeviceTypeListByCondition(deviceType)));
    }

    /**
     * 编辑用户
     * @param deviceType
     * @return
     */
    @PostMapping("/editdevicetype")
    public Result editdeviceType(@RequestBody DeviceType deviceType){
        if(deviceType.getId()!=null){
            deviceTypeMapper.updateDeviceType(deviceType);
            return ResultGenerator.genSuccessResult("修改成功");
        }else{
            deviceTypeMapper.insertDeviceType(deviceType);
            return ResultGenerator.genSuccessResult("添加成功");
        }
    }
    /**
     * 删除用户
     * @param id
     * @return
     */
    @PostMapping(value = "/deletedevicetypebyid")
    public Result deletedeviceTypebyid(@RequestParam int id){
        deviceTypeMapper.deleteDeviceTypeById(id);
        return ResultGenerator.genSuccessResult();
    }
}
