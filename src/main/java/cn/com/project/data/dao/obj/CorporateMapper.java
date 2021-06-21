package cn.com.project.data.dao.obj;

import cn.com.project.data.model.obj.Corporate;

import java.util.List;

public interface CorporateMapper {
    int deleteByPrimaryKey(String cid);

    int insert(Corporate record);

    int insertSelective(Corporate record);

    Corporate selectByPrimaryKey(String cid);

    List<Corporate> selectByCondition(Corporate record);

    int updateByPrimaryKeySelective(Corporate record);

    int updateByPrimaryKey(Corporate record);
}