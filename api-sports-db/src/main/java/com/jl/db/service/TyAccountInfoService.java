package com.jl.db.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jl.db.entity.TyAccountInfo;

import java.math.BigDecimal;

/**
 * <p>
 * 用户信息表 服务类
 * </p>
 *
 * @author 
 * @since 2021-03-06
 */
public interface TyAccountInfoService extends IService<TyAccountInfo> {

    TyAccountInfo getAccountsInfoByGameId(Integer gameId);

    TyAccountInfo getAccountsInfoByAccounts(String accounts, Integer agentId);

    Integer querySeqGameID();

    int addUserScore(Integer userID, BigDecimal money);

    int subtractUserScore(Integer userID, BigDecimal money);
}
