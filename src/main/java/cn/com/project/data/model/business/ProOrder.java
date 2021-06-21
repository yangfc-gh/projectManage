package cn.com.project.data.model.business;

import java.math.BigDecimal;
import java.util.Date;

public class ProOrder {
    private String oid;

    /**订单号*/
    private String ono;

    /**客户*/
    private String customerId;

    /**区域*/
    private String area;

    /**项目名称*/
    private String projectName;

    /**状态0新1询价2报价3合同4结*/
    private String status;

    /**备注描述*/
    private String remark;

    /**中标方*/
    private String bidderZ;

    /**中标金额*/
    private BigDecimal bidAmount;

    /**报价明细*/
    private String bidDetail;

    /**登记人*/
    private String creater;

    private Date ctime;

    private Date utime;

    /**订单创建时间起止-查询用*/
    private String otime;
    private String otimeb;
    private String otimee;

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid == null ? null : oid.trim();
    }

    public String getOno() {
        return ono;
    }

    public void setOno(String ono) {
        this.ono = ono == null ? null : ono.trim();
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId == null ? null : customerId.trim();
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area == null ? null : area.trim();
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName == null ? null : projectName.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getBidderZ() {
        return bidderZ;
    }

    public void setBidderZ(String bidderZ) {
        this.bidderZ = bidderZ == null ? null : bidderZ.trim();
    }

    public BigDecimal getBidAmount() {
        return bidAmount;
    }

    public void setBidAmount(BigDecimal bidAmount) {
        this.bidAmount = bidAmount;
    }

    public String getBidDetail() {
        return bidDetail;
    }

    public void setBidDetail(String bidDetail) {
        this.bidDetail = bidDetail == null ? null : bidDetail.trim();
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater == null ? null : creater.trim();
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

    public String getOtime() {
        return otime;
    }

    public void setOtime(String otime) {
        this.otime = otime;
    }

    public String getOtimeb() {
        return otimeb;
    }

    public void setOtimeb(String otimeb) {
        this.otimeb = otimeb;
    }

    public String getOtimee() {
        return otimee;
    }

    public void setOtimee(String otimee) {
        this.otimee = otimee;
    }
}