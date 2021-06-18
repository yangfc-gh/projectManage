package cn.com.project.modules.user.service;

import cn.com.project.data.dao.sys.SysMenuMapper;
import cn.com.project.data.dao.sys.SysRoleMenuMapper;
import cn.com.project.data.dao.sys.SysUserRoleMapper;
import cn.com.project.data.model.sys.SysRoleMenu;
import cn.com.project.data.model.sys.SysUserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 权限管理：菜单、角色操作服务层
 */
@Service
public class AuthorityService {
    @Autowired
    SysRoleMenuMapper sysRoleMenuMapper;
    @Autowired
    SysUserRoleMapper sysUserRoleMapper;
    @Autowired
    SysMenuMapper sysMenuMapper;

    /**
     * 更新一个角色与菜单的关联关系
     * 先清空原来所有，再新增（或不新增）新的关联
     * @return
     */
    @Transactional
    public boolean roleRelationMenu(String rid, List<SysRoleMenu> sysRoleMenus) {
        sysRoleMenuMapper.deleteByRole(rid);
        if (null != sysRoleMenus && !sysRoleMenus.isEmpty()) {
            int res = sysRoleMenuMapper.insertBatch(sysRoleMenus);
            if (res <= 0){
                throw new RuntimeException("role relation menu failed");
            }
        }
        return true;
    }

    /**
     * 更新一个角色与用户的关联关系
     * 先清空原来所有，再新增（或不新增）新的关联
     * @return
     */
    @Transactional
    public boolean roleRelationUser(String rid, List<SysUserRole> sysUserRoles) {
        sysUserRoleMapper.deleteByRole(rid);
        if (null != sysUserRoles && !sysUserRoles.isEmpty()) {
            int res = sysUserRoleMapper.insertBatch(sysUserRoles);
            if (res <= 0){
                throw new RuntimeException("role relation user failed");
            }
        }
        return true;
    }

    /**
     * 更新一个用户与的角色关联关系
     * 先清空原来所有，再新增（或不新增）新的关联
     * @return
     */
    @Transactional
    public boolean userRelationRole(String uid, List<SysUserRole> sysUserRoles) {
        sysUserRoleMapper.deleteByUser(uid);
        if (null != sysUserRoles && !sysUserRoles.isEmpty()) {
            int res = sysUserRoleMapper.insertBatch(sysUserRoles);
            if (res <= 0){
                throw new RuntimeException("user relation role failed");
            }
        }
        return true;
    }

    /**
     * 删除一个菜单
     * 先清空菜单与角色的关联关系
     * @param mid
     */
    @Transactional
    public boolean delMenu(String mid) {
        sysRoleMenuMapper.deleteByMenu(mid);
        int res = sysMenuMapper.deleteByPrimaryKey(mid);
        if (res <= 0){
            throw new RuntimeException("role relation menu failed");
        }
        return true;
    }
}
