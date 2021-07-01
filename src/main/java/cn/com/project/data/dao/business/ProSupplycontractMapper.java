package cn.com.project.data.dao.business;

import cn.com.project.data.model.business.ProSupplycontract;

import java.util.List;

public interface ProSupplycontractMapper {
    int deleteByPrimaryKey(String scid);

    int insert(ProSupplycontract record);

    int insertSelective(ProSupplycontract record);

    ProSupplycontract selectByPrimaryKey(String scid);

    ProSupplycontract selectDetail(String scid);

    ProSupplycontract selectByAnnexId(String annexId);

    List<ProSupplycontract> selectByOid(String oid);

    List<ProSupplycontract> selectByCondition(ProSupplycontract record);

    int updateByPrimaryKeySelective(ProSupplycontract record);

    int updateByPrimaryKey(ProSupplycontract record);
}