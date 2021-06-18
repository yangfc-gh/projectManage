package cn.com.project.data.dao.sys;

import cn.com.project.data.model.sys.SysDicts;

import java.util.List;

public interface SysDictsMapper {
    int deleteByPrimaryKey(String dcode);

    int insert(SysDicts record);

    int insertSelective(SysDicts record);

    SysDicts selectByPrimaryKey(String dcode);

    List<SysDicts> selectByCondition(SysDicts record);

    int updateByPrimaryKeySelective(SysDicts record);

    int updateByPrimaryKey(SysDicts record);

    int sortNo(List<SysDicts> dicts);
}