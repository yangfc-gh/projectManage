package cn.com.project.data.dao.business;

import cn.com.project.data.model.business.ProSupplycontract;

import java.util.List;

public interface ProSupplycontractMapper {
    int deleteByPrimaryKey(String scid);

    int insert(ProSupplycontract record);

    int insertSelective(ProSupplycontract record);

    ProSupplycontract selectByPrimaryKey(String scid);

    /**
     * 查询单个供应合同基本信息，及支付记录信息
     * @param scid
     * @return
     */
    ProSupplycontract selectDetail(String scid);

    ProSupplycontract selectByAnnexId(String annexId);

    /**
     * 根据订单id，查询基本信息及支付记录
     * @param oid
     * @return
     */
    List<ProSupplycontract> selectByOid(String oid);

    /**
     * 根据订单id，查询基本信息
     * @param oid
     * @return
     */
    List<ProSupplycontract> selectBasicByOid(String oid);

    List<ProSupplycontract> selectByCondition(ProSupplycontract record);

    int updateByPrimaryKeySelective(ProSupplycontract record);

    int updateByPrimaryKey(ProSupplycontract record);
}