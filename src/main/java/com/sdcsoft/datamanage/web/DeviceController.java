package com.sdcsoft.datamanage.web;

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

    /**
     * 查询设备列表-分页
     * @param device
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/devicelistbyconditionandpage")
    public Result getDeviceListByConditionAndPage(Device device, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        return ResultGenerator.genSuccessResult(new PageInfo());
    }



    /**
     * 查询设备列表-分页
     * @param device
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/getdevicelistbyenterpriseidandpage")
    public Result getDeviceListByEnterpriseIdAndPage(Device device, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return ResultGenerator.genSuccessResult(new PageInfo(deviceService.getDeviceListByEnterpriseId(device)));
    }
    /**
     * 根据编号补全在线设备的信息
     * @param list
     * @return
     */

    @PostMapping( "/getdevicelistbyonlinedevice")
    public Result getDeviceListByOnLineDevice(@RequestBody List<String> list) {

        List<Device> onlineList=new ArrayList<Device>();
        for (String deviceNo :list){
         Device d =deviceService.getDeviceByDeviceNo(deviceNo);
         onlineList.add(d);
     }
        return ResultGenerator.genSuccessResult(onlineList);
    }
    /**
     * 批量插入设备数据
     * @param deviceList
     * @return
     */
    @PostMapping("/insertmanydevice")
    public Result insertManyDevice(@RequestBody List<Device> deviceList){
        return ResultGenerator.genSuccessResult("成功添加"+deviceMapper.insertManyDevice(deviceList)+"个设备");
    }

    /**
     * 批量更新加密的设备编号
     * @param deviceList
     * @return
     */
    @PostMapping("/updatemanydeviceno")
    public Result updateManyDeviceNo(@RequestBody List<Device> deviceList){
        return ResultGenerator.genSuccessResult("成功加密"+deviceService.updateManyDeviceNo(deviceList)+"个设备");
    }

    /**
     * 编辑设备
     * @param device
     * @return
     */
    @PostMapping("/editdevice")
    public Result editDevice(@RequestBody Device device){
        if(device.getId()!=null){
            deviceMapper.updateDevice(device);
        }else{
            deviceMapper.insertDevice(device);
        }
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 删除设备
     * @param id
     * @return
     */
    @PostMapping(value = "/deletedevicebyid")
    public Result deleteDeviceById(@RequestParam int id){
        deviceMapper.deleteDeviceById(id);
        return ResultGenerator.genSuccessResult();
    }

}
