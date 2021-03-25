package com.jl.openapi.service;

import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSON;
import com.jl.db.common.Response;
import com.jl.db.config.redis.RedisKeyPrefix;
import com.jl.db.entity.*;
import com.jl.db.exception.GlobeException;
import com.jl.db.service.*;
import com.jl.db.utils.IPUtils;
import com.jl.db.utils.bean.BeanUtils;
import com.jl.db.utils.redis.RedisClient;
import com.jl.db.vo.invo.AccountLoginVO;
import com.jl.db.vo.invo.UpperScoreVo;
import com.jl.db.vo.outvo.*;
import com.jl.openapi.utils.DESUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class OpenApiService {

    @Resource
    private RedisClient redisClient;

    @Resource
    private TyOwnerInfoService tyOwnerInfoService;

    @Resource
    private TAccessorderService tAccessorderService;

    @Resource
    private TyAccountInfoService tyAccountInfoService;

    @Resource
    private BetDataService betDataService;


    /**
     * @description
     * 参数校验
     */
    public Response<ParamVO> verification(HttpServletRequest request, String agent, String timestamp, String s, String param) {
        if (StringUtils.isBlank(timestamp)
                || StringUtils.isBlank(param)
                || StringUtils.isBlank(s)
                || StringUtils.isBlank(agent)) {
            return Response.error(GlobeException.FAIL, "参数错误");
        }
        // 接口超时判断
        if (System.currentTimeMillis() - Long.parseLong(timestamp) > 60000) {
            return Response.error(GlobeException.FAIL_TIMESTAMP_OUT, "验证时间超时");
        }
        // 获取代理信息
        int agentId = Integer.parseInt(agent);
        String agentKey = RedisKeyPrefix.getAgentKey(agentId);
        AgentAccVO accessAgent = this.redisClient.get(agentKey, AgentAccVO.class);
        if (accessAgent == null) {
            accessAgent = BeanUtils.copyProperties(tyOwnerInfoService.getById(agentId),AgentAccVO::new);
            if(null == accessAgent){
                return Response.error(GlobeException.FAIL_NOTEXISTS_AGENT, "代理不存在");
            }

            redisClient.set(agentKey, accessAgent);
            redisClient.expire(agentKey, 5, TimeUnit.MINUTES);
        }
        if (accessAgent.getAgentStatus() == 1) {
            return Response.error(GlobeException.FAIL_NOTEXISTS_AGENT, "代理不存在");
        }
        // 验签
        log.info("待Md5加密串:{}",agent + timestamp + accessAgent.getMd5Key());
        String md5Signature = SecureUtil.md5(agent + timestamp + accessAgent.getMd5Key());
        if (!s.equals(md5Signature)) {
            return Response.error(GlobeException.FAIL_SIGNATURE, "签名错误");
        }
        // 解密参数
        String dParam = DESUtil.decrypt(param, accessAgent.getDesKey());
        if (dParam == null) {
            return Response.error(GlobeException.FAIL_DECRYPT_PARAM, "参数解密失败");
        }

        log.info("参数解密后：" + dParam);
        ParamVO paramVO = JSON.parseObject(dParam, ParamVO.class);
        if (paramVO == null) {
            return Response.error(GlobeException.FAIL, "参数错误");
        }
        if (StringUtils.isBlank(paramVO.getOp())) {
            return Response.error(GlobeException.FAIL_ERR_OP, "请检查操作类型是否正确");
        }
        paramVO.setAgent(agentId);
        paramVO.setIp(IPUtils.getIp(request));
        return Response.successData(paramVO);
    }


    /**
     * @description
     * 查询订单
     **/
    public Response searchOrder(ParamVO param) {
        if (StringUtils.isBlank(param.getOrderId())) {
            return Response.error(GlobeException.FAIL, "订单号不能为空");
        }
        // 查询缓存
        String key = RedisKeyPrefix.getSearchOrderKey(param.getOrderId());
        AccessOrderVO order = redisClient.get(key, AccessOrderVO.class);
        if (order != null) {
            return Response.successData(order);
        }

        order = this.searchOrder(param.getOrderId(), param.getAgent());
        if(order == null){
            return Response.error(GlobeException.FAIL_EXCEPTION, "服务器异常");
        }
        // 放入缓存
        redisClient.set(key, order);
        redisClient.expire(key, 5, TimeUnit.MINUTES);
        return Response.successData(order);
    }

    private AccessOrderVO searchOrder(String orderId, Integer agent) {
        TyAccessOrder order = tAccessorderService.searchOrder(orderId, agent);
        AccessOrderVO vo = BeanUtils.copyProperties(order,AccessOrderVO::new);
        vo.setMoney(order.getOperationScore());
        return vo;
    }


    /**
     * @description
     * 用户登录
     **/
    @Transactional
    public Response<ProcCallBackVO> registerOrLogin(AccountLoginVO loginVO) {
        log.info("用户：{}api登录开始执行,agent:{},入参loginVO:{}",loginVO.getAccount(),loginVO.getAgent(),loginVO);

        // 对agentID的校验
        List<TyOwnerInfo> tccList = tyOwnerInfoService.getAgentByAgentId(loginVO.getAgent(),0);
        if(null == tccList || tccList.isEmpty()) {
            return Response.error(GlobeException.FAIL_NOTEXISTS_AGENT,"代理编号错误活着代理被禁用");
        }

        // 对订单号的校验
        int count = tAccessorderService.getOrderIdCount(loginVO.getOrderId());
        if(count > 0) {
            return Response.error(GlobeException.FAIL_EXISTS_ORDER,"重复订单");
        }

        try{
            String account = loginVO.getAgent() + "_" + loginVO.getSiteCode() + "_" + loginVO.getAccount();

            //添加api订单记录
            TyAccessOrder tyAccessOrder = this.saveAccessOrder(loginVO.getAgent(),account,loginVO.getOrderId(),2,10,loginVO.getMoney());

            BigDecimal beforeScore = BigDecimal.ZERO;
            //查询用户是否存在
            TyAccountInfo tyAccountInfo = tyAccountInfoService.getAccountsInfoByAccounts(account,loginVO.getAgent());
            if(null == tyAccountInfo) {
                //若不存在则创建
                tyAccountInfo = this.saveAccountsInfo(account,loginVO);
            }else{
                // 修改用户金币
                beforeScore = tyAccountInfo.getScore();
                tyAccountInfoService.addUserScore(tyAccountInfo.getUserID(),loginVO.getMoney());
            }

            BigDecimal afterScore = beforeScore.add(loginVO.getMoney()).setScale(2, BigDecimal.ROUND_DOWN);

            //更改api订单记录状态
            new TyAccessOrder().setId(tyAccessOrder.getId()).setOrderState(0).updateById();

            //添加api账变记录
            this.saveApiRecordDrawScore(tyAccountInfo,account,loginVO.getAccount(),loginVO.getMoney(),beforeScore,afterScore,10,"登录");

            ProcCallBackVO vo = new ProcCallBackVO();
            vo.setMoney(afterScore);
            vo.setUserId(tyAccountInfo.getUserID());
            log.info("用户：{}api登录结束执行,返回参数:{}",loginVO.getAccount(),vo);
            return Response.successData("登录成功",vo);

        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.info("api用户:" + loginVO.getAccount() + "登录异常", e);
            return Response.error(GlobeException.FAIL_EXCEPTION, "服务器忙");
        }
    }



    /**
     * @description
     * 获取用户可下分金额
     */
    public Response<ProcCallBackVO> searchLowerScore(String account,Integer agent) {
        try {
            // 校验用户
            TyAccountInfo tyAccountInfo = checkAccountInfo(account, agent);
            BigDecimal score = tyAccountInfo.getScore();

            ProcCallBackVO returnVo = new ProcCallBackVO();
            returnVo.setMoney(score);
            returnVo.setUserId(tyAccountInfo.getUserID());
            log.info("用户:{}可下分金额为:{}",account,score);
            return Response.successData("查询成功",returnVo);
        } catch (GlobeException e) {
            log.info("获取用户可下分金额失败,e:{}",e);
            return Response.error(e.getCode(),e.getMsg());
        } catch (Exception e) {
            log.info("下分系统异常,e:{}", e);
            return Response.error(GlobeException.FAIL_EXCEPTION, "服务器忙");
        }
    }


    /**
     * @description
     * 用户下分
     **/
    @Transactional
    public Response<ProcCallBackVO> lowerScore(UpperScoreVo upperScoreVo) {
        log.info("开始处理api下分订单：{},金额：{},入参:{}",upperScoreVo.getOrderId(),upperScoreVo.getMoney(),upperScoreVo);
        Integer id = 0;
        try{
            // 校验订单号
            this.checkOrderId(upperScoreVo,"下分");
            String account = upperScoreVo.getAgent() + "_" + upperScoreVo.getSiteCode() + "_" + upperScoreVo.getAccount();

            // 校验用户
            TyAccountInfo tyAccountInfo = this.checkAccountInfo(account,upperScoreVo.getAgent());

            // 添加api订单
            TyAccessOrder order = this.saveAccessOrder(upperScoreVo.getAgent(),account,
                    upperScoreVo.getOrderId(),2,30,upperScoreVo.getMoney());
            id = order.getId();

            if(tyAccountInfo.getScore().compareTo(upperScoreVo.getMoney()) == -1) {
                tAccessorderService.supdateAccessOrderById(id);
                log.info("金额不足");
                return Response.error(GlobeException.FAIL_NOT_ENOUGH_MONEY,"金额不足");
            }
            BigDecimal afterMoney = tyAccountInfo.getScore().subtract(upperScoreVo.getMoney());

            //添加账变记录
            this.saveApiRecordDrawScore(tyAccountInfo,account,upperScoreVo.getAccount(),
                    upperScoreVo.getMoney(), tyAccountInfo.getScore(),afterMoney,30,"下分");

            //更新金币
            tyAccountInfoService.subtractUserScore(tyAccountInfo.getUserID(), upperScoreVo.getMoney());

            //更新订单状态
            tAccessorderService.updateAccessOrderById(id);

            // 返回数据
            ProcCallBackVO vo = new ProcCallBackVO();
            vo.setUserId(tyAccountInfo.getUserID());
            vo.setMoney(afterMoney);
            return Response.successData("操作成功",vo);
        } catch (GlobeException e1){
            log.info("下分失败,e1:{}",e1);
            return Response.error(e1.getCode(),e1.getMsg());
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.info("下分系统异常,e:{}", e);
            tAccessorderService.eupdateAccessOrderById(id);
            return Response.error(GlobeException.FAIL_EXCEPTION, "服务器忙");
        }
    }



    /**
     * @description
     * 用户上分
     **/
    @Transactional
    public Response<ProcCallBackVO> upperScore(UpperScoreVo upperScoreVo) {
        log.info("开始处理api上分订单：{},金额：{},入参:{}",upperScoreVo.getOrderId(),upperScoreVo.getMoney(),upperScoreVo);
        Integer id = 0;
        try{
            // 校验订单号
            checkOrderId(upperScoreVo,"上分");

            // 本地账号
            String accounts = upperScoreVo.getAgent() + "_" + upperScoreVo.getSiteCode() + "_" + upperScoreVo.getAccount();
            // 校验用户
            TyAccountInfo tyAccountInfo = checkAccountInfo(accounts,upperScoreVo.getAgent());
            TyAccessOrder order = this.saveAccessOrder(upperScoreVo.getAgent(),accounts,upperScoreVo.getOrderId(),2,20,upperScoreVo.getMoney());
            id = order.getId();

            //添加账变记录
            this.saveApiRecordDrawScore(tyAccountInfo,accounts,upperScoreVo.getAccount(),upperScoreVo.getMoney(),
                    tyAccountInfo.getScore(), tyAccountInfo.getScore().add(upperScoreVo.getMoney()),20,"上分");

            //更新金币
            tyAccountInfoService.addUserScore(tyAccountInfo.getUserID(), upperScoreVo.getMoney());

            //更新订单状态
            tAccessorderService.updateAccessOrderById(id);

            // 返回数据
            ProcCallBackVO vo = new ProcCallBackVO();
            vo.setUserId(tyAccountInfo.getUserID());
            vo.setMoney(tyAccountInfo.getScore().add(upperScoreVo.getMoney()));
            return Response.successData("操作成功",vo);
        } catch (GlobeException e1){
            log.info("上分失败,e1:{}",e1);
            return Response.error(e1.getCode(),e1.getMsg());
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.info("上分系统异常,e:{}",e);
            tAccessorderService.eupdateAccessOrderById(id);
            return Response.error(GlobeException.FAIL_EXCEPTION,"服务器忙");
        }
    }


    /**
     * @description
     * 获取投注列表
     **/
    public Response gameRecord(ParamVO param) {
        String lockKey = RedisKeyPrefix.getGameRecordLockKey(param.getAgent());
        Long lastTimestamp = redisClient.get(lockKey, Long.class);
        if (lastTimestamp != null) {
            return Response.error(GlobeException.FAIL_GAMERECORD_LOCK, "请" + (int) (10 - (System.currentTimeMillis() - lastTimestamp) / 1000) + "秒后再拉取注单");
        }
        if (param.getEndTime() - param.getStartTime() > 1000 * 3600) {
            return Response.error(GlobeException.FAIL_GAMERECORD_TIME_ERR, "注单拉取时间范围太长");
        }

        List<BetData> betDataList = this.betDataService.gameRecord(param);
        redisClient.set(lockKey, System.currentTimeMillis());
        redisClient.expire(lockKey, 10, TimeUnit.SECONDS);
        return Response.successData(BeanUtils.copyProperties(betDataList, GameRecordVO::new));
    }


    private void checkOrderId(UpperScoreVo upperScoreVo,String desc) throws GlobeException {
        TyAccessOrder tAccessOrder = tAccessorderService.getOrderInfoByOrderId(upperScoreVo.getOrderId());
        if(null != tAccessOrder) {
            if(tAccessOrder.getOrderState() > -1 && tAccessOrder.getOrderState() < 2) {
                log.info("订单号已存在");
                throw new GlobeException(GlobeException.FAIL_EXISTS_ORDER,"订单号已存在");
            }else if(tAccessOrder.getOrderState() == 2) {
                log.info("上一个"+ desc +"订单成功处理中");
                new TyAccessOrder().setOwnerId(upperScoreVo.getAgent()).setOrderId(upperScoreVo.getOrderId()).setOrderState(2)
                        .setAccount(upperScoreVo.getAccount()).setOperation(30).setOperationScore(upperScoreVo.getMoney())
                        .setReasonCode(22).insert();
                throw new GlobeException(GlobeException.FAIL_LOCK_MONEY,"上一个"+ desc +"订单成功处理中，请稍后再试");
            }
        }
    }


    private TyAccountInfo checkAccountInfo(String account, Integer agent) throws GlobeException{
        TyAccountInfo tyAccountInfo = tyAccountInfoService.getAccountsInfoByAccounts(account,agent);
        if (null == tyAccountInfo) {
            log.info("账号不存在");
            throw new GlobeException(GlobeException.FAIL_NOTEXISTS_ACCOUNT,"账号不存在");
        }
        return tyAccountInfo;
    }



    /** 添加api账变记录 **/
    private void saveApiRecordDrawScore(TyAccountInfo tyAccountInfo, String account,
                                        String originalAccount, BigDecimal money,
                                        BigDecimal beforeScore, BigDecimal afterScore,
                                        Integer code, String desc){
        new TyRecordDrawScore()
                .setUserId(tyAccountInfo.getUserID())
                .setAccount(account)
                .setOriginalAccount(originalAccount)
                .setSiteCode(tyAccountInfo.getSiteCode())
                .setOwnerId(tyAccountInfo.getOwnerID())
                .setBeforeScore(beforeScore)
                .setAfterScore(afterScore)
                .setOperationScore(money)
                .setInsertDate(new Date())
                .setOperationCode(code)
                .setOperationDescribe(desc)
                .insert();
    }


    /** 保存订单信息 **/
    private TyAccessOrder saveAccessOrder(Integer agent, String account,String orderId,
                                          Integer state,Integer operation,BigDecimal money) {
        TyAccessOrder tyAccessOrder = new TyAccessOrder()
                .setOwnerId(agent)
                .setOrderId(orderId)
                .setOrderState(state)
                .setOperation(operation)
                .setAccount(account)
                .setOperationScore(money);
        tyAccessOrder.insert();
        return tyAccessOrder;
    }

    /** 保存用户信息 **/
    private TyAccountInfo saveAccountsInfo(String account, AccountLoginVO loginVO){
        TyAccountInfo tyAccountInfo = new TyAccountInfo()
                .setAccounts(account)
                .setNickName(loginVO.getAccount())
                .setGender(loginVO.getGender())
                .setFaceId(loginVO.getFaceId())
                .setSiteCode(loginVO.getSiteCode())
                .setOwnerID(loginVO.getAgent())
                .setScore(loginVO.getMoney());
        tyAccountInfo.insert();
        return tyAccountInfo;
    }




}
