package com.jl.db.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jl.db.entity.YsbPayoutRecords;

import java.util.List;

/**
 * <p>
 * 投注派彩记录表 服务类
 * </p>
 *
 * @author 
 * @since 2021-03-11
 */
public interface YsbPayoutRecordsService extends IService<YsbPayoutRecords> {

    List<YsbPayoutRecords> selectPayOutData(String bettingId);
}
