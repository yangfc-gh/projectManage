package cn.com.project.modules.order;

import cn.com.project.common.*;
import cn.com.project.data.dao.obj.CorporateMapper;
import cn.com.project.data.dao.obj.CustomerMapper;
import cn.com.project.data.dao.obj.SupplierMapper;
import cn.com.project.data.dao.sys.SysDictsMapper;
import cn.com.project.data.model.business.ProOrder;
import cn.com.project.data.model.obj.Corporate;
import cn.com.project.data.model.obj.Customer;
import cn.com.project.data.model.sys.SysDicts;
import cn.com.project.data.model.sys.SysUser;
import cn.com.project.modules.order.service.OrderService;
import cn.com.project.data.dao.business.ProOrderMapper;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin
@RequestMapping("order")
@Controller
public class OrderController {

    @Autowired
    ProOrderMapper proOrderMapper;
    @Autowired
    OrderService orderService;
    @Autowired
    CorporateMapper corporateMapper;
    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    SysDictsMapper sysDictsMapper;
    @Autowired
    SysLogComponent sysLogComponent;

    private final String templatePath = "order/";
    /**
     * 跳转管理首页
     */
    @RequestMapping("/index")
    public ModelAndView toIndex(ModelAndView modelAndView) {
        // 所有字典
        List<SysDicts> sysDictsList = sysDictsMapper.selectByCondition(null);
        List<SysDicts> ddzt = sysDictsList.stream().filter(d -> "D_DDZT".equals(d.getPcode())).collect(Collectors.toList());
        List<SysDicts> quyu = sysDictsList.stream().filter(d -> "D_QUYU".equals(d.getPcode())).collect(Collectors.toList());
        modelAndView.addObject("dict_ddzt", ddzt);
        modelAndView.addObject("dict_quyu", quyu);
        List<Corporate> corporates = corporateMapper.selectByCondition(null);
        modelAndView.addObject("corporates", corporates);
        List<Customer> customers = customerMapper.selectByCondition(null);
        modelAndView.addObject("customers", customers);
        modelAndView.setViewName(templatePath+"orderIndex");
        return modelAndView;
    }

    /**
     * 跳转编辑首页
     */
    @RequestMapping("/toEdit")
    public ModelAndView toEdit(ModelAndView modelAndView, HttpServletRequest request) {
        String oid = request.getParameter("oid");
        if (StringUtils.isNotBlank(oid)) {
            ProOrder order = proOrderMapper.selectByPrimaryKey(oid);
            modelAndView.addObject("orderInfo", order);
        }
        SysDicts dicts = new SysDicts();
        dicts.setState((byte)1);
        List<SysDicts> sysDictsList = sysDictsMapper.selectByCondition(dicts);
        List<SysDicts> quyu = sysDictsList.stream().filter(d -> "D_QUYU".equals(d.getPcode())).collect(Collectors.toList());
        modelAndView.addObject("dict_quyu", quyu);
        List<Corporate> corporates = corporateMapper.selectByCondition(null);
        modelAndView.addObject("corporates", corporates);
        List<Customer> customers = customerMapper.selectByCondition(null);
        modelAndView.addObject("customers", customers);
        modelAndView.setViewName(templatePath+"orderEdit");
        return modelAndView;
    }

    /**
     * 跳转管理首页
     */
    @RequestMapping("/detail/{oid}")
    public ModelAndView toDetail(@PathVariable("oid") String oid, ModelAndView modelAndView) {
        ProOrder order = proOrderMapper.selectDetail(oid);
        // 所有字典
        List<SysDicts> sysDictsList = sysDictsMapper.selectByCondition(null);
        // 翻译一下【刻章】字典项
        Map<String, String> dicts = sysDictsList.stream().collect(Collectors.toMap(SysDicts::getDcode, SysDicts::getDname));
        List<Corporate> corporates = corporateMapper.selectByCondition(null);
        Map<String, String> corp = corporates.stream().collect(Collectors.toMap(Corporate::getCid, Corporate::getName));
        List<Customer> customers = customerMapper.selectByCondition(null);
        Map<String, String> cust = customers.stream().collect(Collectors.toMap(Customer::getCid, Customer::getName));
        order.setStatusText(StringUtils.isNotBlank(order.getStatus()) ? dicts.get(order.getStatus()) : "");
        order.setCustomerText(StringUtils.isNotBlank(order.getCustomerId()) ? cust.get(order.getCustomerId()) : "");
        order.setAreaText(StringUtils.isNotBlank(order.getArea()) ? dicts.get(order.getArea()) : "");
        order.setBidderZText(StringUtils.isNotBlank(order.getBidderZ()) ? corp.get(order.getBidderZ()) : "");
        modelAndView.addObject("orderInfo", order);
        modelAndView.setViewName(templatePath+"orderDetail");
        return modelAndView;
    }

    /**
     * 所有
     * @param order
     */
    @RequestMapping("/list")
    public ModelAndView getList(ProOrder order, ModelAndView modelAndView, HttpServletRequest request) {
        if (null != order && StringUtils.isNotBlank(order.getOtime())) {
            String[] times = order.getOtime().split("~");
            order.setOtimeb(times[0].trim());
            order.setOtimee(times[1].trim());
        }
        PageHelper.startPage(Integer.valueOf(1), 500);//不分页，一页默认最多展示500条，在这使用分页的目的是获取总行数
        List<ProOrder> orders = proOrderMapper.selectByCondition(order);
        PageInfo<ProOrder> resInfo = new PageInfo<ProOrder>(orders);
        modelAndView.addObject("resInfo", resInfo);
        // 所有字典
        List<SysDicts> sysDictsList = sysDictsMapper.selectByCondition(null);
        // 翻译一下字典项
        // 字典项转成map
        Map<String, String> ddzt = sysDictsList.stream().filter(d -> "D_DDZT".equals(d.getPcode())).collect(Collectors.toMap(SysDicts::getDcode, SysDicts::getDname));
        Map<String, String> quyu = sysDictsList.stream().filter(d -> "D_QUYU".equals(d.getPcode())).collect(Collectors.toMap(SysDicts::getDcode, SysDicts::getDname));
        List<Customer> customers = customerMapper.selectByCondition(null);
        Map<String, String> cust = customers.stream().collect(Collectors.toMap(Customer::getCid, Customer::getName));
        for(ProOrder order1 : resInfo.getList()){
            order1.setAreaText(quyu.get(order1.getArea()));
            order1.setCustomerText(cust.get(order1.getCustomerId()));
            order1.setStatusText(ddzt.get(order1.getStatus()));
        }
        modelAndView.addObject("orders", orders);
        modelAndView.setViewName(templatePath+"orderList");
        return modelAndView;
    }

    /**
     * 更新（新增或修改）一个信息
     * @param order
     */
    @PostMapping("/update")
    @ResponseBody
    public ResponseResult doUpdate(ProOrder order, HttpServletRequest request) {
        if (null == order || StringUtils.isBlank(order.getArea())) {
            return new ResponseResult(false, "关键信息为空");
        }
        SysUser user = (SysUser)request.getSession().getAttribute("userInfo");
        if(StringUtils.isBlank(order.getOid())) {
            order.setOid(CommonUtils.createUUID());
            order.setCreater(user.getUid());
            int res = proOrderMapper.insert(order);
            if(res > 0) {
                sysLogComponent.writeLog(SysLogComponent.OPT_ADD, "新增订单", JSONObject.toJSONString(order), "Order", request);
            } else {
                return new ResponseResult(false, "新增失败");
            }
        } else {
            ProOrder order1 = proOrderMapper.selectByPrimaryKey(order.getOid());
            if (null == order1) {
                return new ResponseResult(false, "未找到该订单");
            }
            // 修改
            int res = proOrderMapper.updateByPrimaryKey(order);
            if(res > 0) {
                sysLogComponent.writeLog(SysLogComponent.OPT_UPD, "修改订单", JSONObject.toJSONString(order), "Order", request);
            } else {
                return new ResponseResult(false, "修改失败");
            }
        }
        return new ResponseResult(true, "操作成功");
    }

    /**
     * （管理员）删除一个信息
     * @param oid
     */
    @PostMapping("/del/{oid}")
    @ResponseBody
    public ResponseResult doDel(@PathVariable("oid") String oid, HttpServletRequest request) {
        ProOrder order = proOrderMapper.selectDetail(oid);
        if (null == order) {
            return new ResponseResult(false, "未找到该订单");
        }
        boolean res = orderService.delOrder(oid);
        if (res) {
            sysLogComponent.writeLog(SysLogComponent.OPT_DEL, "删除订单", JSONObject.toJSONString(order), "Order", request);
        } else {
            return new ResponseResult(true, "删除（操作数据）失败");
        }
        return new ResponseResult(true, "操作成功");
    }
}
