package cn.com.project.data.dao.sys;

import cn.com.project.data.model.sys.SysRole;

import java.util.List;
import java.util.Map;

/**
 * 权限：角色管理
 */
public interface SysRoleMapper {
    int deleteByPrimaryKey(String rid);

    /**
     * 删除用户与角色关联关系-根据用户id
     * @param uid
     * @return
     */
    int deleteRelationByUser(String uid);

    /**
     * 删除用户与角色关联关系-根据角色id
     * @param rid
     * @return
     */
    int deleteRelationByRole(String rid);

    /**
     * 批量添加用户与角色关联关系
     * @param relations
     * @return
     */
    int insertRelationBatch(List<Map<String, String>> relations);

    int insert(SysRole record);

    int insertSelective(SysRole record);

    SysRole selectByPrimaryKey(String rid);

    List<SysRole> selectByCondition(SysRole record);

    int updateByPrimaryKeySelective(SysRole record);

    int updateByPrimaryKey(SysRole record);
}