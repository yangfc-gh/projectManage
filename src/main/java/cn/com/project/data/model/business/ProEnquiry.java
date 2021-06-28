package cn.com.project.data.model.business;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 询价单
 */
public class ProEnquiry {
    private String eid;

    /**订单*/
    private String oid;

    /**供应商*/
    private String supplier;

    /**总价*/
    private BigDecimal total;

    /**备注*/
    private String remark;

    private Date ctime;

    private Date utime;

    private String supplierName;

    private List<ProEnquiryDetail> enquiryDetails;

    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid == null ? null : eid.trim();
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid == null ? null : oid.trim();
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier == null ? null : supplier.trim();
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
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

    public List<ProEnquiryDetail> getEnquiryDetails() {
        return enquiryDetails;
    }

    public void setEnquiryDetails(List<ProEnquiryDetail> enquiryDetails) {
        this.enquiryDetails = enquiryDetails;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }
}