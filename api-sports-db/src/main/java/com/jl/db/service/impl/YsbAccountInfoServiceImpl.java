package com.jl.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jl.db.entity.YsbAccountInfo;
import com.jl.db.mapper.YsbAccountInfoMapper;
import com.jl.db.service.YsbAccountInfoService;
import com.jl.db.utils.bean.BeanUtils;
import com.jl.db.vo.invo.YsbAccountInfoVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 第三方账户信息表 服务实现类
 * </p>
 *
 * @author 
 * @since 2021-03-11
 */
@Service
public class YsbAccountInfoServiceImpl extends ServiceImpl<YsbAccountInfoMapper, YsbAccountInfo> implements YsbAccountInfoService {

    @Override
    public YsbAccountInfoVO getYsbAccountInfo(Integer userId) {
        LambdaQueryWrapper<YsbAccountInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(YsbAccountInfo::getUserId, userId);
        YsbAccountInfo entity = getOne(wrapper);
        return BeanUtils.copyProperties(entity, YsbAccountInfoVO::new);
    }

    @Override
    public boolean saveYsbAccountInfo(YsbAccountInfoVO accountInfoVO) {
        LambdaQueryWrapper<YsbAccountInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(YsbAccountInfo::getUserId, accountInfoVO.getUserId());
        List<YsbAccountInfo> list = list(wrapper);
        if (null == list || list.isEmpty()) {
            return BeanUtils.copyProperties(accountInfoVO, YsbAccountInfo::new).insert();
        } else {
            list.stream().forEach(x -> {
                removeById(x.getId());
            });
            return BeanUtils.copyProperties(accountInfoVO, YsbAccountInfo::new).insert();
        }
    }
}
