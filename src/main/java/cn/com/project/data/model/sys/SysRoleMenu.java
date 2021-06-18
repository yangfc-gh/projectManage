package cn.com.project.data.model.sys;

public class SysRoleMenu {
    /**角色id*/
    private String rid;

    /**菜单id*/
    private String mid;

    /**增权限*/
    private Byte add;

    /**删权限*/
    private Byte del;

    /**改权限*/
    private Byte upd;

    private SysMenu menu;

    private SysRole role;

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid == null ? null : rid.trim();
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid == null ? null : mid.trim();
    }

    public Byte getAdd() {
        return add;
    }

    public void setAdd(Byte add) {
        this.add = add;
    }

    public Byte getDel() {
        return del;
    }

    public void setDel(Byte del) {
        this.del = del;
    }

    public Byte getUpd() {
        return upd;
    }

    public void setUpd(Byte upd) {
        this.upd = upd;
    }

    public SysMenu getMenu() {
        return menu;
    }

    public void setMenu(SysMenu menu) {
        this.menu = menu;
    }

    public SysRole getRole() {
        return role;
    }

    public void setRole(SysRole role) {
        this.role = role;
    }
}