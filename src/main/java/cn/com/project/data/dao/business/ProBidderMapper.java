package cn.com.project.data.dao.business;

import cn.com.project.data.model.business.ProBidder;

import java.util.List;

public interface ProBidderMapper {
    int deleteByPrimaryKey(String bid);

    int insert(ProBidder record);

    int insertSelective(ProBidder record);

    ProBidder selectByPrimaryKey(String bid);

    List<ProBidder> selectByOid(String oid);

    int updateByPrimaryKeySelective(ProBidder record);

    int updateByPrimaryKey(ProBidder record);
}