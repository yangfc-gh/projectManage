package cn.com.project.modules.order;

import cn.com.project.data.dao.business.ProEnquiryDetailMapper;
import cn.com.project.data.dao.business.ProEnquiryMapper;
import cn.com.project.data.model.business.ProEnquiry;
import cn.com.project.data.model.business.ProEnquiryDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * 询价
 */
@CrossOrigin
@RequestMapping("enquiry")
@Controller
public class EnquiryController {

    private Logger logger = LoggerFactory.getLogger(EnquiryController.class);

    @Autowired
    ProEnquiryMapper proEnquiryMapper;
    @Autowired
    ProEnquiryDetailMapper proEnquiryDetailMapper;

    private final String templatePath = "enquiry/";

    /**
     * 询价列表
     */
    @RequestMapping("/list/{oid}")
    public ModelAndView getList(@PathVariable("oid") String oid, ModelAndView modelAndView) {
        List<ProEnquiry> enquiries = proEnquiryMapper.selectByOid(oid);
        modelAndView.addObject("enquiries", enquiries);
        modelAndView.setViewName(templatePath+"enquiryList");
        return modelAndView;
    }

    /**
     * 询价明细列表（询价本身不存在明细（信息比较简单），所谓明细就是询价条目明细）
     */
    @RequestMapping("/enquiryDetail/list/{eid}")
    public ModelAndView toEnquiry(@PathVariable("eid") String eid, ModelAndView modelAndView) {
        List<ProEnquiryDetail> details = proEnquiryDetailMapper.selectByEid(eid);
        modelAndView.addObject("details", details);
        modelAndView.setViewName(templatePath+"enquiryDetailList");
        return modelAndView;
    }

}
