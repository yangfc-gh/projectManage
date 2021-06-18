package cn.com.project.data.model.sys;

public class SysDicts {
    /**字典编号*/
    private String dcode;

    /**父级编号*/
    private String pcode;

    /**字典名子*/
    private String dname;

    /**排序*/
    private Integer sortno;

    /**状态*/
    private Byte state;

    public String getDcode() {
        return dcode;
    }

    public void setDcode(String dcode) {
        this.dcode = dcode == null ? null : dcode.trim();
    }

    public String getPcode() {
        return pcode;
    }

    public void setPcode(String pcode) {
        this.pcode = pcode == null ? null : pcode.trim();
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname == null ? null : dname.trim();
    }

    public Integer getSortno() {
        return sortno;
    }

    public void setSortno(Integer sortno) {
        this.sortno = sortno;
    }

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
    }
}