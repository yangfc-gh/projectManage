package cn.com.project.data.dao.obj;

import cn.com.project.data.model.obj.Customer;

import java.util.List;

public interface CustomerMapper {
    int deleteByPrimaryKey(String cid);

    int insert(Customer record);

    int insertSelective(Customer record);

    Customer selectByPrimaryKey(String cid);

    List<Customer> selectByCondition(Customer record);

    int updateByPrimaryKeySelective(Customer record);

    int updateByPrimaryKey(Customer record);
}