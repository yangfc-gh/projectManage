package cn.com.project.data.dao.business;

import cn.com.project.data.model.business.ProContract;

import java.util.List;

public interface ProContractMapper {
    int deleteByPrimaryKey(String cid);

    int insert(ProContract record);

    int insertSelective(ProContract record);

    ProContract selectByPrimaryKey(String cid);

    ProContract selectDetail(String cid);

    ProContract selectByOid(String oid);

    ProContract selectByAnnexId(String annexId);

    List<ProContract> selectByCondition(ProContract record);

    int updateByPrimaryKeySelective(ProContract record);

    int updateByPrimaryKey(ProContract record);
}