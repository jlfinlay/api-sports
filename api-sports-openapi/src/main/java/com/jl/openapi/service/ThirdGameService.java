package com.jl.openapi.service;

import cn.hutool.http.HttpStatus;
import com.alibaba.fastjson.JSONObject;

import com.jl.db.common.Response;
import com.jl.db.config.redis.RedisKeyPrefix;
import com.jl.db.exception.ServiceException;
import com.jl.db.utils.redis.RedisClient;
import com.jl.db.vo.invo.RegistrationVO;
import com.jl.db.vo.invo.*;
import com.jl.db.vo.outvo.GameScoreInfoVO;
import com.jl.db.vo.outvo.SportTypeConfigVO;
import com.jl.openapi.game.service.ThirdGameFactory;
import com.jl.openapi.utils.OrderNumberUtils;
import com.jl.openapi.utils.TimeUtil;
import com.jl.openapi.utils.ValidateParamUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;


@Service
@Slf4j
public class ThirdGameService {

    private static final Integer BET_SUBMIT_SUCCESS = 1; //投注提交成功
    private static final Integer BET_SUBMIT_FAIL = 2; //投注提交失败

    @Resource
    private ValidateParamUtil validateParamUtil;


    @Resource
    private ThirdGameFactory gameFactory;

    @Resource
    private RedisClient redisClient;

    @Resource
    private GameService gameService;


    /**
     * @description
     *  检查用户是否在第三方注册
     * @param userId
     */
    public Response<YsbAccountInfoVO> checkUserRegister(Integer userId){
        // 判断是否注册
        Response<YsbAccountInfoVO> result = gameService.getYsbAccountInfoByUserId(userId);
        if(HttpStatus.HTTP_OK == Integer.parseInt(result.getCode())){
            return Response.successData(result.getData());
        }
        return Response.fail(result.getMsg());
    }


    /**
     * @description
     *  第三方注册
     * @param vo
     */
    public Response<YsbAccountInfoVO> clientRegister(RegistrationVO vo) {
        validateParamUtil.valid(vo);
        // 获取用户信息
        vo.setUserName(getAccounts(vo.getUserId()));
        Response<String> result = gameFactory.get(vo.getGameCode()).clientRegister(vo);
        // 解析数据,入库
        if(HttpStatus.HTTP_OK == Integer.parseInt(result.getCode())){
            String accountsId = result.getData();
            YsbAccountInfoVO accountInfoVO = new YsbAccountInfoVO();
            accountInfoVO.setUserId(vo.getUserId());
            accountInfoVO.setAccountId(accountsId);
            accountInfoVO.setAgentId(vo.getAgentId());
            accountInfoVO.setGameCode(vo.getGameCode());
            accountInfoVO.setRegistTime(new Date());
            gameService.saveYsbAccountInfo(accountInfoVO);
            return Response.successData(accountInfoVO);
        }
        return Response.fail(result.getMsg());
    }






    private void setQueryDate(BetDataListVO vo){
        Integer date = vo.getDate();
        // 时间段 0：今天 1:昨天 2:近7天  3:一个月 4:三个月
        if(date.equals(0)){
            vo.setStartTime(TimeUtil.getInitial());
            vo.setEndTime(TimeUtil.getNow());
        }else if(date.equals(1)){
            vo.setStartTime(TimeUtil.format(TimeUtil.getBeginDayOfYesterday()));
            vo.setEndTime(TimeUtil.format(TimeUtil.getEndDayOfYesterday()));
        }else if(date.equals(2)){
            vo.setStartTime(TimeUtil.format(TimeUtil.getBeginDayOfWeekday()));
            vo.setEndTime(TimeUtil.getNow());
        }else if(date.equals(3)){
            vo.setStartTime(TimeUtil.format(TimeUtil.getBeginDayOfMonthday()));
            vo.setEndTime(TimeUtil.getNow());
        }else {
            vo.setStartTime(TimeUtil.format(TimeUtil.getBeginDayOfThreeMonthday()));
            vo.setEndTime(TimeUtil.getNow());
        }

    }

    /**
     * @description
     * 获取本地投注列表
     * @return
     */
    public Response getLocalBetList(BetDataListVO vo) {
        // 参数校验
        validateParamUtil.valid(vo);
        setQueryDate(vo);
        return gameService.getLocalBetList(vo);
    }

    /**
     * @description
     * 获取投注限额
     * @return
     */
    public Response getMaxBetLimit(MaxBetLimitVO vo) {
        log.info("获取投注限额,入参,MaxBetLimitVO:{}", vo);
        validateParamUtil.valid(vo);
        Response result = gameFactory.get(vo.getGameCode()).getMaxBetLimit(vo);
        if(HttpStatus.HTTP_OK == Integer.parseInt(result.getCode())) {
            String detail = (String) result.getData();
            Map<String,Object> map = new HashMap<>();
            if(StringUtils.isNotBlank(detail)){
                JSONObject json = JSONObject.parseObject(detail);
                map.put("maxLimit",json.getString("max"));
                map.put("selectionId",json.getString("si"));
            }
            return Response.successData(map);
        }
        return Response.fail(result.getMsg());
    }

    /**
     * @description
     * 批量获取投注限额
     * @return
     */
    public Response getBatchMaxBetLimit(List<MaxBetLimitVO> vos) {
        log.info("批量获取投注限额,入参,MaxBetLimitVO:{}", vos);
        validateParamUtil.validList(vos);
        CountDownLatch countDownLatch = new CountDownLatch(vos.size());
        List<Map<String,Object>> mapList = new ArrayList<>();
        for(MaxBetLimitVO vo : vos){
            try {
                Future<Response> future = getBatchMaxBetLimitOne(countDownLatch,vo);
                Response result = future.get();
                if(HttpStatus.HTTP_OK == Integer.parseInt(result.getCode())) {
                    Map<String,Object> map  = (Map<String, Object>) result.getData();
                    mapList.add(map);
                }
            }catch (Exception e){
                log.info("批量获取投注限额异常!入参:{},e:{}",vo,e);
                continue;
            }
        }

        return Response.successData(mapList);
    }

    public Response<List<SportTypeConfigVO>> getSportTypeConfig(Integer agentId) {
        log.info("获取体育类型配置,入参,agentId:{}", agentId);
        if(null == agentId){
            throw new ServiceException("业主id不能为空");
        }
        return gameService.getSportTypeConfig(agentId);
    }

    // 多线程处理
    private Future<Response> getBatchMaxBetLimitOne(CountDownLatch countDownLatch, MaxBetLimitVO vo) {
        try {
            return  new AsyncResult<>(getMaxBetLimit(vo));
        } finally {
            countDownLatch.countDown();
        }
    }

  

    

   
    /**
     * @description
     *  获取用户信息
     * @return
     */
    private String getAccounts(Integer userId){
        String accounts = null;
        Response result = gameService.getUserInfo(userId);
        if(HttpStatus.HTTP_OK == Integer.parseInt(result.getCode())){
            Map<String,Object> map = (Map<String, Object>) result.getData();
            if(map.containsKey("accounts")){
                accounts = (String) map.get("accounts");
                return accounts;
            }
        } else {
            throw new ServiceException(result.getMsg());
        }
        return accounts;
    }




    /**
     * @description
     * 投注提交对应投注订单号
     */
    public Response betSubmitRefNo(BetSubmitRefNoVO vo) {
        validateParamUtil.valid(vo);
        // 加锁
        String key = RedisKeyPrefix.BET_SUBMIT_REF_NO+ vo.getUserId();
        try {
            if (!redisClient.setDistributedLock(key, key, 5)) {
                throw new ServiceException("操作失败：请求太频繁，请稍后重试");
            }
            // 获取用户信息
            //vo.setUserName(getAccounts(vo.getUserId()));
            // 判读用用户余额
            checkUserScoreInfo(vo);
            // 生成订单号
            setBetSubmitRefNoVO(vo);
            Response result = gameFactory.get(vo.getGameCode()).betSubmitRefNO(vo);
            if(HttpStatus.HTTP_OK == Integer.parseInt(result.getCode())){
                // 只保存投注提交成功的订单
                Integer status = BET_SUBMIT_SUCCESS;
                String errMsg = result.getData().toString();
                // 投注提交成功信息入库
                setBetSubmitRefNoVO(vo,status,errMsg);
                gameService.saveBetDataRefNo(vo);
                return Response.successData(result.getData());
            }
            return Response.fail(result.getMsg());
        } finally {
            redisClient.releaseDistributedLock(key,key);
        }
    }

    // 生成错误描述
    private void setBetSubmitRefNoVO(BetSubmitRefNoVO vo,Integer status,String msg){
        vo.getBetStakeList().stream().forEach(x->{
            x.setBetStatus(status);
            x.setErrMsg(msg);
        });
    }

    // 生成订单号
    private void setBetSubmitRefNoVO(BetSubmitRefNoVO vo){
        Date betTime = new Date();
        vo.getBetStakeList().stream().forEach(x->{
            x.getBetSelectionList().stream().forEach(m->{
                if(StringUtils.isBlank(m.getSportName())){
                    throw new ServiceException("投注失败,体育赛项名称不能为空");
                }
            });
            // 生成订单号
            x.setLocalBetNo(OrderNumberUtils.makeOrderNum());
            x.setBetTime(betTime);
        });
    }


    /**
     * @description
     *  用户余额校验
     * @return 用户投注额
     */
    private void checkUserScoreInfo(BetSubmitRefNoVO vo){
        // 判断用户余额
        BigDecimal betAmount ;
        Response<GameScoreInfoVO> result = gameService.getUserGameScoreInfo(vo.getUserId());
        if(HttpStatus.HTTP_OK == Integer.parseInt(result.getCode())){
            GameScoreInfoVO scoreVo = result.getData();
            if(null == scoreVo || null == scoreVo.getScore() || scoreVo.getScore().compareTo(BigDecimal.ZERO)<=0){
                throw new ServiceException("用户余额不足,暂不能发起投注");
            }
            BigDecimal score = scoreVo.getScore();
            log.info("投注提交,查询用户余额,用户:{},score:{}",vo.getUserId(),score);
            // 获取投注金额
            List<BetStakeVO> stakeVOS = vo.getBetStakeList();
            stakeVOS.stream().forEach(x->{
                if(x.getStake() == null){
                    throw new ServiceException("投注金额不能为空!");
                }
                if(x.getStake().compareTo(new BigDecimal(8))<0){
                    throw new ServiceException("投注金额错误,最低投注8元!");
                }
            });
            betAmount = stakeVOS.stream().map(BetStakeVO::getStake).reduce(BigDecimal.ZERO,(x,y)->x.add(y).setScale(2,BigDecimal.ROUND_HALF_DOWN));
            if(score.compareTo(betAmount)<0){
                throw new ServiceException("投注金额大于用户余额,暂不能发起投注");
            }
            vo.setTotalBetAmount(betAmount);
        }else{
            throw new ServiceException("用户余额查询失败");
        }

    }


    public Response getExtraBetOdds(Integer userId, String gameCode) {
        log.info("获取额外赔率,入参,userId:{}",userId);
        if(null == userId){
            throw new ServiceException("获取额外赔率,userId不能为空");
        }
        Response result = gameFactory.get(gameCode).getExtraBetOdds(userId);
        if(HttpStatus.HTTP_OK == Integer.parseInt(result.getCode())){
            return Response.successData(result.getData());
        }
        return Response.fail(result.getMsg());

    }
}
