package com.jl.openapi.controller;

import com.jl.db.common.Response;
import com.jl.db.vo.invo.RegistrationVO;
import com.jl.db.vo.invo.BetDataListVO;
import com.jl.db.vo.invo.BetSubmitRefNoVO;
import com.jl.db.vo.invo.MaxBetLimitVO;
import com.jl.db.vo.invo.YsbAccountInfoVO;
import com.jl.db.vo.outvo.SportTypeConfigVO;
import com.jl.openapi.service.ThirdGameService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


@Slf4j
@RequestMapping("/game")
@RestController
@Api(tags = "第三方游戏接口")
public class ThirdGameController {

    @Resource
    private ThirdGameService thirdGameService;

    @GetMapping("/getSportTypeConfig")
    @ApiOperation(value = "获取体育类型配置")
    Response<List<SportTypeConfigVO>> getSportTypeConfig(@RequestParam("agentId") Integer agentId){
        log.info("获取体育类型配置,入参, agentId:{}",agentId);
        return thirdGameService.getSportTypeConfig(agentId);
    }

    @GetMapping("/checkUserRegister")
    @ApiOperation(value = "校验用户是否在第三方注册账号,如果注册过就返回注册信息")
    Response<YsbAccountInfoVO> checkUserRegister(@RequestParam("userId") Integer userId){
        log.info("校验用户是否在第三方注册账号,入参,userId:{}", userId);
        return thirdGameService.checkUserRegister(userId);
    }

    @PostMapping("/clientRegister")
    @ApiOperation(value = "第三方注册")
    public Response clientRegister(@RequestBody RegistrationVO vo){
        log.info("第三方注册,入参,vo:{}", vo);
        return thirdGameService.clientRegister(vo);
    }

    @PostMapping("/getMaxBetLimit")
    @ApiOperation(value = "获取投注限额")
    public Response getMaxBetLimit(@RequestBody MaxBetLimitVO vo){
        log.info("获取投注限额,入参,MaxBetLimitVO:{}", vo);
        return thirdGameService.getMaxBetLimit(vo);
    }

    @PostMapping("/getBatchMaxBetLimit")
    @ApiOperation(value = "批量获取投注限额")
    public Response getBatchMaxBetLimit(@RequestBody List<MaxBetLimitVO> vos){
        log.info("批量获取投注限额,入参,MaxBetLimitVO:{}", vos);
        return thirdGameService.getBatchMaxBetLimit(vos);
    }

    @PostMapping("/betSubmitRefNo")
    @ApiOperation(value = "投注提交对应投注订单号")
    public Response betSubmitRefNo(@RequestBody BetSubmitRefNoVO vo){
        log.info("投注提交对应投注订单号,入参,BetSubmitRefNoVO:{}",vo);
        return thirdGameService.betSubmitRefNo(vo);
    }

    @PostMapping("/getExtraBetOdds")
    @ApiOperation(value = "获取额外赔率")
    public Response getExtraBetOdds(@RequestParam("gameCode")  String gameCode ,@RequestParam("userId")  Integer userId){
        log.info("获取额外赔率,入参,userId:{},gameCode:{}", userId,gameCode);
        return thirdGameService.getExtraBetOdds(userId,gameCode);
    }

    @PostMapping("/getBetList")
    @ApiOperation(value = "获取体育投注列表")
    public Response getBetList(@RequestBody BetDataListVO vo){
        log.info("获取投注列表,入参,BetDataListVO:{}", vo);
        return thirdGameService.getLocalBetList(vo);
    }

}
