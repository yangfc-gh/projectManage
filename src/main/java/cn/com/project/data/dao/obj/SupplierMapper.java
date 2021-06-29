package cn.com.project.data.dao.obj;

import cn.com.project.data.model.obj.Supplier;

import java.util.List;

/**
 * 供应商管理
 */
public interface SupplierMapper {
    int deleteByPrimaryKey(String sid);

    int insert(Supplier record);

    int insertSelective(Supplier record);

    Supplier selectByPrimaryKey(String sid);

    List<Supplier> selectByCondition(Supplier record);

    int updateByPrimaryKeySelective(Supplier record);

    int updateByPrimaryKey(Supplier record);
}