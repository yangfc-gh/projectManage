package cn.com.project.data.dao.business;

import cn.com.project.data.model.business.ProOrder;

import java.util.List;

/**
 * 业务订单
 */
public interface ProOrderMapper {
    int deleteByPrimaryKey(String oid);

    int insert(ProOrder record);

    int insertSelective(ProOrder record);

    ProOrder selectByPrimaryKey(String oid);

    ProOrder selectDetail(String oid);

    List<ProOrder> selectByCondition(ProOrder record);

    int updateByPrimaryKeySelective(ProOrder record);

    int updateByPrimaryKey(ProOrder record);
}