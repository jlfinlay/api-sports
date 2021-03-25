package com.jl.db.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jl.db.common.Response;
import com.jl.db.entity.BetData;
import com.jl.db.vo.outvo.ParamVO;

import java.util.List;


/**
 * <p>
 * 本地投注记录表 服务类
 * </p>
 *
 * @author 
 * @since 2021-03-12
 */
public interface BetDataService extends IService<BetData> {

    BetData getBetDataByRefNo(String refNo);

    BetData getBetDataByBettingId(String referenceId);

    List<BetData> gameRecord(ParamVO param);
}
