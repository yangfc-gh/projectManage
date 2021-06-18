package cn.com.project.data.dao.sys;

import cn.com.project.data.model.sys.SysUserRole;

import java.util.List;

public interface SysUserRoleMapper {
    int insert(SysUserRole record);

    int insertSelective(SysUserRole record);

    int insertBatch(List<SysUserRole> records);

    int deleteByRole(String rid);
    int deleteByUser(String uid);

    List<SysUserRole> selectByRole(String rid);
    List<SysUserRole> selectByUser(String uid);
}