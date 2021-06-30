package cn.com.project.data.model.business;

import java.util.Date;

/**
 * 报价单
 */
public class ProQuotation {
    private String qid;

    /**订单*/
    private String oid;

    /**报价单（给客户）*/
    private String customerAnnexName;

    /**报价单存放路径*/
    private String customerAnnexPath;

    /**报价明细（自留，包含供应商信息）*/
    private String selfAnnexName;

    /**报价明细存放路径*/
    private String selfAnnexPath;

    /**备注*/
    private String remark;

    private Date ctime;

    private Date utime;

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid == null ? null : qid.trim();
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid == null ? null : oid.trim();
    }

    public String getCustomerAnnexName() {
        return customerAnnexName;
    }

    public void setCustomerAnnexName(String customerAnnexName) {
        this.customerAnnexName = customerAnnexName == null ? null : customerAnnexName.trim();
    }

    public String getCustomerAnnexPath() {
        return customerAnnexPath;
    }

    public void setCustomerAnnexPath(String customerAnnexPath) {
        this.customerAnnexPath = customerAnnexPath == null ? null : customerAnnexPath.trim();
    }

    public String getSelfAnnexName() {
        return selfAnnexName;
    }

    public void setSelfAnnexName(String selfAnnexName) {
        this.selfAnnexName = selfAnnexName == null ? null : selfAnnexName.trim();
    }

    public String getSelfAnnexPath() {
        return selfAnnexPath;
    }

    public void setSelfAnnexPath(String selfAnnexPath) {
        this.selfAnnexPath = selfAnnexPath == null ? null : selfAnnexPath.trim();
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