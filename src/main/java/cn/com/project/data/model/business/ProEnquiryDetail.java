package cn.com.project.data.model.business;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 询价单明细
 */
public class ProEnquiryDetail {
    private String did;

    /**询价*/
    private String eid;

    /**产品名称*/
    private String pname;

    /**物料编码*/
    private String pcode;

    /**规格型号*/
    private String model;

    /**单位*/
    private String unit;

    /**数量*/
    private BigDecimal quantity;

    /**单价*/
    private BigDecimal price;

    /**总价*/
    private BigDecimal total;

    /**备注*/
    private String remark;

    private Date ctime;

    private Date utime;

    private ProEnquiry enquiry;

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did == null ? null : did.trim();
    }

    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid == null ? null : eid.trim();
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname == null ? null : pname.trim();
    }

    public String getPcode() {
        return pcode;
    }

    public void setPcode(String pcode) {
        this.pcode = pcode == null ? null : pcode.trim();
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model == null ? null : model.trim();
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit == null ? null : unit.trim();
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
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

    public ProEnquiry getEnquiry() {
        return enquiry;
    }

    public void setEnquiry(ProEnquiry enquiry) {
        this.enquiry = enquiry;
    }
}