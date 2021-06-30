package cn.com.project.data.dao.business;

import cn.com.project.data.model.business.ProSupplycontractPayment;

import java.util.List;

/**
 * 供应合同支付管理
 */
public interface ProSupplycontractPaymentMapper {
    int deleteByPrimaryKey(String pid);

    int insert(ProSupplycontractPayment record);

    int insertSelective(ProSupplycontractPayment record);

    ProSupplycontractPayment selectByPrimaryKey(String pid);

    ProSupplycontractPayment selectByAnnexId(String annexId);

    List<ProSupplycontractPayment> selectByScid(String scid);

    List<ProSupplycontractPayment> selectByCondition(ProSupplycontractPayment record);

    int updateByPrimaryKeySelective(ProSupplycontractPayment record);

    int updateByPrimaryKey(ProSupplycontractPayment record);
}