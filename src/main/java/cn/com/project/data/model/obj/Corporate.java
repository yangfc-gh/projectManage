package cn.com.project.data.model.obj;

import java.util.Date;

/**
 * 主体（我方主体和友方主体）
 */
public class Corporate {
    /**主体id*/
    private String cid;

    /**全称*/
    private String name;

    /**简称*/
    private String shortname;

    /**类型（1自有2友方）*/
    private String ctype;

    /**类型（1自有2友方）*/
    private String ctypeName;

    /**备注*/
    private String remark;

    /**状态1正常0禁用*/
    private String status;

    private Date ctime;

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid == null ? null : cid.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getShortname() {
        return shortname;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname == null ? null : shortname.trim();
    }

    public String getCtype() {
        return ctype;
    }

    public void setCtype(String ctype) {
        this.ctype = ctype == null ? null : ctype.trim();
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

    public String getCtypeName() {
        return ctypeName;
    }

    public void setCtypeName(String ctypeName) {
        this.ctypeName = ctypeName;
    }
}