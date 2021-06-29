package cn.com.project.modules.order;

import cn.com.project.common.CommonUtils;
import cn.com.project.common.FileHelper;
import cn.com.project.common.ResponseResult;
import cn.com.project.data.dao.business.ProOrderMapper;
import cn.com.project.data.dao.business.ProSupplycontractMapper;
import cn.com.project.data.dao.business.ProSupplycontractPaymentMapper;
import cn.com.project.data.dao.obj.CorporateMapper;
import cn.com.project.data.dao.obj.SupplierMapper;
import cn.com.project.data.model.business.*;
import cn.com.project.data.model.obj.Corporate;
import cn.com.project.data.model.obj.Supplier;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
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

    private final String templatePath = "supplycontract/";
    private final String objName = "supplyContract";
    private final String objNameSub = "supplyContractPayment";

    /**
     * 去编辑
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
        corporates = corporates.stream().filter(c -> "1".equals(c.getStatus())).collect(Collectors.toList());
        modelAndView.addObject("corporates", corporates);
        List<Supplier> suppliers = supplierMapper.selectByCondition(null);
        suppliers = suppliers.stream().filter(s -> "1".equals(s.getStatus())).collect(Collectors.toList());
        modelAndView.addObject("suppliers", suppliers);
        modelAndView.setViewName(templatePath+objName+"Edit");
        return modelAndView;
    }

    /**
     * 去编辑
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
        corporates = corporates.stream().filter(c -> "1".equals(c.getStatus())).collect(Collectors.toList());
        modelAndView.addObject("corporates", corporates);
        modelAndView.addObject("payment", payment);
        modelAndView.setViewName(templatePath+objNameSub+"Edit");
        return modelAndView;
    }

    /**
     * 编辑更新
     */
    @RequestMapping("/update")
    @ResponseBody
    public ResponseResult doUpdate(ProSupplycontract supplycontract, HttpServletRequest request) {
        if (null == supplycontract || StringUtils.isBlank(supplycontract.getOid())){
            return new ResponseResult(false, "关键信息空");
        }
        ProOrder order = orderMapper.selectByPrimaryKey(supplycontract.getOid());
        if(null == order){
            return new ResponseResult(false, "未找到订单");
        }
        // 供应合同附件
        if(ServletFileUpload.isMultipartContent(request)) {
            MultipartHttpServletRequest multipartRequest = WebUtils.getNativeRequest(request, MultipartHttpServletRequest.class);
            List<MultipartFile> files = multipartRequest.getFiles("attachment");
            // -- 这一步是处理给客户的报价单
            if (files.size() > 0) {
                String fileName; // 文件名
                String localName; // 本地存储名
                try {
                    fileName = files.get(0).getOriginalFilename();
                    // 比较奇怪，不选择文件也会有一个空”“的提交上来
                    if (StringUtils.isNotBlank(fileName)) {
                        localName = CommonUtils.createUUID() + fileName.substring(fileName.indexOf("."));
                        //文件存储到本地
                        FileHelper.uploadSingleFile(files.get(0), localName, null);
                        supplycontract.setAnnexName(fileName);
                        supplycontract.setAnnexPath(localName);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        // 送货单
        if(ServletFileUpload.isMultipartContent(request)) {
            MultipartHttpServletRequest multipartRequest = WebUtils.getNativeRequest(request, MultipartHttpServletRequest.class);
            List<MultipartFile> files = multipartRequest.getFiles("attachmentDelivery");
            // -- 这一步是处理给客户的报价单
            if (files.size() > 0) {
                String fileName; // 文件名
                String localName; // 本地存储名
                try {
                    fileName = files.get(0).getOriginalFilename();
                    if (StringUtils.isNotBlank(fileName)) {
                        localName = CommonUtils.createUUID() + fileName.substring(fileName.indexOf("."));
                        //文件存储到本地
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
        } else {
            int res = supplycontractMapper.updateByPrimaryKey(supplycontract);
            if (res <= 0) {
                return new ResponseResult(false);
            }
        }
        return new ResponseResult(true, "操作成功");
    }

    /**
     * 编辑更新-款项
     */
    @RequestMapping("/payment/update")
    @ResponseBody
    public ResponseResult doPaymentUpdate(ProSupplycontractPayment payment, HttpServletRequest request) {
        if (null == payment || StringUtils.isBlank(payment.getScid())){
            return new ResponseResult(false, "关键信息空");
        }
        ProSupplycontract supplycontract = supplycontractMapper.selectByPrimaryKey(payment.getScid());
        if(null == supplycontract){
            return new ResponseResult(false, "未找到合同");
        }
        if(ServletFileUpload.isMultipartContent(request)) {
            MultipartHttpServletRequest multipartRequest = WebUtils.getNativeRequest(request, MultipartHttpServletRequest.class);
            List<MultipartFile> files = multipartRequest.getFiles("attachment");
            // -- 这一步是处理给客户的报价单
            if (files.size() > 0) {
                String fileName; // 文件名
                String localName; // 本地存储名
                try {
                    fileName = files.get(0).getOriginalFilename();
                    if (StringUtils.isNotBlank(fileName)) {
                        localName = CommonUtils.createUUID() + fileName.substring(fileName.indexOf("."));
                        //文件存储到本地
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
        } else {
            int res = supplycontractPaymentMapper.updateByPrimaryKey(payment);
            if (res <= 0) {
                return new ResponseResult(false);
            }
        }
        return new ResponseResult(true, "操作成功");
    }

    /**
     * 下载附件
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
            // inputStream：读文件，前提是这个文件必须存在，要不就会报错
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
     * 下载附件
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
            // inputStream：读文件，前提是这个文件必须存在，要不就会报错
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
}
