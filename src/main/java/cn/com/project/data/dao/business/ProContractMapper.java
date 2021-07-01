package cn.com.project.data.dao.business;

import cn.com.project.data.model.business.ProContract;

import java.util.List;

public interface ProContractMapper {
    int deleteByPrimaryKey(String cid);

    int insert(ProContract record);

    int insertSelective(ProContract record);

    ProContract selectByPrimaryKey(String cid);

    /**
     * 为利润计算所用，关联查询出：支出费用、供应合同
     * @param cid
     * @return
     */
    ProContract select4Profit(String cid);

    ProContract selectDetail(String cid);

    ProContract selectByOid(String oid);

    ProContract selectByAnnexId(String annexId);

    List<ProContract> selectByCondition(ProContract record);

    int updateByPrimaryKeySelective(ProContract record);

    int updateByPrimaryKey(ProContract record);
}