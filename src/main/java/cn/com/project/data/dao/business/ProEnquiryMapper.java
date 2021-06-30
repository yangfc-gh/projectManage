package cn.com.project.data.dao.business;

import cn.com.project.data.model.business.ProEnquiry;

import java.util.List;

/**
 * 询价信息
 */
public interface ProEnquiryMapper {
    int deleteByPrimaryKey(String eid);

    int insert(ProEnquiry record);

    int insertSelective(ProEnquiry record);

    ProEnquiry selectByPrimaryKey(String eid);

    List<ProEnquiry> selectByOid(String oid);

    List<ProEnquiry> selectByCondition(ProEnquiry record);

    int updateByPrimaryKeySelective(ProEnquiry record);

    int updateByPrimaryKey(ProEnquiry record);
}