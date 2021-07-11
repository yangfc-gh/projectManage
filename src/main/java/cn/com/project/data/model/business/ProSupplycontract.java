package cn.com.project.data.model.business;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 供应合同
 */
public class ProSupplycontract {
    private String scid;

    private String oid;

    /**合同号*/
    private String cno;

    /**合同名称*/
    private String cname;

    /**合同金额*/
    private BigDecimal amount;

    /**甲方(我方主体)*/
    private String partya;

    /**乙方(供应商)*/
    private String partyb;

    /**签订日期*/
    private String signDate;

    /**交付日期*/
    private String deliveryDate;

    /**概述*/
    private String description;

    /**备注*/
    private String remark;

    /**状态*/
    private String status;

    /**合同附件*/
    private String annexName;

    /**合同附件本地路径*/
    private String annexPath;

    /**送货单*/
    private String deliveryName;

    /**送货单本地路径*/
    private String deliveryPath;

    private Date ctime;

    private Date utime;

    private String isPayoff;

    /** 查询用-签订日期 */
    private String signTime;
    private String signTimeb;
    private String signTimee;
    /** 查询用-交付日期 */
    private String deliveryTime;
    private String deliveryTimeb;
    private String deliveryTimee;
    /** 查询用-交付日期 */
    private String sendState; // 是否送货单
    private String payoffState; // 是否付清

    private BigDecimal payTotal; // 已支付合计
    private BigDecimal unpayTotal; // 未支付合计

    private List<ProSupplycontractPayment> payments;

    public String getScid() {
        return scid;
    }

    public void setScid(String scid) {
        this.scid = scid == null ? null : scid.trim();
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
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

    public String getDeliveryName() {
        return deliveryName;
    }

    public void setDeliveryName(String deliveryName) {
        this.deliveryName = deliveryName == null ? null : deliveryName.trim();
    }

    public String getDeliveryPath() {
        return deliveryPath;
    }

    public void setDeliveryPath(String deliveryPath) {
        this.deliveryPath = deliveryPath == null ? null : deliveryPath.trim();
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

    public List<ProSupplycontractPayment> getPayments() {
        return payments;
    }

    public void setPayments(List<ProSupplycontractPayment> payments) {
        this.payments = payments;
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

    public String getSendState() {
        return sendState;
    }

    public void setSendState(String sendState) {
        this.sendState = sendState;
    }

    public String getPayoffState() {
        return payoffState;
    }

    public void setPayoffState(String payoffState) {
        this.payoffState = payoffState;
    }

    public String getIsPayoff() {
        return isPayoff;
    }

    public void setIsPayoff(String isPayoff) {
        this.isPayoff = isPayoff;
    }

    public BigDecimal getPayTotal() {
        return payTotal;
    }

    public void setPayTotal(BigDecimal payTotal) {
        this.payTotal = payTotal;
    }

    public BigDecimal getUnpayTotal() {
        return unpayTotal;
    }

    public void setUnpayTotal(BigDecimal unpayTotal) {
        this.unpayTotal = unpayTotal;
    }
}