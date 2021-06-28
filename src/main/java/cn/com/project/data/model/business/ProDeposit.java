package cn.com.project.data.model.business;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 投标保证金
 */
public class ProDeposit {
    private String did;

    /**订单id*/
    private String oid;

    /**缴纳方（不一定是出钱方）*/
    private String payer;

    private String payerName;

    /**出资方（具体出钱方）*/
    private String provider;

    private String providerName;

    /**金额*/
    private BigDecimal amount;

    /**缴纳日期*/
    private String payDate;

    /**是否我方代缴*/
    private String forPay;

    /**是否收回*/
    private String isBack;

    /**收回日期*/
    private String backDate;

    /**备注*/
    private String remark;

    private Date ctime;

    private Date utime;

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did == null ? null : did.trim();
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid == null ? null : oid.trim();
    }

    public String getPayer() {
        return payer;
    }

    public void setPayer(String payer) {
        this.payer = payer == null ? null : payer.trim();
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider == null ? null : provider.trim();
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate == null ? null : payDate.trim();
    }

    public String getForPay() {
        return forPay;
    }

    public void setForPay(String forPay) {
        this.forPay = forPay == null ? null : forPay.trim();
    }

    public String getIsBack() {
        return isBack;
    }

    public void setIsBack(String isBack) {
        this.isBack = isBack == null ? null : isBack.trim();
    }

    public String getBackDate() {
        return backDate;
    }

    public void setBackDate(String backDate) {
        this.backDate = backDate == null ? null : backDate.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
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

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }
}