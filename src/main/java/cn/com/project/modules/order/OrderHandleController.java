package cn.com.project.modules.order;

import cn.com.project.common.ResponseResult;
import cn.com.project.data.dao.business.ProOrderMapper;
import cn.com.project.data.dao.obj.SupplierMapper;
import cn.com.project.data.model.business.ProEnquiry;
import cn.com.project.data.model.business.ProOrder;
import cn.com.project.data.model.obj.Supplier;
import cn.com.project.data.model.sys.SysUser;
import cn.com.project.modules.order.service.EnquiryService;
import cn.com.project.modules.order.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 订单流程（生命周期）控制
 */
@CrossOrigin
@RequestMapping("orderHandle")
@Controller
public class OrderHandleController {

    private Logger logger = LoggerFactory.getLogger(OrderHandleController.class);

    @Autowired
    ProOrderMapper proOrderMapper;
    @Autowired
    SupplierMapper supplierMapper;
    @Autowired
    EnquiryService enquiryService;
    @Autowired
    OrderService orderService;

    private final String templatePath = "orderhandle/";

    /**
     * 新增询价
     */
    @RequestMapping("/toEnquiry/{oid}")
    public ModelAndView toEnquiry(@PathVariable("oid") String oid, ModelAndView modelAndView) {
        ProOrder order = proOrderMapper.selectByPrimaryKey(oid);
        if (null == order){
            modelAndView.addObject("message", "未找到相应订单");
        }
        List<Supplier> suppliers = supplierMapper.selectByCondition(null);
        modelAndView.addObject("suppliers", suppliers);
        modelAndView.addObject("oid", oid);
        modelAndView.setViewName(templatePath+"enquiry");
        return modelAndView;
    }

    /**
     * 新增询价
     */
    @RequestMapping("/enquiry/add")
    @ResponseBody
    public ResponseResult addEnquiry(@RequestBody ProEnquiry enquiry, ModelAndView modelAndView) {
        ProOrder order = proOrderMapper.selectByPrimaryKey(enquiry.getOid());
        if (null == order){
            modelAndView.addObject("message", "未找到相应订单");
        }
        try {
            boolean res = enquiryService.add(enquiry);
            if (res) {
                orderService.orderState(enquiry.getOid(), "DDZT_XUNJIA");
                return new ResponseResult(true);
            }else {
                return new ResponseResult(false, "数据库操作失败");
            }
        } catch (Exception e){
            logger.error(e.getMessage());
        }

        return new ResponseResult(false);
    }
}
