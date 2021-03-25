package com.jl.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jl.db.entity.BetData;
import com.jl.db.exception.ServiceException;
import com.jl.db.mapper.BetDataMapper;
import com.jl.db.service.BetDataService;
import com.jl.db.service.YsbPayoutRecordsService;
import com.jl.db.vo.outvo.ParamVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 本地投注记录表 服务实现类
 * </p>
 *
 * @author 
 * @since 2021-03-12
 */
@Service
public class BetDataServiceImpl extends ServiceImpl<BetDataMapper, BetData> implements BetDataService {

    @Resource
    private YsbPayoutRecordsService ysbPayoutRecordsService;

    @Override
    public BetData getBetDataByRefNo(String refNo) {
        LambdaQueryWrapper<BetData> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BetData::getLocalBetNo,refNo);
        return baseMapper.selectOne(wrapper);
    }

    @Override
    public BetData getBetDataByBettingId(String referenceId) {
        LambdaQueryWrapper<BetData> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BetData::getBettingId,referenceId);
        List<BetData> betDataEntities = baseMapper.selectList(wrapper);
        if(betDataEntities == null||betDataEntities.size() == 0){
            throw new ServiceException("派彩失败! referenceId对应的订单信息不存在");
        }
        if(betDataEntities.size()>1){
            throw new ServiceException("派彩失败,重复的referenceId!");
        }
        return betDataEntities.get(0);
    }

    @Override
    public List<BetData> gameRecord(ParamVO param) {
        LambdaQueryWrapper<BetData> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BetData::getAgentId,param.getAgent());
        wrapper.between(BetData::getBetTime,new Date(param.getStartTime()),new Date(param.getEndTime()));
        return list(wrapper);
    }


}
