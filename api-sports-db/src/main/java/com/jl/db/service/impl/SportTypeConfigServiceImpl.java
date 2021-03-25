package com.jl.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jl.db.entity.SportTypeConfig;
import com.jl.db.mapper.SportTypeConfigMapper;
import com.jl.db.service.SportTypeConfigService;
import com.jl.db.utils.bean.BeanUtils;
import com.jl.db.vo.outvo.SportTypeConfigVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 体育类型定义表 服务实现类
 * </p>
 *
 * @author 
 * @since 2021-03-11
 */
@Service
public class SportTypeConfigServiceImpl extends ServiceImpl<SportTypeConfigMapper, SportTypeConfig> implements SportTypeConfigService {

    @Override
    public List<SportTypeConfigVO> getSportTypeConfig(Integer agentId, Integer status) {
        LambdaQueryWrapper<SportTypeConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SportTypeConfig::getAgentId,agentId);
        wrapper.orderByAsc(SportTypeConfig::getSort);
        if(null != status){
            wrapper.eq(SportTypeConfig::getIsOpen,status);
        }
        return BeanUtils.copyProperties(baseMapper.selectList(wrapper),SportTypeConfigVO::new);
    }
}
