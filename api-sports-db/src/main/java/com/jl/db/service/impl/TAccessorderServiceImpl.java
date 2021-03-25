package com.jl.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jl.db.entity.TyAccessOrder;
import com.jl.db.mapper.TAccessorderMapper;
import com.jl.db.service.TAccessorderService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 
 * @since 2021-03-17
 */
@Service
public class TAccessorderServiceImpl extends ServiceImpl<TAccessorderMapper, TyAccessOrder> implements TAccessorderService {

    @Override
    public int getOrderIdCount(String orderId) {
        LambdaQueryWrapper<TyAccessOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TyAccessOrder:: getOrderId, orderId);
        return count(wrapper);
    }

    @Override
    public TyAccessOrder getOrderInfoByOrderId(String orderId) {
        LambdaQueryWrapper<TyAccessOrder> tAccessOrderWrapper = new LambdaQueryWrapper<>();
        tAccessOrderWrapper.eq(TyAccessOrder:: getOrderId, orderId);
        return getOne(tAccessOrderWrapper);
    }

    @Override
    public void gupdateAccessOrderById(Integer id) {
        baseMapper.gupdateStatusById(id);
    }

    @Override
    public void updateAccessOrderById(Integer id) {
        baseMapper.updateAccessOrderById(id);
    }

    @Override
    public void eupdateAccessOrderById(Integer id) {
        baseMapper.eupdateAccessOrderById(id);
    }

    @Override
    public void supdateAccessOrderById(Integer id) {
        baseMapper.supdateAccessOrderById(id);
    }

    @Override
    public TyAccessOrder searchOrder(String orderId, Integer agent) {
        LambdaQueryWrapper<TyAccessOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TyAccessOrder::getOrderId,orderId);
        wrapper.eq(TyAccessOrder::getOwnerId,agent);
        return getOne(wrapper);
    }
}
