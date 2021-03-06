package cn.com.project.modules.order;

import cn.com.project.common.*;
import cn.com.project.data.dao.obj.CorporateMapper;
import cn.com.project.data.dao.obj.CustomerMapper;
import cn.com.project.data.dao.obj.SupplierMapper;
import cn.com.project.data.dao.sys.SysDictsMapper;
import cn.com.project.data.model.business.ProContract;
import cn.com.project.data.model.business.ProOrder;
import cn.com.project.data.model.business.ProSupplycontract;
import cn.com.project.data.model.business.ProSupplycontractPayment;
import cn.com.project.data.model.obj.Corporate;
import cn.com.project.data.model.obj.Customer;
import cn.com.project.data.model.obj.Supplier;
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
import org.springframework.ui.Model;
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
    SupplierMapper supplierMapper;
    @Autowired
    SysDictsMapper sysDictsMapper;
    @Autowired
    SysLogComponent sysLogComponent;

    private final String templatePath = "order/";
    /**
     * ??????????????????
     */
    @RequestMapping("/index")
    public ModelAndView toIndex(ModelAndView modelAndView) {
        // ????????????
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
     * ??????????????????
     */
    @RequestMapping("/toEdit")
    public ModelAndView toEdit(ModelAndView modelAndView, HttpServletRequest request) {
        String oid = request.getParameter("oid");

        SysDicts dicts = new SysDicts();
        dicts.setPcode("D_QUYU");
        List<SysDicts> quyu = sysDictsMapper.selectByCondition(dicts);
        List<Corporate> corporates = corporateMapper.selectByCondition(null);
        List<Customer> customers = customerMapper.selectByCondition(null);
        if (StringUtils.isNotBlank(oid)) {
            ProOrder order = proOrderMapper.selectByPrimaryKey(oid);
            if (null != order) {
                // ???????????????????????????????????????????????????????????????????????????
                corporates = corporates.stream().filter(c -> "1".equals(c.getStatus()) || order.getBidderZ().equals(c.getCid())).collect(Collectors.toList());
                customers = customers.stream().filter(c -> "1".equals(c.getStatus()) || order.getCustomerId().equals(c.getCid())).collect(Collectors.toList());
                quyu = quyu.stream().filter(d -> 1 == d.getState() || order.getArea().equals(d.getDcode())).collect(Collectors.toList());
                modelAndView.addObject("orderInfo", order);
            }
        } else {
            // ?????????????????????????????????
            corporates = corporates.stream().filter(c -> "1".equals(c.getStatus())).collect(Collectors.toList());
            customers = customers.stream().filter(c -> "1".equals(c.getStatus())).collect(Collectors.toList());
            quyu = quyu.stream().filter(d -> 1 == d.getState()).collect(Collectors.toList());
        }
        modelAndView.addObject("dict_quyu", quyu);
        modelAndView.addObject("corporates", corporates);
        modelAndView.addObject("customers", customers);
        modelAndView.setViewName(templatePath+"orderEdit");
        return modelAndView;
    }

    /**
     * ??????????????????
     */
    @RequestMapping("/detail/{oid}")
    public ModelAndView toDetail(@PathVariable("oid") String oid, ModelAndView modelAndView) {
        ProOrder order = proOrderMapper.selectDetail(oid);
        // ????????????
        List<SysDicts> sysDictsList = sysDictsMapper.selectByCondition(null);
        // ?????????????????????????????????
        Map<String, String> dicts = sysDictsList.stream().collect(Collectors.toMap(SysDicts::getDcode, SysDicts::getDname));
        // ??????????????????
        List<Corporate> corporates = corporateMapper.selectByCondition(null);
        Map<String, String> corp = corporates.stream().collect(Collectors.toMap(Corporate::getCid, Corporate::getName));
        // ??????????????????
        List<Customer> customers = customerMapper.selectByCondition(null);
        Map<String, String> cust = customers.stream().collect(Collectors.toMap(Customer::getCid, Customer::getName));
        // ?????????????????????
        List<Supplier> suppliers = supplierMapper.selectByCondition(null);
        Map<String, String> supp = suppliers.stream().collect(Collectors.toMap(Supplier::getSid, Supplier::getName));
        // ??????????????????
        order.setStatusText(StringUtils.isNotBlank(order.getStatus()) ? dicts.get(order.getStatus()) : "");
        // ??????????????????
        order.setCustomerText(StringUtils.isNotBlank(order.getCustomerId()) ? cust.get(order.getCustomerId()) : "");
        // ??????????????????
        order.setAreaText(StringUtils.isNotBlank(order.getArea()) ? dicts.get(order.getArea()) : "");
        // ?????????????????????
        order.setBidderZText(StringUtils.isNotBlank(order.getBidderZ()) ? corp.get(order.getBidderZ()) : "");
        // ?????????????????????
        if (null != order.getContract()) {
            ProContract contract = order.getContract();
            contract.setStatus(contract.getStatus().equals("0") ? "?????????" : "?????????");
            contract.setPartya(cust.get(contract.getPartya()));
            contract.setPartyb(corp.get(contract.getPartyb()));
            contract.setPartyz(corp.get(contract.getPartyz()));
            contract.setPartyu(cust.get(contract.getPartyu()));
        }
        // ???????????????????????????
        for (ProSupplycontract supplycontract : order.getSupplycontracts()) {
            supplycontract.setPartya(corp.get(supplycontract.getPartya()));
            supplycontract.setPartyb(supp.get(supplycontract.getPartyb()));
            for (ProSupplycontractPayment payment : supplycontract.getPayments()) {
                payment.setPayCorporate(StringUtils.isNotBlank(payment.getPayCorporate()) ? corp.get(payment.getPayCorporate()) : null);
            }
        }
        modelAndView.addObject("orderInfo", order);
        modelAndView.setViewName(templatePath+"orderDetail");
        return modelAndView;
    }

    /**
     * ??????
     * @param order
     */
    @RequestMapping("/list")
    public String getList(ProOrder order, Model model, HttpServletRequest request) {
        if (null != order && StringUtils.isNotBlank(order.getOtime())) {
            String[] times = order.getOtime().split("~");
            order.setOtimeb(times[0].trim());
            order.setOtimee(times[1].trim());
        }
        PageHelper.startPage(Integer.valueOf(1), 500);//????????????????????????????????????500???????????????????????????????????????????????????
        List<ProOrder> orders = proOrderMapper.selectByCondition(order);
        // ????????????
        List<SysDicts> sysDictsList = sysDictsMapper.selectByCondition(null);
        // ?????????????????????
        // ???????????????map
        Map<String, String> ddzt = sysDictsList.stream().filter(d -> "D_DDZT".equals(d.getPcode())).collect(Collectors.toMap(SysDicts::getDcode, SysDicts::getDname));
        Map<String, String> quyu = sysDictsList.stream().filter(d -> "D_QUYU".equals(d.getPcode())).collect(Collectors.toMap(SysDicts::getDcode, SysDicts::getDname));
        List<Customer> customers = customerMapper.selectByCondition(null);
        Map<String, String> cust = customers.stream().collect(Collectors.toMap(Customer::getCid, Customer::getName));
        for(ProOrder order1 : orders){
            order1.setAreaText(quyu.get(order1.getArea()));
            order1.setCustomerText(cust.get(order1.getCustomerId()));
            order1.setStatusText(ddzt.get(order1.getStatus()));
        }
        PageInfo<ProOrder> resInfo = new PageInfo<ProOrder>(orders);
        model.addAttribute("resInfo", resInfo);
        return templatePath+"orderList";
        /*modelAndView.addObject("resInfo", resInfo);
        modelAndView.addObject("orders", orders);
        modelAndView.setViewName(templatePath+"orderList");
        return modelAndView;*/
    }

    /**
     * ???????????????????????????????????????
     * @param order
     */
    @PostMapping("/update")
    @ResponseBody
    public ResponseResult doUpdate(ProOrder order, HttpServletRequest request) {
        if (null == order || StringUtils.isBlank(order.getArea())) {
            return new ResponseResult(false, "??????????????????");
        }
        order.setSupplement("on".equals(order.getSupplement()) ? "1" : "0");
        SysUser user = (SysUser)request.getSession().getAttribute("userInfo");
        if(StringUtils.isBlank(order.getOid())) {
            order.setOid(CommonUtils.createUUID());
            order.setCreater(user.getUid());
            int res = proOrderMapper.insert(order);
            if(res > 0) {
                sysLogComponent.writeLog(SysLogComponent.OPT_ADD, "????????????", JSONObject.toJSONString(order), "Order", request);
            } else {
                return new ResponseResult(false, "????????????");
            }
        } else {
            ProOrder order1 = proOrderMapper.selectByPrimaryKey(order.getOid());
            if (null == order1) {
                return new ResponseResult(false, "??????????????????");
            }
            // ??????
            int res = proOrderMapper.updateByPrimaryKey(order);
            if(res > 0) {
                sysLogComponent.writeLog(SysLogComponent.OPT_UPD, "????????????", JSONObject.toJSONString(order), "Order", request);
            } else {
                return new ResponseResult(false, "????????????");
            }
        }
        return new ResponseResult(true, "????????????");
    }

    /**
     * ?????????????????????????????????
     * @param oid
     */
    @PostMapping("/del/{oid}")
    @ResponseBody
    public ResponseResult doDel(@PathVariable("oid") String oid, HttpServletRequest request) {
        ProOrder order = proOrderMapper.selectDetail(oid);
        if (null == order) {
            return new ResponseResult(false, "??????????????????");
        }
        boolean res = orderService.delOrder(oid);
        if (res) {
            sysLogComponent.writeLog(SysLogComponent.OPT_DEL, "????????????", JSONObject.toJSONString(order), "Order", request);
        } else {
            return new ResponseResult(true, "??????????????????????????????");
        }
        return new ResponseResult(true, "????????????");
    }
}
