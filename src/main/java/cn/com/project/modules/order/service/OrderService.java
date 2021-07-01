package cn.com.project.modules.order.service;

import cn.com.project.common.CommonUtils;
import cn.com.project.data.dao.business.*;
import cn.com.project.data.model.business.ProOrder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 订单信息事务操作
 */
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

    /**
     * 订单状态进展
     * @param oid
     * @return
     */
    public boolean orderState(String oid, String status){
        if (StringUtils.isNotBlank(oid) && StringUtils.isNotBlank(status)){
            ProOrder order = new ProOrder();
            order.setOid(oid);
            order.setStatus(status);

            int res = proOrderMapper.updateByPrimaryKeySelective(order);
            if(res <= 0) {
                throw new RuntimeException("update order state failed");
            }
            return true;
        }
        return false;
    }
}
