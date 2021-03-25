package com.jl.openapi.service;


import com.jl.db.common.Response;
import com.jl.db.config.redis.RedisKeyPrefix;
import com.jl.db.exception.GlobeException;
import com.jl.db.utils.bean.BeanUtils;
import com.jl.db.utils.redis.RedisClient;
import com.jl.db.vo.invo.UpperScoreVo;
import com.jl.db.vo.outvo.ParamVO;
import com.jl.db.vo.outvo.ProcCallBackVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Slf4j
@Service
public class LowerScoreService {

    @Resource
    private RedisClient redisClient;

    @Resource
    private OpenApiService openApiService;


    public Response searchLowerScore(String account, Integer agent) {
        if (StringUtils.isBlank(account)) {
            return Response.error(GlobeException.FAIL, "账号不能为空");
        }
        Response<ProcCallBackVO> response = openApiService.searchLowerScore(account,agent);
        log.info("查询可下分返回数据:{}",response);
        return response;
    }

    public Response lowerScore(ParamVO param, String timestamp) {
        log.info("用户下分,入参:param:{}",param);

        /** 添加分布式锁,防止并发 **/
        String redisKey = RedisKeyPrefix.getUserLowerScore(param.getAgent() + "_" + param.getSiteCode() + "_" + param.getAccount());
        try {
            if (!redisClient.setDistributedLock(redisKey, redisKey, 5)) {
                throw new GlobeException(GlobeException.FAIL_LOCK_OFTEN, "操作频繁");
            }

            // 参数校验
            verifyLowerScoreParam(param,timestamp);

            // 下分
            UpperScoreVo upperScoreVo = BeanUtils.copyProperties(param,UpperScoreVo::new);
            upperScoreVo.setMoney(upperScoreVo.getMoney().setScale(2, BigDecimal.ROUND_DOWN));
            Response<ProcCallBackVO> response = openApiService.lowerScore(upperScoreVo);
            log.info("用户下分返回参数:{}",response);
            return response;
        } catch (GlobeException e){
            return Response.error(e.getCode(),e.getMsg());
        } finally {
            // 释放锁
            redisClient.releaseDistributedLock(redisKey, redisKey);
        }
    }

    private void verifyLowerScoreParam(ParamVO param, String timestamp) throws GlobeException {
        if (param.getMoney().compareTo(BigDecimal.ZERO) != 1
                || param.getMoney().compareTo(new BigDecimal(10000000)) == 1) {
            throw new GlobeException(GlobeException.FAIL_ERR_MONEY, "金额错误");
        }
        if (StringUtils.isBlank(param.getOrderId())) {
            throw new GlobeException(GlobeException.FAIL, "订单号不能为空");
        }
        if (!StringUtils.isBlank(param.getOrderId()) && !param.getOrderId().equals(param.getAgent() + param.getSiteCode() + timestamp + param.getAccount())) {
            throw new GlobeException(GlobeException.FAIL_ERR_ORDER, "订单号格式错误");
        }
        if (StringUtils.isBlank(param.getSiteCode())) {
            throw new GlobeException(GlobeException.FAIL, "站点编号不能为空");
        }
        if (StringUtils.isBlank(param.getAccount())) {
            throw new GlobeException(GlobeException.FAIL, "账号不能为空");
        }
    }


}
