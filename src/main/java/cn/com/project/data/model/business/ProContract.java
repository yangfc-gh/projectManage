package cn.com.project.data.model.business;

import java.math.BigDecimal;
import java.util.Date;

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

    private Date ctime;

    private Date utime;

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
}