package cn.com.project.data.dao.sys;


import cn.com.project.data.model.sys.SysLog;

public interface SysLogMapper {
    int deleteByPrimaryKey(String logid);

    int insert(SysLog record);

    int insertSelective(SysLog record);

    SysLog selectByPrimaryKey(String logid);

    int updateByPrimaryKeySelective(SysLog record);

    int updateByPrimaryKeyWithBLOBs(SysLog record);

    int updateByPrimaryKey(SysLog record);
}