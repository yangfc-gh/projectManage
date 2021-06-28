package cn.com.project.modules.order.service;

import cn.com.project.common.CommonUtils;
import cn.com.project.data.dao.business.ProEnquiryDetailMapper;
import cn.com.project.data.dao.business.ProEnquiryMapper;
import cn.com.project.data.model.business.ProEnquiry;
import cn.com.project.data.model.business.ProEnquiryDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 询价单服务层
 */
@Service
public class EnquiryService {
    @Autowired
    ProEnquiryMapper proEnquiryMapper;
    @Autowired
    ProEnquiryDetailMapper proEnquiryDetailMapper;

    /**
     * 询价新增-同时新增明细
     * @param enquiry
     * @return
     */
    @Transactional
    public boolean add(ProEnquiry enquiry) {
        enquiry.setEid(CommonUtils.createUUID());
        for (ProEnquiryDetail ped : enquiry.getEnquiryDetails()) {
            ped.setEid(enquiry.getEid());
            ped.setDid(CommonUtils.createUUID());
        }
        int res = proEnquiryMapper.insertSelective(enquiry);
        if(res <= 0) {
            throw new RuntimeException("add enquiry failed");
        }
        res = proEnquiryDetailMapper.insertBatch(enquiry.getEnquiryDetails());
        if(res <= 0) {
            throw new RuntimeException("add enquiry detail failed");
        }
        return true;
    }
}
