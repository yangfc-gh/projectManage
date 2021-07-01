package cn.com.project.data.dao.business;

import cn.com.project.data.model.business.ProContractExpend;

import java.util.List;

/**
 * 项目支出费用记录
 */
public interface ProContractExpendMapper {
    int deleteByPrimaryKey(String eid);

    int insert(ProContractExpend record);

    int insertSelective(ProContractExpend record);

    ProContractExpend selectByPrimaryKey(String eid);

    List<ProContractExpend> selectByCid(String cid);

    List<ProContractExpend> selectByCondition(ProContractExpend record);

    int updateByPrimaryKeySelective(ProContractExpend record);

    int updateByPrimaryKey(ProContractExpend record);
}