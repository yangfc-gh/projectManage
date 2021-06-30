package cn.com.project.data.dao.business;

import cn.com.project.data.model.business.ProContractPayment;

import java.util.List;

public interface ProContractPaymentMapper {
    int deleteByPrimaryKey(String pid);

    int insert(ProContractPayment record);

    int insertSelective(ProContractPayment record);

    ProContractPayment selectByPrimaryKey(String pid);

    ProContractPayment selectByAnnexId(String annexId);

    List<ProContractPayment> selectByCid(String cid);

    List<ProContractPayment> selectByCondition(ProContractPayment record);

    int updateByPrimaryKeySelective(ProContractPayment record);

    int updateByPrimaryKey(ProContractPayment record);
}