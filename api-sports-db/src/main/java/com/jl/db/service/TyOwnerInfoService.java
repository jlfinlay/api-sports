package com.jl.db.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jl.db.entity.TyOwnerInfo;

import java.util.List;

/**
 * <p>
 * 业主信息表 服务类
 * </p>
 *
 * @author 
 * @since 2021-03-17
 */
public interface TyOwnerInfoService extends IService<TyOwnerInfo> {

    List<TyOwnerInfo> getAgentByAgentId(Integer agent, Integer status);
}
