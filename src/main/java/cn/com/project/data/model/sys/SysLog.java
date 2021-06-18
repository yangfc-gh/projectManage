package cn.com.project.data.model.sys;

import java.util.Date;

public class SysLog {
    /**日志记录id*/
    private String logid;

    /**操作人*/
    private String optuser;

    /**日志时间*/
    private Date logtime;

    /**日志简要*/
    private String resume;

    /**日志类型0其它1增2删3改*/
    private String logtype;

    /**操作实体*/
    private String entity;

    /**日志详情*/
    private String describe;

    public String getLogid() {
        return logid;
    }

    public void setLogid(String logid) {
        this.logid = logid == null ? null : logid.trim();
    }

    public String getOptuser() {
        return optuser;
    }

    public void setOptuser(String optuser) {
        this.optuser = optuser == null ? null : optuser.trim();
    }

    public Date getLogtime() {
        return logtime;
    }

    public void setLogtime(Date logtime) {
        this.logtime = logtime;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume == null ? null : resume.trim();
    }

    public String getLogtype() {
        return logtype;
    }

    public void setLogtype(String logtype) {
        this.logtype = logtype == null ? null : logtype.trim();
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity == null ? null : entity.trim();
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe == null ? null : describe.trim();
    }
}