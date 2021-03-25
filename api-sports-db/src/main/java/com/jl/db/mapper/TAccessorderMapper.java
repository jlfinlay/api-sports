package com.jl.db.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jl.db.entity.TyAccessOrder;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 
 * @since 2021-03-17
 */
public interface TAccessorderMapper extends BaseMapper<TyAccessOrder> {

    void gupdateStatusById(@Param("id") Integer id);

    void updateAccessOrderById(@Param("id") Integer id);

    void eupdateAccessOrderById(@Param("id") Integer id);

    void supdateAccessOrderById(@Param("id") Integer id);
}
