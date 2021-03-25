package com.jl.openapi.service;

import com.alibaba.fastjson.JSON;
import com.jl.db.common.Response;
import com.jl.db.config.redis.RedisKeyPrefix;
import com.jl.db.exception.GlobeException;
import com.jl.db.utils.bean.BeanUtils;
import com.jl.db.utils.redis.RedisClient;
import com.jl.db.vo.invo.AccountLoginVO;
import com.jl.db.vo.outvo.AccountInfoVO;
import com.jl.db.vo.outvo.LoginBackVO;
import com.jl.db.vo.outvo.ParamVO;
import com.jl.db.vo.outvo.ProcCallBackVO;
import com.jl.openapi.config.OpenApiPropertiesConfig;
import com.jl.openapi.utils.MD5Utils;
import com.jl.openapi.utils.RandomUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.util.URLEncoder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class LoginService {

    @Resource
    private OpenApiPropertiesConfig openApiPropertiesConfig;

    @Resource
    private RedisClient redisClient;

    @Resource
    private OpenApiService openApiService;


    /**
     * @description
     * 登录
     */
    public Response login(ParamVO param, String timestamp) {
        log.info("用户登录,入参:param:{}",param);
        /** 添加分布式锁,防止并发 **/
        String redisKey = RedisKeyPrefix.getLogin(param.getAgent() + "_" + param.getSiteCode() + "_" + param.getAccount());
        try {
            if (!redisClient.setDistributedLock(redisKey, redisKey, 5)) {
                throw new GlobeException(GlobeException.FAIL_LOCK_OFTEN, "操作频繁");
            }
            // 校验登录参数
            verifyLoginParam(param,timestamp);

            // 登录
            AccountLoginVO loginVO = BeanUtils.copyProperties(param,AccountLoginVO::new);
            loginVO.setMoney(loginVO.getMoney().setScale(2, BigDecimal.ROUND_DOWN));
            Response<ProcCallBackVO> response = openApiService.registerOrLogin(loginVO);

            if(Response.SUCCESS.equals(response.getCode())){
                ProcCallBackVO back = response.getData();
                log.info("登录返回参数:{}",back);
                LoginBackVO lb = new LoginBackVO();
                String url = RandomUtils.getRandomUrl(openApiPropertiesConfig.getGameUrl().split(","));
                lb.setUrl(url + "?token=" + this.createToken(param));
                return Response.successData(lb);
            }
            return response;
        } catch (GlobeException e){
            return Response.error(e.getCode(),e.getMsg());
        } finally {
            // 释放锁
            redisClient.releaseDistributedLock(redisKey, redisKey);
        }
    }

    /**
     * @description
     * 登录参数校验
     */
    private void verifyLoginParam(ParamVO param, String timestamp) throws GlobeException {
        if (StringUtils.isBlank(param.getOrderId())) {
            throw new GlobeException(GlobeException.FAIL, "订单号不能为空");
        }

        if (StringUtils.isNotBlank(param.getOrderId())
                && !param.getOrderId().equals(param.getAgent() + param.getSiteCode() + timestamp + param.getAccount())) {
            throw new GlobeException(GlobeException.FAIL_ERR_ORDER, "订单号格式错误");
        }

        if (StringUtils.isBlank(param.getSiteCode())) {
            throw new GlobeException(GlobeException.FAIL, "站点编号不能为空");
        }

        if (StringUtils.isBlank(param.getAccount())) {
            throw new GlobeException(GlobeException.FAIL, "账号不能为空");
        }

        if (param.getAccount().length() < 4 || param.getAccount().length() > 20) {
            throw new GlobeException(GlobeException.FAIL, "账号长度太小或太长");
        }
        if (param.getMoney().compareTo(BigDecimal.ZERO) == -1
                || param.getMoney().compareTo(new BigDecimal(10000000)) == 1) {
            throw new GlobeException(GlobeException.FAIL_ERR_MONEY, "金额错误");
        }
    }

    /**
     * 创建TOKEN
     *
     * @param param
     * @return
     */
    private String createToken(ParamVO param) {
        String account = param.getAgent() + "_" + param.getSiteCode() + "_" + param.getAccount();
        String token = MD5Utils.MD5Encode(account + System.currentTimeMillis() + openApiPropertiesConfig.getTokenEncodeKey(), "");
        log.info("生成token-------"+token);
        AccountInfoVO accountVo = new AccountInfoVO();
        accountVo.setAccount(account);
        accountVo.setClientIp(param.getIp());
        String tokenSaveKey = RedisKeyPrefix.getTokenKey(token);
        redisClient.set(tokenSaveKey, accountVo);
        redisClient.expire(tokenSaveKey, 10, TimeUnit.MINUTES);
        return token;
    }
}
