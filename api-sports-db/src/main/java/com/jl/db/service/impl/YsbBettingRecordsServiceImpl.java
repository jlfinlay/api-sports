package com.jl.db.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jl.db.entity.YsbBettingRecords;
import com.jl.db.service.YsbBettingRecordsService;
import com.jl.db.mapper.YsbBettingRecordsMapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 投注信息记录表(第三方回调记录) 服务实现类
 * </p>
 *
 * @author 
 * @since 2021-03-11
 */
@Service
public class YsbBettingRecordsServiceImpl extends ServiceImpl<YsbBettingRecordsMapper, YsbBettingRecords> implements YsbBettingRecordsService {

}
