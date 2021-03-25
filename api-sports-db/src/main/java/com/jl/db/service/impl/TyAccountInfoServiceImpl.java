package com.jl.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jl.db.entity.TyAccountInfo;
import com.jl.db.mapper.TyAccountInfoMapper;
import com.jl.db.service.TyAccountInfoService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author 
 * @since 2021-03-06
 */
@Service
public class TyAccountInfoServiceImpl extends ServiceImpl<TyAccountInfoMapper, TyAccountInfo> implements TyAccountInfoService {

    @Override
    public TyAccountInfo getAccountsInfoByGameId(Integer gameId) {
        LambdaQueryWrapper<TyAccountInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TyAccountInfo::getUserID,gameId);
        return getOne(wrapper);
    }

    @Override
    public TyAccountInfo getAccountsInfoByAccounts(String accounts, Integer agentId) {
        LambdaQueryWrapper<TyAccountInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TyAccountInfo::getAccounts,accounts);
        wrapper.eq(TyAccountInfo::getOwnerID,agentId);
        return getOne(wrapper);
    }

    @Override
    public Integer querySeqGameID() {
        return baseMapper.getGameId();
    }

    @Override
    public int addUserScore(Integer userID, BigDecimal money) {
        money = money.setScale(2,BigDecimal.ROUND_DOWN);
        return baseMapper.addUserScore(userID,money);
    }

    @Override
    public int subtractUserScore(Integer userID, BigDecimal money) {
        money = money.setScale(2,BigDecimal.ROUND_DOWN);
        return baseMapper.subtractUserScore(userID,money);
    }
}
