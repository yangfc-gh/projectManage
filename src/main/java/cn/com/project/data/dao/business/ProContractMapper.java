package cn.com.project.data.dao.business;

import cn.com.project.data.model.business.ProContract;

public interface ProContractMapper {
    int deleteByPrimaryKey(String cid);

    int insert(ProContract record);

    int insertSelective(ProContract record);

    ProContract selectByPrimaryKey(String cid);

    ProContract selectByOid(String oid);

    ProContract selectByAnnexId(String annexId);

    int updateByPrimaryKeySelective(ProContract record);

    int updateByPrimaryKey(ProContract record);
}