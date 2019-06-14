package com.sdcsoft.datamanage.mapper;

import com.sdcsoft.datamanage.model.TokenDict;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface TokenDictMapper {
    @Select("select Code,Name from Token_Dict where Type=2 and SUBSTR(Code, 1,2) = " +
            "(select Code from Token_Dict where Type =1 and orgId =#{enterpriseId} );")
    List<TokenDict> getCustomerListByEnterpriseId(@Param("enterpriseId") Integer enterpriseId);

    @Select("select Code,Name from Token_Dict where Type=#{organizationType}")
    List<TokenDict> getCustomerOrEnterpriseList(@Param("organizationType")Integer organizationType);

    @Select("select OrgUuid from Token_Dict where  Code=#{code}")
    String getTokenDictByCode(String code);

    @Insert("insert into Token_Dict (OrgUuid,Code,Name,Type,orgId) values (#{orgUuid},#{code},#{name},#{type},#{orgId})")
    int insertTokenDict(TokenDict tokenDict);

    @Update("update Token_Dict set Code=#{code},Name=#{name},Type=#{type},orgId=#{orgId} where OrgUuid = #{orgUuid}")
    int updateDeviceType(TokenDict tokenDict);

    @Delete("delete from Token_Dict where Code=#{code}")
    void deleteTokenDictByCode(String code);
}
