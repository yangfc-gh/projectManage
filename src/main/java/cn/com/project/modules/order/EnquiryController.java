package cn.com.project.modules.order;

import cn.com.project.common.ResponseResult;
import cn.com.project.data.dao.business.ProEnquiryDetailMapper;
import cn.com.project.data.dao.business.ProEnquiryMapper;
import cn.com.project.data.model.business.ProEnquiry;
import cn.com.project.data.model.business.ProEnquiryDetail;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
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
    @RequestMapping("/list")
    public ModelAndView getList(ProEnquiry enquiry, ModelAndView modelAndView) {
        PageHelper.startPage(Integer.valueOf(1), 500);//不分页，一页默认最多展示500条，在这使用分页的目的是获取总行数
        List<ProEnquiry> enquiries = proEnquiryMapper.selectByCondition(enquiry);
        PageInfo<ProEnquiry> resInfo = new PageInfo<ProEnquiry>(enquiries);
        modelAndView.addObject("resInfo", resInfo);
        modelAndView.setViewName(templatePath+"enquiryList");
        return modelAndView;
    }

    /**
     * 删除
     */
    @RequestMapping("/del/{eid}")
    @ResponseBody
    public ResponseResult doDel(@PathVariable("eid") String eid, ModelAndView modelAndView) {
        ProEnquiry enquiry = proEnquiryMapper.selectByPrimaryKey(eid);
        if (null == enquiry) {
            new ResponseResult(false, "未找到询价信息");
        }
        int res = proEnquiryMapper.deleteByPrimaryKey(eid);
        if (res > 0) {
            return new ResponseResult(true);
        } else {
            return new ResponseResult(false);
        }
    }

    /**
     * 询价明细列表（询价本身不存在明细（信息比较简单），所谓明细就是询价条目明细）
     */
    @RequestMapping("/enquiryDetail/list")
    public ModelAndView toEnquiry(ProEnquiryDetail enquiryDetail, ModelAndView modelAndView) {
        List<ProEnquiryDetail> details = proEnquiryDetailMapper.selectByCondition(enquiryDetail);
        modelAndView.addObject("details", details);
        modelAndView.setViewName(templatePath+"enquiryDetailList");
        return modelAndView;
    }

}
