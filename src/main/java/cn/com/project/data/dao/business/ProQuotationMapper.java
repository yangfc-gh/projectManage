package cn.com.project.data.dao.business;

import cn.com.project.data.model.business.ProQuotation;

public interface ProQuotationMapper {
    int deleteByPrimaryKey(String qid);

    int insert(ProQuotation record);

    int insertSelective(ProQuotation record);

    ProQuotation selectByPrimaryKey(String qid);

    ProQuotation selectByOid(String oid);

    ProQuotation selectByAnnexId(String annexId);

    int updateByPrimaryKeySelective(ProQuotation record);

    int updateByPrimaryKey(ProQuotation record);
}