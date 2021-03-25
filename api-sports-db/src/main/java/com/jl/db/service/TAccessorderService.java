package com.jl.db.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jl.db.entity.TyAccessOrder;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 
 * @since 2021-03-17
 */
public interface TAccessorderService extends IService<TyAccessOrder> {

    int getOrderIdCount(String orderId);

    TyAccessOrder getOrderInfoByOrderId(String orderId);

    /**
     * 玩家处于游戏中
     * 根据订单号更新状态
     */
    void gupdateAccessOrderById(Integer id);

    /**
     * 根据订单号更新状态
     */
    void updateAccessOrderById(Integer id);

    /**
     * 异常时
     * 根据订单号更新状态
     */
    void eupdateAccessOrderById(Integer id);

    /**
     * 金额不足
     * 根据订单号更新状态
     */
    void supdateAccessOrderById(Integer id);

    /**
     *
     * 订单查询
     */
    TyAccessOrder searchOrder(String orderId, Integer agent);
}
