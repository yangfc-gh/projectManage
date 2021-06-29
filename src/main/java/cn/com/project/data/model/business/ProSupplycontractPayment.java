package cn.com.project.data.model.business;

import java.math.BigDecimal;
import java.util.Date;

public class ProSupplycontractPayment {
    private String pid;

    /**供应合同*/
    private String scid;

    /**支付金额*/
    private BigDecimal amount;

    /**支付日期*/
    private String payDate;

    /**支付方式*/
    private String payMode;

    /**支付主体*/
    private String payCorporate;

    /**票据名称*/
    private String billName;

    /**票据本地路径*/
    private String billPath;

    /**备注*/
    private String remark;

    private Date ctime;

    private Date utime;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid == null ? null : pid.trim();
    }

    public String getScid() {
        return scid;
    }

    public void setScid(String scid) {
        this.scid = scid == null ? null : scid.trim();
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

    public String getPayMode() {
        return payMode;
    }

    public void setPayMode(String payMode) {
        this.payMode = payMode == null ? null : payMode.trim();
    }

    public String getPayCorporate() {
        return payCorporate;
    }

    public void setPayCorporate(String payCorporate) {
        this.payCorporate = payCorporate == null ? null : payCorporate.trim();
    }

    public String getBillName() {
        return billName;
    }

    public void setBillName(String billName) {
        this.billName = billName == null ? null : billName.trim();
    }

    public String getBillPath() {
        return billPath;
    }

    public void setBillPath(String billPath) {
        this.billPath = billPath == null ? null : billPath.trim();
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
}