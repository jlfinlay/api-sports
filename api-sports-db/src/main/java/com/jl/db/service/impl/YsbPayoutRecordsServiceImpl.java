package com.jl.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jl.db.entity.YsbPayoutRecords;
import com.jl.db.mapper.YsbPayoutRecordsMapper;
import com.jl.db.service.YsbPayoutRecordsService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 投注派彩记录表 服务实现类
 * </p>
 *
 * @author 
 * @since 2021-03-11
 */
@Service
public class YsbPayoutRecordsServiceImpl extends ServiceImpl<YsbPayoutRecordsMapper, YsbPayoutRecords> implements YsbPayoutRecordsService {

    @Override
    public List<YsbPayoutRecords> selectPayOutData(String bettingId) {
        LambdaQueryWrapper<YsbPayoutRecords> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(YsbPayoutRecords::getReferenceId,bettingId);
        wrapper.orderByDesc(YsbPayoutRecords::getId);
        return baseMapper.selectList(wrapper);
    }
}
