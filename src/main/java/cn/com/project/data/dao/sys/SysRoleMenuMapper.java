package cn.com.project.data.dao.sys;

import cn.com.project.data.model.sys.SysRoleMenu;

import java.util.List;

/**
 * 权限：角色与菜单关联关系管理
 */
public interface SysRoleMenuMapper {
    int insert(SysRoleMenu record);

    int insertBatch(List<SysRoleMenu> records);

    int insertSelective(SysRoleMenu record);

    int deleteByRole(String rid);
    int deleteByMenu(String mid);

    List<SysRoleMenu> selectByRole(String rid);
    List<SysRoleMenu> selectByMenu(String mid);
}