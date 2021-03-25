package com.jl.db.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jl.db.entity.YsbAccountInfo;
import com.jl.db.vo.invo.YsbAccountInfoVO;

/**
 * <p>
 * 第三方账户信息表 服务类
 * </p>
 *
 * @author 
 * @since 2021-03-11
 */
public interface YsbAccountInfoService extends IService<YsbAccountInfo> {

    YsbAccountInfoVO getYsbAccountInfo(Integer userId);

    boolean saveYsbAccountInfo(YsbAccountInfoVO accountInfoVO);
}
