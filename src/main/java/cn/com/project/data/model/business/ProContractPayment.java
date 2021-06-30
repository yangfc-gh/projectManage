package cn.com.project.data.model.business;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 项目合同款项信息
 */
public class ProContractPayment {
    private String pid;

    /**合同id*/
    private String cid;

    /**款项名称*/
    private String payName;

    /**金额*/
    private BigDecimal amount;

    /**预计日期*/
    private String expectedDate;

    /**款项备注*/
    private String expectedRemark;

    /**回款金额*/
    private BigDecimal payAmount;

    /**回款日期*/
    private String payDate;

    /**票据名称*/
    private String billName;

    /**本地路径*/
    private String billPath;

    /**备注*/
    private String remark;

    /**状态0未回1已回*/
    private String status;

    private Date ctime;

    private Date utime;

    /**预计付款时间起止-查询用*/
    private String stime;
    private String stimeb;
    private String stimee;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid == null ? null : pid.trim();
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid == null ? null : cid.trim();
    }

    public String getPayName() {
        return payName;
    }

    public void setPayName(String payName) {
        this.payName = payName == null ? null : payName.trim();
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getExpectedDate() {
        return expectedDate;
    }

    public void setExpectedDate(String expectedDate) {
        this.expectedDate = expectedDate == null ? null : expectedDate.trim();
    }

    public String getExpectedRemark() {
        return expectedRemark;
    }

    public void setExpectedRemark(String expectedRemark) {
        this.expectedRemark = expectedRemark == null ? null : expectedRemark.trim();
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate == null ? null : payDate.trim();
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
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

    public String getStime() {
        return stime;
    }

    public void setStime(String stime) {
        this.stime = stime;
    }

    public String getStimeb() {
        return stimeb;
    }

    public void setStimeb(String stimeb) {
        this.stimeb = stimeb;
    }

    public String getStimee() {
        return stimee;
    }

    public void setStimee(String stimee) {
        this.stimee = stimee;
    }
}