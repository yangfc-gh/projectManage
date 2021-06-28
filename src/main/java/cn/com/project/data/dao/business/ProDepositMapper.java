package cn.com.project.data.dao.business;

import cn.com.project.data.model.business.ProDeposit;

import java.util.List;

/**
 * 投标保证金管理
 */
public interface ProDepositMapper {
    int deleteByPrimaryKey(String did);

    int insert(ProDeposit record);

    int insertSelective(ProDeposit record);

    ProDeposit selectByPrimaryKey(String did);

    List<ProDeposit> selectByOid(String oid);

    List<ProDeposit> selectByCondition(ProDeposit record);

    int updateByPrimaryKeySelective(ProDeposit record);

    int updateByPrimaryKey(ProDeposit record);
}