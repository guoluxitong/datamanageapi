package com.sdcsoft.datamanage.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.sdcsoft.datamanage.client.TemplateClient;
import com.sdcsoft.datamanage.mapper.DeviceMapper;
import com.sdcsoft.datamanage.model.Device;
import com.sdcsoft.datamanage.service.DeviceService;
import com.sdcsoft.datamanage.utils.Result;
import com.sdcsoft.datamanage.utils.ResultGenerator;
import feign.Feign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.List;

@RestController
@RequestMapping(value = "/device")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;
    
    @Autowired
    private DeviceMapper deviceMapper;

    private static String dataUrlUrl;
    @Value("${com.sdcsoft.datamanage.dataurl}")
    public void setDataUrl(String dataUrlUrl) {
        this.dataUrlUrl = dataUrlUrl;
    }


    @GetMapping(value = "/devicelist")
    public Result devicelist(Device device) {
        TemplateClient dataClient = Feign.builder().target(TemplateClient.class, String.format("%s%s",dataUrlUrl,"/device/list"));
        JSONObject jobj = JSON.parseObject(dataClient.get());
        JSONArray ja = jobj.getJSONArray("data");
        return ResultGenerator.genSuccessResult(ja);
    }

    @GetMapping(value = "/devicelistbyenterpriseId")
    public Result devicelistbyenterpriseId(Device device) {
        TemplateClient dataClient = Feign.builder().target(TemplateClient.class, String.format("%s%s",dataUrlUrl,"/find/enterprise"));
        Map<String,String> map=new HashMap<>();
        map.put("enterpriseId",device.getEnterpriseId().toString());
        JSONObject jobj = JSON.parseObject(dataClient.get());
        JSONArray ja = jobj.getJSONArray("data");
        return ResultGenerator.genSuccessResult(ja);
    }

    @GetMapping(value = "/devicelistbycustomerId")
    public Result devicelistbycustomerId(Device device) {
        TemplateClient dataClient = Feign.builder().target(TemplateClient.class, String.format("%s%s",dataUrlUrl,"/find/customer"));
        Map<String,String> map=new HashMap<>();
        map.put("customerId",device.getCustomerId().toString());
        JSONObject jobj = JSON.parseObject(dataClient.get());
        JSONArray ja = jobj.getJSONArray("data");
        return ResultGenerator.genSuccessResult(ja);
    }

    /**
     * 根据deviceNo获取可用设备，设备状态非可用时，deviceNo有效也无法拿到数据
     * 专为手机APP/微信小程序提供的添加设备用的查询接口
     * @param  deviceNo 加密或非加密的设备编号
     * @return
     */
    @GetMapping(value = "/get/deviceno")
    public Result devicebydeviceno(String deviceNo) {
        TemplateClient dataClient = Feign.builder().target(TemplateClient.class, String.format("%s%s",dataUrlUrl,"/get/deviceno"));
        Map<String,String> map=new HashMap<>();
        map.put("deviceNo",deviceNo);
        JSONObject jobj = JSON.parseObject(dataClient.get());
        JSONArray ja = jobj.getJSONArray("data");
        return ResultGenerator.genSuccessResult(ja);

    }
    /**
     * 根据suffix获取可用设备，设备状态非可用时，suffix有效也无法拿到数据
     * 专为手机APP/微信小程序提供的添加设备用的查询接口
     * @param suffix 未加密的设备编号
     * @return
     */
    @GetMapping(value = "/get/suffix")
    public Result devicebysuffix(String suffix) {
        TemplateClient dataClient = Feign.builder().target(TemplateClient.class, String.format("%s%s",dataUrlUrl,"/get/suffix"));
        Map<String,String> map=new HashMap<>();
        map.put("suffix",suffix);
        JSONObject jobj = JSON.parseObject(dataClient.get());
        JSONArray ja = jobj.getJSONArray("data");
        return ResultGenerator.genSuccessResult(ja);
    }
    /**
     * 根据suffix获取设备信息，设备状态非可用也可获取到数据
     * 专为微信小程序提供的企业内部员工查询设备的接口
     * @param suffix
     * @return
     */
    @GetMapping(value = "/fix/suffix")
    public Result findBySuffixForEuser(String suffix) {
        TemplateClient dataClient = Feign.builder().target(TemplateClient.class, String.format("%s%s",dataUrlUrl,"/fix/suffix"));
        Map<String,String> map=new HashMap<>();
        map.put("suffix",suffix);
        JSONObject jobj = JSON.parseObject(dataClient.get());
        JSONArray ja = jobj.getJSONArray("data");
        return ResultGenerator.genSuccessResult(ja);
    }
    /**
     * 根据suffix修改设备信息
     * 专为微信小程序提供的企业内部员工修改设备的接口
     * @param suffix
     * @param prefix 设备类型 1为控制器 2为PLC
     * @param deviceType 设备类型信息
     * @param saleStatus 销售状态 0 未销售 1 已销售
     * @return
     */
    @PostMapping(value = "/fix/modify")
    public Result modifyDevice(String suffix, Integer prefix, String deviceType, Integer saleStatus) {
        TemplateClient dataClient = Feign.builder().target(TemplateClient.class, String.format("%s%s",dataUrlUrl,"/fix/modify"));
        Map<String,String> map=new HashMap<>();
        map.put("suffix",suffix);
        map.put("prefix",prefix.toString());
        map.put("saleStatus",saleStatus.toString());
        map.put("deviceType",deviceType);
        JSONObject jobj = JSON.parseObject(dataClient.post(map));
        return ResultGenerator.genSuccessResult(jobj.get("msg")+"");
    }
    /**
     * 批量创建设备
     * @param deviceList  要创建的设备列表
     * @return
     */
    @PostMapping("/create")
    public Result insertManyDevice(@RequestBody List<Device> deviceList) {
        TemplateClient dataClient = Feign.builder().target(TemplateClient.class, String.format("%s%s",dataUrlUrl,"/create"));
        Map<String,String> map=new HashMap<>();
        map.put("deviceList",deviceList.toString());
        JSONObject jobj = JSON.parseObject(dataClient.post(map));
        return ResultGenerator.genSuccessResult(jobj.get("msg")+"");
    }
    /**
     * 根据suffix修改设备类型信息
     * @param deviceType 设备类型信息
     * @param subType 具体类型信息
     * @return
     */
    @PostMapping(value = "/modify/type")
    public Result modifyDeviceType(String suffix, String deviceType, String subType) {
        TemplateClient dataClient = Feign.builder().target(TemplateClient.class, String.format("%s%s",dataUrlUrl,"/modify/type"));
        Map<String,String> map=new HashMap<>();
        map.put("suffix",suffix);
        map.put("deviceType",deviceType);
        map.put("subType",subType);
        JSONObject jobj = JSON.parseObject(dataClient.post(map));
        return ResultGenerator.genSuccessResult(jobj.get("msg")+"");
    }


//    /**
//     * 查询设备列表-分页
//     * @param device
//     * @param pageNum
//     * @param pageSize
//     * @return
//     */
//    @GetMapping(value = "/devicebyconditionandpage")
//    public Result getDeviceListByConditionAndPage(Device device, int pageNum, int pageSize) {
//        PageHelper.startPage(pageNum, pageSize);
//
//        return ResultGenerator.genSuccessResult(new PageInfo());
//    }
//
//    /**
//     * 查询设备列表-分页
//     * @param device
//     * @param pageNum
//     * @param pageSize
//     * @return
//     */
//    @GetMapping(value = "/devicebyconditionandpage")
//    public Result devicebyconditionandpage(Device device, int pageNum, int pageSize) {
//        PageHelper.startPage(pageNum, pageSize);
//
//        return ResultGenerator.genSuccessResult(new PageInfo());
//    }
//
//    /**
//     * 查询设备列表-分页
//     * @param device
//     * @param pageNum
//     * @param pageSize
//     * @return
//     */
//    @GetMapping(value = "/getdevicelistbyenterpriseidandpage")
//    public Result getDeviceListByEnterpriseIdAndPage(Device device, int pageNum, int pageSize) {
//        PageHelper.startPage(pageNum, pageSize);
//        return ResultGenerator.genSuccessResult(new PageInfo(deviceService.getDeviceListByEnterpriseId(device)));
//    }
//    /**
//     * 根据编号补全在线设备的信息
//     * @param list
//     * @return
//     */
//
//    @PostMapping( "/getdevicelistbyonlinedevice")
//    public Result getDeviceListByOnLineDevice(@RequestBody List<String> list) {
//
//        List<Device> onlineList=new ArrayList<Device>();
//        for (String deviceNo :list){
//         Device d =deviceService.getDeviceByDeviceNo(deviceNo);
//         onlineList.add(d);
//     }
//        return ResultGenerator.genSuccessResult(onlineList);
//    }
//    /**
//     * 批量插入设备数据
//     * @param deviceList
//     * @return
//     */
//    @PostMapping("/insertmanydevice")
//    public Result insertManyDevice(@RequestBody List<Device> deviceList){
//        return ResultGenerator.genSuccessResult("成功添加"+deviceMapper.insertManyDevice(deviceList)+"个设备");
//    }
//
//    /**
//     * 批量更新加密的设备编号
//     * @param deviceList
//     * @return
//     */
//    @PostMapping("/updatemanydeviceno")
//    public Result updateManyDeviceNo(@RequestBody List<Device> deviceList){
//        return ResultGenerator.genSuccessResult("成功加密"+deviceService.updateManyDeviceNo(deviceList)+"个设备");
//    }
//
//    /**
//     * 编辑设备
//     * @param device
//     * @return
//     */
//    @PostMapping("/editdevice")
//    public Result editDevice(@RequestBody Device device){
//        if(device.getId()!=null){
//            deviceMapper.updateDevice(device);
//        }else{
//            deviceMapper.insertDevice(device);
//        }
//        return ResultGenerator.genSuccessResult();
//    }
//
//    /**
//     * 删除设备
//     * @param id
//     * @return
//     */
//    @PostMapping(value = "/deletedevicebyid")
//    public Result deleteDeviceById(@RequestParam int id){
//        deviceMapper.deleteDeviceById(id);
//        return ResultGenerator.genSuccessResult();
//    }

}
