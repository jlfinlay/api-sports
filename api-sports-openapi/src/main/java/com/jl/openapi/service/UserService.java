package com.jl.openapi.service;

import com.jl.db.common.Response;
import com.jl.db.entity.TyAccountInfo;
import com.jl.db.exception.ServiceException;
import com.jl.db.service.TyAccountInfoService;
import com.jl.db.utils.bean.BeanUtils;
import com.jl.db.vo.invo.AccountsVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class UserService {

    @Resource
    private TyAccountInfoService tyAccountInfoService;


    public Response<AccountsVO> getUserInfo(Integer userId) {
        TyAccountInfo user = tyAccountInfoService.getById(userId);
        if(user == null){
            throw new ServiceException("用户信息不存在");
        }
        AccountsVO accountsVO = BeanUtils.copyProperties(user, AccountsVO::new);
        return Response.successData(accountsVO);
    }
}
