package com.sdcsoft.datamanage.service;

import com.sdcsoft.datamanage.model.Device;

import java.util.List;
public interface DeviceService {

    List<Device> getDeviceListByCondition(Device device);

    List<Device> getDeviceListByEnterpriseId(Device device);

    Device getDeviceByDeviceNo(String deviceNo);

    int updateManyDeviceNo(List<Device> deviceList);
}
