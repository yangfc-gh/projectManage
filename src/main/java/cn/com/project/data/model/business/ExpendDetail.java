package cn.com.project.data.model.business;

import java.math.BigDecimal;

/**
 * 支出费用明细（利润计算用）
 */
public class ExpendDetail {
    /** 费用类型 */
    private String etype;
    /** 费用名称 */
    private String ename;
    /** 费用金额 */
    private BigDecimal amount;
    /** 费用合计（暂时没用） */
    private BigDecimal total;
    /** 描述 */
    private String description;

    public String getEtype() {
        return etype;
    }

    public void setEtype(String etype) {
        this.etype = etype;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
