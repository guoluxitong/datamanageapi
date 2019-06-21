package com.sdcsoft.datamanage.web;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sdcsoft.datamanage.mapper.CustomerMapper;
import com.sdcsoft.datamanage.mapper.TokenDictMapper;
import com.sdcsoft.datamanage.model.Customer;
import com.sdcsoft.datamanage.model.TokenDict;
import com.sdcsoft.datamanage.utils.Result;
import com.sdcsoft.datamanage.utils.ResultGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/customer")
public class CustomerController {
    
    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private TokenDictMapper tokenDictMapper;
    /**
     * 查询客户列表-分页
     * @param customer
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/customerlistbyconditionandpage")
    public Result getCustomerListByConditionAndPage(Customer customer, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Customer> list =customerMapper.getCustomerListByCondition(customer);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 查询客户列表-不分页
     * @param customer
     * @return
     */
    @GetMapping(value = "/customerlistbycondition")
    public Result getCustomerListByCondition(Customer customer) {
        return ResultGenerator.genSuccessResult(customerMapper.getCustomerListByCondition(customer));
    }

    /**
     * 编辑客户
     * @param customer
     * @return
     */
    @PostMapping("/editcustomer")
    public Result editCustomer(@RequestBody Customer customer){
        if(customer.getId()!=null){
            //在修改该客户之前 根据id先拿到修改之前的客户编号 根据编号去TokenDict 查到
            TokenDict t= new TokenDict();
            t.setOrgUuid(tokenDictMapper.getTokenDictByCode(customerMapper.getCustomerNoById(customer.getId())));
            t.setType(2);
            t.setOrgId(customer.getEnterpriseId());
            t.setName(customer.getCustomerName());
            t.setCode(customer.getCustomerNo());
            tokenDictMapper.updateDeviceType(t);
            customerMapper.updateCustomer(customer);
        }else{
            customerMapper.insertCustomer(customer);
            UUID uuid  =  UUID.randomUUID();
            TokenDict t =new TokenDict();
            t.setOrgUuid(UUID.randomUUID().toString());
            t.setCode(customer.getCustomerNo());
            t.setName(customer.getCustomerName());
            t.setOrgId(customer.getEnterpriseId());
            t.setType(2);
            tokenDictMapper.insertTokenDict(t);
        }
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 删除客户
     * @param id
     * @return
     */
    @PostMapping(value = "/deletecustomerbyid")
    public Result deleteCustomerById(@RequestParam int id){
        tokenDictMapper.deleteTokenDictByCode(customerMapper.getCustomerNoById(id));
        customerMapper.deleteCustomerById(id);
        return ResultGenerator.genSuccessResult();
    }
}
