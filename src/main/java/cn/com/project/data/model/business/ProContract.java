package cn.com.project.data.model.business;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 合同信息
 */
public class ProContract {
    private String cid;

    /**订单*/
    private String oid;

    /**合同编号*/
    private String cno;

    /**合同名称*/
    private String cname;

    /**工程名称*/
    private String pname;

    /**合同金额*/
    private BigDecimal amount;

    /** 利润 */
    private BigDecimal profit;

    /**甲方*/
    private String partya;

    /**乙方*/
    private String partyb;

    /**使用方*/
    private String partyu;

    /**执行方*/
    private String partyz;

    /**乙方和执行方合约金*/
    private String treatybz;

    /**签订日期*/
    private String signDate;

    /**交付日期*/
    private String deliveryDate;

    /**概述*/
    private String description;

    /**备注*/
    private String remark;

    /**发票日期*/
    private String invoiceDate;

    /**发票金额*/
    private BigDecimal invoiceAmount;

    /**合同电子档*/
    private String annexName;

    /**本地名称*/
    private String annexPath;

    private String status;

    private String isInvoice;

    private Date ctime;

    private Date utime;

    /** 查询用-签订日期 */
    private String signTime;
    private String signTimeb;
    private String signTimee;
    /** 查询用-交付日期 */
    private String deliveryTime;
    private String deliveryTimeb;
    private String deliveryTimee;
    /** 查询用-下次回款日期 */
    private String expectedTime;
    private String expectedTimeb;
    private String expectedTimee;

    /** 显示用-下次回款日期 */
    private String nextStep;
    private String nextDate;
    private String process;

    private List<ProContractPayment> payments;
    private List<ProContractExpend> expends;
    private List<ProSupplycontract> supplycontracts;

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid == null ? null : cid.trim();
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid == null ? null : oid.trim();
    }

    public String getCno() {
        return cno;
    }

    public void setCno(String cno) {
        this.cno = cno == null ? null : cno.trim();
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname == null ? null : cname.trim();
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname == null ? null : pname.trim();
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPartya() {
        return partya;
    }

    public void setPartya(String partya) {
        this.partya = partya == null ? null : partya.trim();
    }

    public String getPartyb() {
        return partyb;
    }

    public void setPartyb(String partyb) {
        this.partyb = partyb == null ? null : partyb.trim();
    }

    public String getPartyu() {
        return partyu;
    }

    public void setPartyu(String partyu) {
        this.partyu = partyu == null ? null : partyu.trim();
    }

    public String getPartyz() {
        return partyz;
    }

    public void setPartyz(String partyz) {
        this.partyz = partyz == null ? null : partyz.trim();
    }

    public String getTreatybz() {
        return treatybz;
    }

    public void setTreatybz(String treatybz) {
        this.treatybz = treatybz == null ? null : treatybz.trim();
    }

    public String getSignDate() {
        return signDate;
    }

    public void setSignDate(String signDate) {
        this.signDate = signDate == null ? null : signDate.trim();
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate == null ? null : deliveryDate.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate == null ? null : invoiceDate.trim();
    }

    public BigDecimal getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(BigDecimal invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public String getAnnexName() {
        return annexName;
    }

    public void setAnnexName(String annexName) {
        this.annexName = annexName == null ? null : annexName.trim();
    }

    public String getAnnexPath() {
        return annexPath;
    }

    public void setAnnexPath(String annexPath) {
        this.annexPath = annexPath == null ? null : annexPath.trim();
    }

    public Date getCtime() {
        return ctime;
    }

    public void setCtime(Date ctime) {
        this.ctime = ctime;
    }

    public Date getUtime() {
        return utime;
    }

    public void setUtime(Date utime) {
        this.utime = utime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public List<ProContractPayment> getPayments() {
        return payments;
    }

    public void setPayments(List<ProContractPayment> payments) {
        this.payments = payments;
    }

    public String getIsInvoice() {
        return isInvoice;
    }

    public void setIsInvoice(String isInvoice) {
        this.isInvoice = isInvoice == null ? null : isInvoice.trim();
    }

    public String getSignTime() {
        return signTime;
    }

    public void setSignTime(String signTime) {
        this.signTime = signTime == null ? null : signTime.trim();
    }

    public String getSignTimeb() {
        return signTimeb;
    }

    public void setSignTimeb(String signTimeb) {
        this.signTimeb = signTimeb == null ? null : signTimeb.trim();
    }

    public String getSignTimee() {
        return signTimee;
    }

    public void setSignTimee(String signTimee) {
        this.signTimee = signTimee == null ? null : signTimee.trim();
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime == null ? null : deliveryTime.trim();
    }

    public String getDeliveryTimeb() {
        return deliveryTimeb;
    }

    public void setDeliveryTimeb(String deliveryTimeb) {
        this.deliveryTimeb = deliveryTimeb == null ? null : deliveryTimeb.trim();
    }

    public String getDeliveryTimee() {
        return deliveryTimee;
    }

    public void setDeliveryTimee(String deliveryTimee) {
        this.deliveryTimee = deliveryTimee == null ? null : deliveryTimee.trim();
    }

    public String getExpectedTime() {
        return expectedTime;
    }

    public void setExpectedTime(String expectedTime) {
        this.expectedTime = expectedTime == null ? null : expectedTime.trim();
    }

    public String getExpectedTimeb() {
        return expectedTimeb;
    }

    public void setExpectedTimeb(String expectedTimeb) {
        this.expectedTimeb = expectedTimeb == null ? null : expectedTimeb.trim();
    }

    public String getExpectedTimee() {
        return expectedTimee;
    }

    public void setExpectedTimee(String expectedTimee) {
        this.expectedTimee = expectedTimee == null ? null : expectedTimee.trim();
    }

    public String getNextStep() {
        return nextStep;
    }

    public void setNextStep(String nextStep) {
        this.nextStep = nextStep;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getNextDate() {
        return nextDate;
    }

    public void setNextDate(String nextDate) {
        this.nextDate = nextDate;
    }

    public List<ProContractExpend> getExpends() {
        return expends;
    }

    public void setExpends(List<ProContractExpend> expends) {
        this.expends = expends;
    }

    public List<ProSupplycontract> getSupplycontracts() {
        return supplycontracts;
    }

    public void setSupplycontracts(List<ProSupplycontract> supplycontracts) {
        this.supplycontracts = supplycontracts;
    }

    public BigDecimal getProfit() {
        return profit;
    }

    public void setProfit(BigDecimal profit) {
        this.profit = profit;
    }
}