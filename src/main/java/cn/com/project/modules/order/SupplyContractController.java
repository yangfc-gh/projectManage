package cn.com.project.modules.order;

import cn.com.project.common.*;
import cn.com.project.data.dao.business.ProOrderMapper;
import cn.com.project.data.dao.business.ProSupplycontractMapper;
import cn.com.project.data.dao.business.ProSupplycontractPaymentMapper;
import cn.com.project.data.dao.obj.CorporateMapper;
import cn.com.project.data.dao.obj.CustomerMapper;
import cn.com.project.data.dao.obj.SupplierMapper;
import cn.com.project.data.dao.sys.SysDictsMapper;
import cn.com.project.data.model.business.*;
import cn.com.project.data.model.obj.Corporate;
import cn.com.project.data.model.obj.Customer;
import cn.com.project.data.model.obj.Supplier;
import cn.com.project.data.model.sys.SysDicts;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin
@RequestMapping("supplycontract")
@Controller
public class SupplyContractController {
    private Logger logger = LoggerFactory.getLogger(SupplyContractController.class);

    @Autowired
    ProSupplycontractMapper supplycontractMapper;
    @Autowired
    ProSupplycontractPaymentMapper supplycontractPaymentMapper;
    @Autowired
    ProOrderMapper orderMapper;
    @Autowired
    CorporateMapper corporateMapper;
    @Autowired
    SupplierMapper supplierMapper;
    @Autowired
    SysLogComponent sysLogComponent;

    private final String templatePath = "supplycontract/";
    private final String objName = "supplyContract";
    private final String objNameSub = "supplyContractPayment";

    /**
     * ??????????????????
     */
    @RequestMapping("/index")
    public ModelAndView toIndex(ModelAndView modelAndView) {
        List<Corporate> corporates = corporateMapper.selectByCondition(null);
        modelAndView.addObject("corporates", corporates);
        List<Supplier> suppliers = supplierMapper.selectByCondition(null);
        modelAndView.addObject("suppliers", suppliers);
        modelAndView.setViewName(templatePath+objName+"Index");
        return modelAndView;
    }
    /**
     * ?????????
     */
    @RequestMapping("/toEdit")
    public ModelAndView toEdit(ModelAndView modelAndView, HttpServletRequest request) {
        String oid = request.getParameter("oid");
        String scid = request.getParameter("scid");
        ProSupplycontract supplycontract = null;
        if (StringUtils.isNotBlank(scid)) {
            supplycontract = supplycontractMapper.selectByPrimaryKey(scid);
        } else {
            supplycontract = new ProSupplycontract();
            supplycontract.setOid(oid);
        }
        modelAndView.addObject("supplycontract", supplycontract);
        List<Corporate> corporates = corporateMapper.selectByCondition(null);
        ProSupplycontract finalSupplycontract = supplycontract;
        corporates = corporates.stream().filter(c -> "1".equals(c.getStatus()) || StringUtils.equals(c.getCid(), finalSupplycontract.getPartya())).collect(Collectors.toList());
        modelAndView.addObject("corporates", corporates);
        List<Supplier> suppliers = supplierMapper.selectByCondition(null);
        suppliers = suppliers.stream().filter(s -> "1".equals(s.getStatus()) || StringUtils.equals(s.getStatus(), finalSupplycontract.getPartyb())).collect(Collectors.toList());
        modelAndView.addObject("suppliers", suppliers);
        modelAndView.setViewName(templatePath+objName+"Edit");
        return modelAndView;
    }

    /**
     * ?????????
     */
    @RequestMapping("/payment/toEdit")
    public ModelAndView toPaymentEdit(ModelAndView modelAndView, HttpServletRequest request) {
        String scid = request.getParameter("scid");
        String pid = request.getParameter("pid");
        ProSupplycontractPayment payment = null;
        if (StringUtils.isNotBlank(pid)) {
            payment = supplycontractPaymentMapper.selectByPrimaryKey(pid);
        } else {
            payment = new ProSupplycontractPayment();
            payment.setScid(scid);
        }
        List<Corporate> corporates = corporateMapper.selectByCondition(null);
        ProSupplycontractPayment finalPayment = payment;
        corporates = corporates.stream().filter(c -> "1".equals(c.getStatus()) || StringUtils.equals(finalPayment.getPayCorporate(), c.getCid())).collect(Collectors.toList());
        modelAndView.addObject("corporates", corporates);
        modelAndView.addObject("payment", payment);
        modelAndView.setViewName(templatePath+objNameSub+"Edit");
        return modelAndView;
    }
    /**
     * ??????????????????
     */
    @RequestMapping("/detail/{scid}")
    public ModelAndView toDetail(@PathVariable("scid") String scid, ModelAndView modelAndView) {
        ProSupplycontract supplycontract = supplycontractMapper.selectDetail(scid);
        // ??????????????????
        List<Corporate> corporates = corporateMapper.selectByCondition(null);
        Map<String, String> corp = corporates.stream().collect(Collectors.toMap(Corporate::getCid, Corporate::getName));
        // ?????????????????????
        List<Supplier> suppliers = supplierMapper.selectByCondition(null);
        Map<String, String> supp = suppliers.stream().collect(Collectors.toMap(Supplier::getSid, Supplier::getName));

        supplycontract.setPartya(StringUtils.isNotBlank(supplycontract.getPartya()) ? corp.get(supplycontract.getPartya()) : null);
        supplycontract.setPartyb(StringUtils.isNotBlank(supplycontract.getPartyb()) ? supp.get(supplycontract.getPartyb()) : null);
        for (ProSupplycontractPayment payment : supplycontract.getPayments()) {
            payment.setPayCorporate(StringUtils.isNotBlank(payment.getPayCorporate()) ? corp.get(payment.getPayCorporate()) : null);
        }
        modelAndView.addObject("supplycontract", supplycontract);
        modelAndView.setViewName(templatePath+objName+"Detail");
        return modelAndView;
    }
    /**
     * ??????
     */
    @RequestMapping("/list")
    public ModelAndView getList(ProSupplycontract supplycontract, ModelAndView modelAndView) {
        if (null != supplycontract && StringUtils.isNotBlank(supplycontract.getSignTime())) {
            String[] times = supplycontract.getSignTime().split("~");
            supplycontract.setSignTimeb(times[0].trim());
            supplycontract.setSignTimee(times[1].trim());
        }
        if (null != supplycontract && StringUtils.isNotBlank(supplycontract.getDeliveryTime())) {
            String[] times = supplycontract.getDeliveryTime().split("~");
            supplycontract.setDeliveryTimeb(times[0].trim());
            supplycontract.setDeliveryTimee(times[1].trim());
        }
        PageHelper.startPage(Integer.valueOf(1), 500);//????????????????????????????????????500???????????????????????????????????????????????????
        List<ProSupplycontract> supplycontracts = supplycontractMapper.selectByCondition(supplycontract);
        // ??????????????????
        List<Corporate> corporates = corporateMapper.selectByCondition(null);
        Map<String, String> corp = corporates.stream().collect(Collectors.toMap(Corporate::getCid, Corporate::getName));
        // ?????????????????????
        List<Supplier> suppliers = supplierMapper.selectByCondition(null);
        Map<String, String> supp = suppliers.stream().collect(Collectors.toMap(Supplier::getSid, Supplier::getName));
        // ????????????????????????
        for (ProSupplycontract supplycontract1 : supplycontracts) {
            supplycontract1.setPartya(StringUtils.isNotBlank(supplycontract1.getPartya()) ? corp.get(supplycontract1.getPartya()) : null);
            supplycontract1.setPartyb(StringUtils.isNotBlank(supplycontract1.getPartyb()) ? supp.get(supplycontract1.getPartyb()) : null);
            supplycontract1.setPayTotal(supplycontract1.getPayments().stream().map(ProSupplycontractPayment::getAmount).reduce(BigDecimal.ZERO,BigDecimal::add));
            supplycontract1.setUnpayTotal(supplycontract1.getAmount().subtract(supplycontract1.getPayTotal()));
        }

        PageInfo<ProSupplycontract> resInfo = new PageInfo<>(supplycontracts);
        modelAndView.addObject("resInfo", resInfo);
        modelAndView.setViewName(templatePath+objName+"List");
        return modelAndView;
    }
    /**
     * ????????????
     */
    @RequestMapping("/payment/list")
    public ModelAndView getPaymentList(ProSupplycontractPayment payment, ModelAndView modelAndView) {
        PageHelper.startPage(Integer.valueOf(1), 500);//????????????????????????????????????500???????????????????????????????????????????????????
        List<ProSupplycontractPayment> payments = supplycontractPaymentMapper.selectByCondition(payment);
        List<Corporate> corporates = corporateMapper.selectByCondition(null);
        Map<String, String> corp = corporates.stream().collect(Collectors.toMap(Corporate::getCid, Corporate::getName));
        for (ProSupplycontractPayment payment1 : payments) {
            payment1.setPayCorporate(StringUtils.isNotBlank(payment1.getPayCorporate()) ? corp.get(payment1.getPayCorporate()) : null);
        }
        PageInfo<ProSupplycontractPayment> resInfo = new PageInfo<>(payments);
        modelAndView.addObject("resInfo", resInfo);
        modelAndView.setViewName(templatePath+objNameSub+"List");
        return modelAndView;
    }

    /**
     * ????????????
     */
    @RequestMapping("/update")
    @ResponseBody
    public ResponseResult doUpdate(ProSupplycontract supplycontract, HttpServletRequest request) {
        if (null == supplycontract || StringUtils.isBlank(supplycontract.getOid())){
            return new ResponseResult(false, "???????????????");
        }
        ProOrder order = orderMapper.selectByPrimaryKey(supplycontract.getOid());
        if(null == order){
            return new ResponseResult(false, "???????????????");
        }
        // ??????????????????
        if(ServletFileUpload.isMultipartContent(request)) {
            MultipartHttpServletRequest multipartRequest = WebUtils.getNativeRequest(request, MultipartHttpServletRequest.class);
            List<MultipartFile> files = multipartRequest.getFiles("attachment");
            // -- ???????????????????????????????????????
            if (files.size() > 0) {
                String fileName; // ?????????
                String localName; // ???????????????
                try {
                    fileName = files.get(0).getOriginalFilename();
                    // ?????????????????????????????????????????????????????????????????????
                    if (StringUtils.isNotBlank(fileName)) {
                        localName = CommonUtils.createUUID() + fileName.substring(fileName.indexOf("."));
                        //?????????????????????
                        FileHelper.uploadSingleFile(files.get(0), localName, null);
                        supplycontract.setAnnexName(fileName);
                        supplycontract.setAnnexPath(localName);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        // ?????????
        if(ServletFileUpload.isMultipartContent(request)) {
            MultipartHttpServletRequest multipartRequest = WebUtils.getNativeRequest(request, MultipartHttpServletRequest.class);
            List<MultipartFile> files = multipartRequest.getFiles("attachmentDelivery");
            // -- ???????????????????????????????????????
            if (files.size() > 0) {
                String fileName; // ?????????
                String localName; // ???????????????
                try {
                    fileName = files.get(0).getOriginalFilename();
                    if (StringUtils.isNotBlank(fileName)) {
                        localName = CommonUtils.createUUID() + fileName.substring(fileName.indexOf("."));
                        //?????????????????????
                        FileHelper.uploadSingleFile(files.get(0), localName, null);
                        supplycontract.setDeliveryName(fileName);
                        supplycontract.setDeliveryPath(localName);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (StringUtils.isBlank(supplycontract.getScid())) {
            supplycontract.setScid(CommonUtils.createUUID());
            int res = supplycontractMapper.insertSelective(supplycontract);
            if (res <= 0) {
                return new ResponseResult(false);
            }
            sysLogComponent.writeLog(SysLogComponent.OPT_ADD, "??????????????????", JSONObject.toJSONString(supplycontract), "ProSupplycontract", request);
        } else {
            int res = supplycontractMapper.updateByPrimaryKey(supplycontract);
            if (res <= 0) {
                return new ResponseResult(false);
            }
            sysLogComponent.writeLog(SysLogComponent.OPT_UPD, "??????????????????", JSONObject.toJSONString(supplycontract), "ProSupplycontract", request);
        }
        return new ResponseResult(true, "????????????");
    }

    /**
     * ?????????????????????????????????
     * @param scid
     */
    @PostMapping("/del/{scid}")
    @ResponseBody
    public ResponseResult doDel(@PathVariable("scid") String scid, HttpServletRequest request) {
        ProSupplycontract proSupplycontract = supplycontractMapper.selectDetail(scid);
        if (null == proSupplycontract) {
            return new ResponseResult(false, "????????????????????????");
        }
        int res = supplycontractMapper.deleteByPrimaryKey(scid);
        if (res > 0) {
            sysLogComponent.writeLog(SysLogComponent.OPT_DEL, "??????????????????", JSONObject.toJSONString(proSupplycontract), "ProSupplycontract", request);
        } else {
            return new ResponseResult(true, "??????????????????????????????");
        }
        return new ResponseResult(true, "????????????");
    }

    /**
     * ????????????-??????
     */
    @RequestMapping("/payment/update")
    @ResponseBody
    public ResponseResult doPaymentUpdate(ProSupplycontractPayment payment, HttpServletRequest request) {
        if (null == payment || StringUtils.isBlank(payment.getScid())){
            return new ResponseResult(false, "???????????????");
        }
        ProSupplycontract supplycontract = supplycontractMapper.selectByPrimaryKey(payment.getScid());
        if(null == supplycontract){
            return new ResponseResult(false, "???????????????");
        }
        if(ServletFileUpload.isMultipartContent(request)) {
            MultipartHttpServletRequest multipartRequest = WebUtils.getNativeRequest(request, MultipartHttpServletRequest.class);
            List<MultipartFile> files = multipartRequest.getFiles("attachment");
            // -- ???????????????????????????????????????
            if (files.size() > 0) {
                String fileName; // ?????????
                String localName; // ???????????????
                try {
                    fileName = files.get(0).getOriginalFilename();
                    if (StringUtils.isNotBlank(fileName)) {
                        localName = CommonUtils.createUUID() + fileName.substring(fileName.indexOf("."));
                        //?????????????????????
                        FileHelper.uploadSingleFile(files.get(0), localName, null);
                        payment.setBillName(fileName);
                        payment.setBillPath(localName);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (StringUtils.isBlank(payment.getPid())) {
            payment.setPid(CommonUtils.createUUID());
            int res = supplycontractPaymentMapper.insertSelective(payment);
            if (res <= 0) {
                return new ResponseResult(false);
            }
            sysLogComponent.writeLog(SysLogComponent.OPT_ADD, "??????????????????????????????", JSONObject.toJSONString(payment), "ProSupplycontractPayment", request);
        } else {
            int res = supplycontractPaymentMapper.updateByPrimaryKey(payment);
            if (res <= 0) {
                return new ResponseResult(false);
            }
            sysLogComponent.writeLog(SysLogComponent.OPT_UPD, "??????????????????????????????", JSONObject.toJSONString(payment), "ProSupplycontractPayment", request);
        }
        return new ResponseResult(true, "????????????");
    }

    /**
     * ????????????
     * @param request
     * @param response
     */
    @RequestMapping("/downloadAnnex/{annexId}")
    public void annexDownload(@PathVariable("annexId") String annexId, HttpServletRequest request, HttpServletResponse response) {
        ProSupplycontract supplycontract = supplycontractMapper.selectByAnnexId(annexId);
        if (null == supplycontract) {
            return;
        }
        String downloadName = annexId.equals(supplycontract.getAnnexPath()) ? supplycontract.getAnnexName() : supplycontract.getDeliveryName();
        try {
            response.addHeader("content-disposition", "attachment;filename="
                    + java.net.URLEncoder.encode(downloadName, "utf-8"));
            response.setContentType("multipart/form-data");
            OutputStream out = response.getOutputStream();
            // inputStream?????????????????????????????????????????????????????????????????????
            InputStream is = new FileInputStream(FileHelper.FILE_SAVE_PATH+annexId);
            byte[] b = new byte[4096];
            int size = is.read(b);
            while (size > 0) {
                out.write(b, 0, size);
                size = is.read(b);
            }
            out.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ????????????
     * @param request
     * @param response
     */
    @RequestMapping("/payment/downloadAnnex/{annexId}")
    public void payementAnnexDownload(@PathVariable("annexId") String annexId, HttpServletRequest request, HttpServletResponse response) {
        ProSupplycontractPayment payment = supplycontractPaymentMapper.selectByAnnexId(annexId);
        if (null == payment) {
            return;
        }
        String downloadName = payment.getBillName();
        try {
            response.addHeader("content-disposition", "attachment;filename="
                    + java.net.URLEncoder.encode(downloadName, "utf-8"));
            response.setContentType("multipart/form-data");
            OutputStream out = response.getOutputStream();
            // inputStream?????????????????????????????????????????????????????????????????????
            InputStream is = new FileInputStream(FileHelper.FILE_SAVE_PATH+annexId);
            byte[] b = new byte[4096];
            int size = is.read(b);
            while (size > 0) {
                out.write(b, 0, size);
                size = is.read(b);
            }
            out.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ????????????
     */
    @RequestMapping("/payment/del/{pid}")
    @ResponseBody
    public ResponseResult doDelete(@PathVariable("pid") String pid, HttpServletRequest request) {
        ProSupplycontractPayment payment = supplycontractPaymentMapper.selectByPrimaryKey(pid);
        if (null == payment) {
            new ResponseResult(false, "?????????????????????");
        }
        int res = supplycontractPaymentMapper.deleteByPrimaryKey(pid);
        if (res > 0) {
            sysLogComponent.writeLog(SysLogComponent.OPT_DEL, "??????????????????????????????", JSONObject.toJSONString(payment), "ProSupplycontractPayment", request);
            return new ResponseResult(true);
        } else {
            return new ResponseResult(false);
        }
    }

    /**
     * ??????
     */
    @RequestMapping("/export")
    public void doExport(ProSupplycontract supplycontract,  HttpServletResponse response, HttpServletRequest request) {
        if (null != supplycontract && StringUtils.isNotBlank(supplycontract.getSignTime())) {
            String[] times = supplycontract.getSignTime().split("~");
            supplycontract.setSignTimeb(times[0].trim());
            supplycontract.setSignTimee(times[1].trim());
        }
        if (null != supplycontract && StringUtils.isNotBlank(supplycontract.getDeliveryTime())) {
            String[] times = supplycontract.getDeliveryTime().split("~");
            supplycontract.setDeliveryTimeb(times[0].trim());
            supplycontract.setDeliveryTimee(times[1].trim());
        }
        List<ProSupplycontract> supplycontracts = supplycontractMapper.selectByCondition(supplycontract);
        // ??????????????????
        List<Corporate> corporates = corporateMapper.selectByCondition(null);
        Map<String, String> corp = corporates.stream().collect(Collectors.toMap(Corporate::getCid, Corporate::getName));
        // ?????????????????????
        List<Supplier> suppliers = supplierMapper.selectByCondition(null);
        Map<String, String> supp = suppliers.stream().collect(Collectors.toMap(Supplier::getSid, Supplier::getName));
        // ????????????????????????
        for (ProSupplycontract supplycontract1 : supplycontracts) {
            supplycontract1.setPartya(StringUtils.isNotBlank(supplycontract1.getPartya()) ? corp.get(supplycontract1.getPartya()) : null);
            supplycontract1.setPartyb(StringUtils.isNotBlank(supplycontract1.getPartyb()) ? supp.get(supplycontract1.getPartyb()) : null);
            supplycontract1.setPayTotal(supplycontract1.getPayments().stream().map(ProSupplycontractPayment::getAmount).reduce(BigDecimal.ZERO,BigDecimal::add));
            supplycontract1.setUnpayTotal(supplycontract1.getAmount().subtract(supplycontract1.getPayTotal()));
        }

        List<List<Object>> datas = new ArrayList<>(); // ????????????
        List<Object> data = null; // ????????????
        String[] headers = {"??????", "?????????", "??????", "??????", "????????????", "?????????", "?????????", "?????????"};
        for (ProSupplycontract supplycontract1 : supplycontracts) {
            data = new ArrayList<>();
            data.add(supplycontract1.getSignDate());//
            data.add(supplycontract1.getCname()); //
            data.add(supplycontract1.getPartya()); //
            data.add(supplycontract1.getPartyb()); //
            data.add(supplycontract1.getAmount()); //
            data.add(supplycontract1.getPayTotal()); //
            data.add(supplycontract1.getUnpayTotal()); //
            data.add(StringUtils.isNotBlank(supplycontract1.getDeliveryPath()) ? "???" : "???"); //
            datas.add(data);
        }

        try {
            String path = ExcelUtils.writeExcel(0,"??????????????????", headers, datas);
            response.addHeader("content-disposition", "attachment;filename="
                    + java.net.URLEncoder.encode("??????????????????.xls", "utf-8"));
            OutputStream out = response.getOutputStream();
            // inputStream?????????????????????????????????????????????????????????????????????
            InputStream is = new FileInputStream(path);
            byte[] b = new byte[4096];
            int size = is.read(b);
            while (size > 0) {
                out.write(b, 0, size);
                size = is.read(b);
            }
            out.close();
            is.close();
            File file= new File(path);
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
