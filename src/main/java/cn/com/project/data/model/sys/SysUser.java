package cn.com.project.data.model.sys;

import java.util.Date;
import java.util.List;

public class SysUser {
    /**用户id*/
    private String uid;

    /**姓名*/
    private String name;

    /**性别*/
    private String gender;

    /**职务*/
    private String position;

    /**部门*/
    private String deptid;

    /**联系电话*/
    private String phone;

    /**登录账号*/
    private String loginname;

    /**登录密码*/
    private String password;

    /**拥有角色id*/
    private List<String> roleids;

    /**状态*/
    private Byte state;

    /**备注*/
    private String remark;

    private Date ctime;

    private Date utime;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid == null ? null : uid.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position == null ? null : position.trim();
    }

    public String getDeptid() {
        return deptid;
    }

    public void setDeptid(String deptid) {
        this.deptid = deptid == null ? null : deptid.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getLoginname() {
        return loginname;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname == null ? null : loginname.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public List<String> getRoleids() {
        return roleids;
    }

    public void setRoleids(List<String> roleids) {
        this.roleids = roleids;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}