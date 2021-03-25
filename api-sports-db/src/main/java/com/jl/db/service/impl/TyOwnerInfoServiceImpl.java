package com.jl.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jl.db.entity.TyOwnerInfo;
import com.jl.db.mapper.TyOwnerInfoMapper;
import com.jl.db.service.TyOwnerInfoService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 业主信息表 服务实现类
 * </p>
 *
 * @author 
 * @since 2021-03-17
 */
@Service
public class TyOwnerInfoServiceImpl extends ServiceImpl<TyOwnerInfoMapper, TyOwnerInfo> implements TyOwnerInfoService {

    @Override
    public List<TyOwnerInfo> getAgentByAgentId(Integer agent, Integer status) {
        LambdaQueryWrapper<TyOwnerInfo> tccLambdaQueryWrapper = new LambdaQueryWrapper<>();
        if(null != agent){
            tccLambdaQueryWrapper.eq(TyOwnerInfo:: getOwnerId, agent);
        }
        if(null != status){
            tccLambdaQueryWrapper.eq(TyOwnerInfo:: getAgentStatus, status);
        }
        return list(tccLambdaQueryWrapper);
    }
}
