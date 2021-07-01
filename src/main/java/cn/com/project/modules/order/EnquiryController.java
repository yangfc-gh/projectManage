package cn.com.project.modules.order;

import cn.com.project.common.ResponseResult;
import cn.com.project.data.dao.business.ProEnquiryDetailMapper;
import cn.com.project.data.dao.business.ProEnquiryMapper;
import cn.com.project.data.dao.business.ProOrderMapper;
import cn.com.project.data.dao.obj.SupplierMapper;
import cn.com.project.data.model.business.ProEnquiry;
import cn.com.project.data.model.business.ProEnquiryDetail;
import cn.com.project.data.model.business.ProOrder;
import cn.com.project.data.model.business.ProSupplycontract;
import cn.com.project.data.model.obj.Supplier;
import cn.com.project.modules.order.service.OrderService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
import java.util.Map;
import java.util.stream.Collectors;

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
    @Autowired
    ProOrderMapper orderMapper;
    @Autowired
    OrderService orderService;
    @Autowired
    SupplierMapper supplierMapper;

    private final String templatePath = "enquiry/";
    private final String objName = "enquiry";
    private final String objNameSub = "enquiryDetail";
    /**
     * 跳转管理首页
     */
    @RequestMapping("/index")
    public ModelAndView toIndex(ModelAndView modelAndView) {
        List<Supplier> suppliers = supplierMapper.selectByCondition(null);
        modelAndView.addObject("suppliers", suppliers);
        modelAndView.setViewName(templatePath+objName+"Index");
        return modelAndView;
    }
    /**
     * 报价明细列表
     */
    @RequestMapping("/enquiryDetail/index")
    public ModelAndView toEnquiryDetailIndex(ModelAndView modelAndView) {
        List<Supplier> suppliers = supplierMapper.selectByCondition(null);
        modelAndView.addObject("suppliers", suppliers);
        modelAndView.setViewName(templatePath+objNameSub+"Index");
        return modelAndView;
    }
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
            // 如果清空了询价
            List<ProEnquiry> enquiries = proEnquiryMapper.selectByOid(enquiry.getOid());
            if (CollectionUtils.isEmpty(enquiries)) {
                ProOrder order = orderMapper.selectByPrimaryKey(enquiry.getOid());
                // 且状态为询价，则回退订单状态到”新订单“
                if ("DDZT_XUNJIA".equals(order.getStatus())) {
                    orderService.orderState(enquiry.getOid(), "DDZT_XDD");
                }
            }
            return new ResponseResult(true);
        } else {
            return new ResponseResult(false);
        }
    }

    /**
     * 询价明细列表（询价本身不存在明细（信息比较简单），所谓明细就是询价条目明细）
     */
    @RequestMapping("/enquiryDetail/list")
    public ModelAndView toEnquiryDetail(ProEnquiryDetail enquiryDetail, ModelAndView modelAndView) {
        List<ProEnquiryDetail> details = proEnquiryDetailMapper.selectByCondition(enquiryDetail);
        modelAndView.addObject("details", details);
        modelAndView.setViewName(templatePath+objNameSub+"List");
        return modelAndView;
    }

    /**
     * 询价明细列表（独立查询使用）
     */
    @RequestMapping("/enquiryDetailInfo/list")
    public ModelAndView toEnquiryDetailInfo(ProEnquiryDetail enquiryDetail, ModelAndView modelAndView) {
        PageHelper.startPage(Integer.valueOf(1), 500);//不分页，一页默认最多展示500条，在这使用分页的目的是获取总行数
        List<ProEnquiryDetail> details = proEnquiryDetailMapper.selectByCondition(enquiryDetail);
        // 全部供应商信息
        List<Supplier> suppliers = supplierMapper.selectByCondition(null);
        Map<String, String> supp = suppliers.stream().collect(Collectors.toMap(Supplier::getSid, Supplier::getName));
        for (ProEnquiryDetail detail : details) {
            detail.getEnquiry().setSupplierName(StringUtils.isNotBlank(detail.getEnquiry().getSupplier()) ? supp.get(detail.getEnquiry().getSupplier()) : null);
        }
        PageInfo<ProEnquiryDetail> resInfo = new PageInfo<>(details);
        modelAndView.addObject("resInfo", resInfo);
        modelAndView.setViewName(templatePath+objNameSub+"InfoList");
        return modelAndView;
    }

}
