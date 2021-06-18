package cn.com.project.data.dao.sys;

import cn.com.project.data.model.sys.SysUser;

import java.util.List;

public interface SysUserMapper {
    int deleteByPrimaryKey(String uid);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(String uid);

    SysUser selectByLoginname(String loginname);

    List<SysUser> selectByCondition(SysUser record);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);
}