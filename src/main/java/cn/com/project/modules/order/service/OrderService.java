package cn.com.project.modules.order.service;

import cn.com.project.common.CommonUtils;
import cn.com.project.data.dao.business.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    @Autowired
    ProOrderMapper proOrderMapper;

    /**
     * 删除一个订单
     * @param oid
     * @return
     */
    @Transactional
    public boolean delOrder(String oid){
        int res = proOrderMapper.deleteByPrimaryKey(oid);
        if(res <= 0) {
            throw new RuntimeException("delete order failed");
        }
        return true;
    }
}
