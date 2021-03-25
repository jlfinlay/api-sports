package com.jl.openapi.controller;

import com.jl.db.common.Response;
import com.jl.db.vo.invo.AccountsVO;
import com.jl.openapi.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Slf4j
@Api(tags = "个人中心/我的")
public class UserCenterController {

    @Resource
    private UserService userService;

    @GetMapping("/getUserInfo")
    @ApiOperation(value = "获取个人信息")
    public Response<AccountsVO> getUserInfo(@RequestParam("userId") Integer userId){
        log.info("获取个人信息,入参userId:{}",userId);
        return userService.getUserInfo(userId);
    }


}
