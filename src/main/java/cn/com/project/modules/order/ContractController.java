package cn.com.project.modules.order;

import cn.com.project.common.CommonUtils;
import cn.com.project.common.FileHelper;
import cn.com.project.common.ResponseResult;
import cn.com.project.common.SysLogComponent;
import cn.com.project.data.dao.business.ProContractMapper;
import cn.com.project.data.dao.business.ProContractPaymentMapper;
import cn.com.project.data.dao.business.ProOrderMapper;
import cn.com.project.data.dao.obj.CorporateMapper;
import cn.com.project.data.dao.obj.CustomerMapper;
import cn.com.project.data.dao.sys.SysDictsMapper;
import cn.com.project.data.model.business.*;
import cn.com.project.data.model.obj.Corporate;
import cn.com.project.data.model.obj.Customer;
import cn.com.project.data.model.sys.SysDicts;
import cn.com.project.modules.order.service.OrderService;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 合同管理
 */
@CrossOrigin
@RequestMapping("contract")
@Controller
public class ContractController {
    private Logger logger = LoggerFactory.getLogger(ContractController.class);

    @Autowired
    ProContractMapper contractMapper;
    @Autowired
    ProContractPaymentMapper contractPaymentMapper;
    @Autowired
    ProOrderMapper orderMapper;
    @Autowired
    CorporateMapper corporateMapper;
    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    SysDictsMapper sysDictsMapper;
    @Autowired
    OrderService orderService;
    @Autowired
    SysLogComponent sysLogComponent;

    private final String templatePath = "contract/";
    private final String objName = "contract";
    private final String objNameSub = "contractPayment";
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
        modelAndView.setViewName(templatePath+"contractIndex");
        return modelAndView;
    }
    /**
     * 去编辑
     */
    @RequestMapping("/toEdit")
    public ModelAndView toEdit(ModelAndView modelAndView, HttpServletRequest request) {
        String oid = request.getParameter("oid");
        ProContract contract = contractMapper.selectByOid(oid);
        if (null == contract) {
            contract = new ProContract();
            contract.setOid(oid);
        }
        modelAndView.addObject("contract", contract);
        List<Corporate> corporates = corporateMapper.selectByCondition(null);
        corporates = corporates.stream().filter(c -> "1".equals(c.getStatus())).collect(Collectors.toList());
        modelAndView.addObject("corporates", corporates);
        List<Customer> customers = customerMapper.selectByCondition(null);
        customers = customers.stream().filter(c -> "1".equals(c.getStatus())).collect(Collectors.toList());
        modelAndView.addObject("customers", customers);
        modelAndView.setViewName(templatePath+objName+"Edit");
        return modelAndView;
    }

    /**
     * 跳转管理首页
     */
    @RequestMapping("/detail/{cid}")
    public ModelAndView toDetail(@PathVariable("cid") String cid, ModelAndView modelAndView) {
        ProContract contract = contractMapper.selectDetail(cid);
        // 全部主体信息
        List<Corporate> corporates = corporateMapper.selectByCondition(null);
        Map<String, String> corp = corporates.stream().collect(Collectors.toMap(Corporate::getCid, Corporate::getName));
        // 全部客户信息
        List<Customer> customers = customerMapper.selectByCondition(null);
        Map<String, String> cust = customers.stream().collect(Collectors.toMap(Customer::getCid, Customer::getName));

        contract.setPartya(StringUtils.isNotBlank(contract.getPartya()) ? cust.get(contract.getPartya()) : null);
        contract.setPartyb(StringUtils.isNotBlank(contract.getPartyb()) ? corp.get(contract.getPartyb()) : null);
        contract.setPartyu(StringUtils.isNotBlank(contract.getPartyu()) ? cust.get(contract.getPartyu()) : null);
        contract.setPartyz(StringUtils.isNotBlank(contract.getPartyz()) ? corp.get(contract.getPartyz()) : null);
        modelAndView.addObject("contract", contract);
        modelAndView.setViewName(templatePath+objName+"Detail");
        return modelAndView;
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public ModelAndView getList(ProContract contract, ModelAndView modelAndView) {
        if (null != contract && StringUtils.isNotBlank(contract.getSignTime())) {
            String[] times = contract.getSignTime().split("~");
            contract.setSignTimeb(times[0].trim());
            contract.setSignTimee(times[1].trim());
        }
        if (null != contract && StringUtils.isNotBlank(contract.getDeliveryTime())) {
            String[] times = contract.getDeliveryTime().split("~");
            contract.setDeliveryTimeb(times[0].trim());
            contract.setDeliveryTimee(times[1].trim());
        }
        if (null != contract && StringUtils.isNotBlank(contract.getExpectedTime())) {
            String[] times = contract.getExpectedTime().split("~");
            contract.setExpectedTimeb(times[0].trim());
            contract.setExpectedTimee(times[1].trim());
        }
        PageHelper.startPage(Integer.valueOf(1), 500);//不分页，一页默认最多展示500条，在这使用分页的目的是获取总行数
        List<ProContract> contracts = contractMapper.selectByCondition(contract);
        // 全部主体信息
        List<Corporate> corporates = corporateMapper.selectByCondition(null);
        Map<String, String> corp = corporates.stream().collect(Collectors.toMap(Corporate::getCid, Corporate::getName));
        // 全部客户信息
        List<Customer> customers = customerMapper.selectByCondition(null);
        Map<String, String> cust = customers.stream().collect(Collectors.toMap(Customer::getCid, Customer::getName));
        // 翻译一下甲方乙方
        for (ProContract proContract : contracts) {
            proContract.setPartya(StringUtils.isNotBlank(proContract.getPartya()) ? cust.get(proContract.getPartya()) : null);
            proContract.setPartyb(StringUtils.isNotBlank(proContract.getPartyb()) ? corp.get(proContract.getPartyb()) : null);
            if (CollectionUtils.isNotEmpty(proContract.getPayments())) {
                long total = proContract.getPayments().size();
                long process = proContract.getPayments().stream().filter(p -> null != p.getPayAmount() && StringUtils.isNotBlank(p.getPayDate())).count();
                proContract.setProcess(process+"/"+total); //进度
                for (ProContractPayment payment : proContract.getPayments()) {
                    if (null == payment.getPayAmount() && StringUtils.isBlank(payment.getPayDate())) {
                        proContract.setNextStep(payment.getPayName());
                        proContract.setNextDate(payment.getExpectedDate());
                        break;
                    }
                }
            } else {
                proContract.setProcess("0/0"); //进度
            }
        }

        PageInfo<ProContract> resInfo = new PageInfo<>(contracts);
        modelAndView.addObject("resInfo", resInfo);
        modelAndView.setViewName(templatePath+objName+"List");
        return modelAndView;
    }

    /**
     * 合同利润计算
     */
    @RequestMapping("/profit/{cid}")
    public ModelAndView profitStatistics(@PathVariable("cid") String cid, ModelAndView modelAndView) {
        ProContract contract = contractMapper.select4Profit(cid);
        List<ExpendDetail> expendDetails = new ArrayList<>();
        ExpendDetail expendDetail = null;
        // 合约金算作支出，累计一下
        if (StringUtils.isNotBlank(contract.getTreatybz())) {
            // 如果填写是百分比
            if ("%".indexOf(contract.getTreatybz()) > 0) {
                // 去除%号和空格后，如果还是数字
                String treat = contract.getTreatybz().replaceAll("%", "").replaceAll(" ", "");
                if (StringUtils.isNumeric(treat)) {
                    BigDecimal divide = contract.getAmount().multiply(new BigDecimal(treat)).divide(BigDecimal.valueOf(100));
                    expendDetail = new ExpendDetail();
                    expendDetail.setEtype("合约金");
                    expendDetail.setEname("友方合同合约金");
                    expendDetail.setAmount(divide);
                    expendDetail.setDescription("友方公司签订转交合同时支付的合约金");
                    expendDetails.add(expendDetail);
                }
            } else if (StringUtils.isNumeric(contract.getTreatybz().replaceAll(" ", ""))) {
                expendDetail = new ExpendDetail();
                expendDetail.setEtype("合约金");
                expendDetail.setEname("友方合同合约金");
                expendDetail.setAmount(new BigDecimal(contract.getTreatybz().replaceAll(" ", "")));
                expendDetail.setDescription("友方公司签订转交合同时支付的合约金");
                expendDetails.add(expendDetail);
            }
        }
        // 供应合同算作支出，累计一下
        for (ProSupplycontract supplycontract : contract.getSupplycontracts()) {
            if (null != supplycontract.getAmount() && supplycontract.getAmount().compareTo(BigDecimal.ZERO) > 0) {
                expendDetail = new ExpendDetail();
                expendDetail.setEtype("供应合同");
                expendDetail.setEname(supplycontract.getCname());
                expendDetail.setAmount(supplycontract.getAmount());
                expendDetails.add(expendDetail);
            }
        }
        // 支出款项，累计下
        for (ProContractExpend expend : contract.getExpends()) {
            if (null != expend.getAmount() && expend.getAmount().compareTo(BigDecimal.ZERO) > 0) {
                expendDetail = new ExpendDetail();
                expendDetail.setEtype("支出费用");
                expendDetail.setEname(expend.getEname());
                expendDetail.setAmount(expend.getAmount());
                expendDetail.setDescription(expend.getRemark());
                expendDetails.add(expendDetail);
            }
        }
        BigDecimal expendTotal = expendDetails.stream().map(ExpendDetail::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        if (null != expendTotal && expendTotal.compareTo(BigDecimal.ZERO) > 0) {
            contract.setProfit(contract.getAmount().subtract(expendTotal));
            modelAndView.addObject("expendTotal", expendTotal); // 总支出
            modelAndView.addObject("profit", contract.getAmount().subtract(expendTotal)); // 净利润
        }
        modelAndView.addObject("contractAmount", contract.getAmount()); // 合同金额
        modelAndView.addObject("expendDetails", expendDetails);
        modelAndView.setViewName(templatePath+"profitInfo");
        return modelAndView;
    }

    /**
     * 去编辑
     */
    @RequestMapping("/payment/toEdit")
    public ModelAndView toPaymentEdit(ModelAndView modelAndView, HttpServletRequest request) {
        String cid = request.getParameter("cid");
        String pid = request.getParameter("pid");
        ProContractPayment payment = null;
        if (StringUtils.isNotBlank(pid)) {
            payment = contractPaymentMapper.selectByPrimaryKey(pid);
        } else {
            payment = new ProContractPayment();
            payment.setCid(cid);
        }
        modelAndView.addObject("payment", payment);
        modelAndView.setViewName(templatePath+objNameSub+"Edit");
        return modelAndView;
    }

    /**
     * 款项列表
     */
    @RequestMapping("/payment/list")
    public ModelAndView getList(ProContractPayment payment, ModelAndView modelAndView) {
        PageHelper.startPage(Integer.valueOf(1), 500);//不分页，一页默认最多展示500条，在这使用分页的目的是获取总行数
        List<ProContractPayment> payments = contractPaymentMapper.selectByCondition(payment);
        PageInfo<ProContractPayment> resInfo = new PageInfo<>(payments);
        modelAndView.addObject("resInfo", resInfo);
        modelAndView.setViewName(templatePath+objNameSub+"List");
        return modelAndView;
    }
    /**
     * 编辑更新
     */
    @RequestMapping("/update")
    @ResponseBody
    public ResponseResult doUpdate(ProContract contract, HttpServletRequest request) {
        if (null == contract || StringUtils.isBlank(contract.getOid())){
            return new ResponseResult(false, "关键信息空");
        }
        ProOrder order = orderMapper.selectByPrimaryKey(contract.getOid());
        if(null == order){
            return new ResponseResult(false, "未找到订单");
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
                        contract.setAnnexName(fileName);
                        contract.setAnnexPath(localName);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (StringUtils.isBlank(contract.getCid())) {
            contract.setCid(CommonUtils.createUUID());
            int res = contractMapper.insertSelective(contract);
            if (res <= 0) {
                return new ResponseResult(false);
            }
            // 订单状态切换为”已签合同“
            orderService.orderState(contract.getOid(), "DDZT_QDHT");
            sysLogComponent.writeLog(SysLogComponent.OPT_ADD, "新增合同", JSONObject.toJSONString(contract), "ProContract", request);
        } else {
            int res = contractMapper.updateByPrimaryKey(contract);
            if (res <= 0) {
                return new ResponseResult(false);
            }
            sysLogComponent.writeLog(SysLogComponent.OPT_UPD, "修改合同", JSONObject.toJSONString(contract), "ProContract", request);
        }
        return new ResponseResult(true, "操作成功");
    }

    /**
     * 编辑更新-款项
     */
    @RequestMapping("/payment/update")
    @ResponseBody
    public ResponseResult doPaymentUpdate(ProContractPayment payment, HttpServletRequest request) {
        if (null == payment || StringUtils.isBlank(payment.getCid())){
            return new ResponseResult(false, "关键信息空");
        }
        ProContract contract = contractMapper.selectByPrimaryKey(payment.getCid());
        if(null == contract){
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
            int res = contractPaymentMapper.insertSelective(payment);
            if (res <= 0) {
                return new ResponseResult(false);
            }
            sysLogComponent.writeLog(SysLogComponent.OPT_ADD, "新增合同款项", JSONObject.toJSONString(payment), "ProContractPayment", request);
        } else {
            int res = contractPaymentMapper.updateByPrimaryKey(payment);
            if (res <= 0) {
                return new ResponseResult(false);
            }
            sysLogComponent.writeLog(SysLogComponent.OPT_UPD, "修改合同款项", JSONObject.toJSONString(payment), "ProContractPayment", request);
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
        ProContract contract = contractMapper.selectByAnnexId(annexId);
        if (null == contract) {
            return;
        }
        String downloadName = contract.getAnnexName();
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
        ProContractPayment payment = contractPaymentMapper.selectByAnnexId(annexId);
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

    /**
     * 删除款项
     */
    @RequestMapping("/payment/del/{pid}")
    @ResponseBody
    public ResponseResult doDelete(@PathVariable("pid") String pid, HttpServletRequest request) {
        ProContractPayment payment = contractPaymentMapper.selectByPrimaryKey(pid);
        if (null == payment) {
            new ResponseResult(false, "未找到款项信息");
        }
        int res = contractPaymentMapper.deleteByPrimaryKey(pid);
        if (res > 0) {
            sysLogComponent.writeLog(SysLogComponent.OPT_DEL, "删除合同款项", JSONObject.toJSONString(payment), "ProContractPayment", request);
            return new ResponseResult(true);
        } else {
            return new ResponseResult(false);
        }
    }
}
