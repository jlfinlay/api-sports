package com.jl.db.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jl.db.entity.SportTypeConfig;
import com.jl.db.vo.outvo.SportTypeConfigVO;

import java.util.List;

/**
 * <p>
 * 体育类型定义表 服务类
 * </p>
 *
 * @author 
 * @since 2021-03-11
 */
public interface SportTypeConfigService extends IService<SportTypeConfig> {

    List<SportTypeConfigVO> getSportTypeConfig(Integer agentId, Integer status);
}
