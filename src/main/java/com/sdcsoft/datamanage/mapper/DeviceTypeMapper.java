package com.sdcsoft.datamanage.mapper;

import com.sdcsoft.datamanage.model.DeviceType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface DeviceTypeMapper {

    @Select("select * from DeviceType ")
    List<DeviceType> getDeviceTypeList();

    @Select("<script>" +
            "select * from DeviceType "+
            "<where>"+
            " 1=1 "+
            "<if test='deviceType != null and deviceType.length>0'> "+
            " AND DeviceType = #{deviceType} "+
            "</if>"+
            "</where>"+
            "</script>")
    List<DeviceType> getDeviceTypeListByCondition(DeviceType deviceType);

    @Insert("insert into DeviceType (Id,DeviceType) values (#{id},#{deviceType})")
    int insertDeviceType(DeviceType deviceType);

    @Update("update DeviceType set DeviceType=#{deviceType} where Id = #{id}")
    int updateDeviceType(DeviceType deviceType);

    @Delete("delete from DeviceType where Id=#{id}")
    int deleteDeviceTypeById(Integer id);
}
