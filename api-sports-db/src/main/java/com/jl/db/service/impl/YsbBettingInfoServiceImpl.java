package com.jl.db.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jl.db.entity.YsbBettingInfo;
import com.jl.db.mapper.YsbBettingInfoMapper;
import com.jl.db.service.YsbBettingInfoService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 投注信息表(第三方回调记录) 服务实现类
 * </p>
 *
 * @author 
 * @since 2021-03-11
 */
@Service
public class YsbBettingInfoServiceImpl extends ServiceImpl<YsbBettingInfoMapper, YsbBettingInfo> implements YsbBettingInfoService {

}
