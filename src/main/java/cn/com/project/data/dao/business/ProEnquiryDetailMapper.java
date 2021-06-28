package cn.com.project.data.dao.business;

import cn.com.project.data.model.business.ProEnquiryDetail;

import java.util.List;

public interface ProEnquiryDetailMapper {
    int deleteByPrimaryKey(String did);

    int insert(ProEnquiryDetail record);

    int insertSelective(ProEnquiryDetail record);

    int insertBatch(List<ProEnquiryDetail> details);

    ProEnquiryDetail selectByPrimaryKey(String did);

    List<ProEnquiryDetail> selectByEid(String eid);

    int updateByPrimaryKeySelective(ProEnquiryDetail record);

    int updateByPrimaryKey(ProEnquiryDetail record);
}