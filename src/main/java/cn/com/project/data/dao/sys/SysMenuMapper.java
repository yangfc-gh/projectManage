package cn.com.project.data.dao.sys;

import cn.com.project.data.model.sys.SysMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 权限：菜单管理
 */
public interface SysMenuMapper {
    int deleteByPrimaryKey(String mid);

    int insert(SysMenu record);

    int insertSelective(SysMenu record);

    SysMenu selectByPrimaryKey(String mid);

    List<SysMenu> selectByCondition(SysMenu record);

    /**
     * 子孙菜单
     * @param pid
     * @return
     */
    List<SysMenu> selectByPid(Map<String, String> params);

    /**
     * 只取子，不取孙
     * @param pid
     * @return
     */
    List<SysMenu> selectSub(String pid);

    int updateByPrimaryKeySelective(SysMenu record);

    int updateByPrimaryKey(SysMenu record);

    int sortNo(List<SysMenu> sysMenus);
}