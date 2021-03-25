package com.jl.openapi.controller;

import com.alibaba.fastjson.JSONObject;
import com.jl.openapi.service.NotifyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@Api(tags = "第三方投注回调接口")
public class NotifyController {

    @Resource
    private NotifyService notifyService;

    @ApiOperation(value = "第三方投注回调", notes = "第三方投注回调")
    @RequestMapping("/game/callback.do")
    public String gameCallBack(HttpServletRequest request, @RequestBody JSONObject json){
        log.info("第三方投注回调,入,json:{}",json);
        return notifyService.gameCallBack(request,json);
    }
}
