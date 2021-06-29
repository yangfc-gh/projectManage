package cn.com.project.data.dao.obj;

import cn.com.project.data.model.obj.Corporate;

import java.util.List;

/**
 * 主体管理（我方主体和友方主体）
 */
public interface CorporateMapper {
    int deleteByPrimaryKey(String cid);

    int insert(Corporate record);

    int insertSelective(Corporate record);

    Corporate selectByPrimaryKey(String cid);

    List<Corporate> selectByCondition(Corporate record);

    int updateByPrimaryKeySelective(Corporate record);

    int updateByPrimaryKey(Corporate record);
}