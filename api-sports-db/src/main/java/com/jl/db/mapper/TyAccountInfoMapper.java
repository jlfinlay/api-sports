package com.jl.db.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jl.db.entity.TyAccountInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * <p>
 * 用户信息表 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2021-03-06
 */
public interface TyAccountInfoMapper extends BaseMapper<TyAccountInfo> {

    Integer getGameId();

    int addUserScore(@Param("userId") Integer userId, @Param("money") BigDecimal money);

    int subtractUserScore(@Param("userId") Integer userId, @Param("money") BigDecimal money);
}
