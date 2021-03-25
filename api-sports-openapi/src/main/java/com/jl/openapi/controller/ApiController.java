package com.jl.openapi.controller;


import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSON;
import com.jl.db.common.Response;
import com.jl.db.exception.GlobeException;
import com.jl.db.vo.outvo.ParamVO;
import com.jl.openapi.service.OpenApiService;
import com.jl.openapi.service.LoginService;
import com.jl.openapi.service.LowerScoreService;
import com.jl.openapi.service.UpperScoreService;
import com.jl.openapi.utils.DESUtil;
import com.jl.openapi.utils.MD5Utils;
import com.jl.openapi.utils.TimeUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@Api(tags = "API接入接口")
@RequestMapping("openApi")
public class ApiController {

    @Resource
    private OpenApiService openApiService;

    @Resource
    private LoginService loginService;

    @Resource
    private UpperScoreService upperScoreService;

    @Resource
    private LowerScoreService lowerScoreService;

    /**
     * 主入口
     *
     * @param timestamp
     * @param param
     * @param s
     * @param
     * @param agent
     * @return
     */
    @GetMapping("/channel")
    @ResponseBody
    public Response channel(HttpServletRequest request,
                            @RequestParam(value = "timestamp") String timestamp,
                            @RequestParam(value = "param") String param,
                            @RequestParam(value = "s") String s,
                            @RequestParam(value = "agent") String agent) {
        log.info("API接口接到请求信息：" + JSON.toJSONString(request.getParameterMap()));
        Response<ParamVO> result = openApiService.verification(request,agent,timestamp,s,param);
        if(Response.SUCCESS.equals(result.getCode())) {
            ParamVO paramVO = result.getData();
            switch (paramVO.getOp()) {
                case "10":
                    return this.login(paramVO, timestamp);
                case "11":
                    return this.searchOrder(paramVO);
                case "20":
                    return this.upperScore(paramVO, timestamp);
                case "30":
                    return this.lowerScore(paramVO, timestamp);
                case "31":
                    return this.searchLowerScore(paramVO);
                case "40":
                    return this.gameRecord(paramVO);
                default:
                    return Response.error(GlobeException.FAIL_ERR_OP, "请检查操作类型是否正确");
            }
        }
        return result;
    }


   /* public static void main(String[] args) {
        Long time = System.currentTimeMillis();
        String deskey = "qwe123qaz";
        String md5key = "qqq123456";
        Integer agent = 160;
        String siteCode = "A01";
        String account = "finlay123";
        System.out.println("time:"+time);
        Map<String, Object> map = new HashMap<>();
        map.put("op","10");
        map.put("account",account);
        map.put("money",1000);
        map.put("siteCode",siteCode);
        map.put("orderId",agent + siteCode + time + account);
        map.put("ip","127.0.0.1");
        System.out.println("param:"+ DESUtil.encrypt(JSON.toJSONString(map),deskey));
        System.out.println("待Md5加密串:" +agent + time + md5key);
        System.out.println("s:" + SecureUtil.md5(agent + "" + time + md5key));
    } */

   
    /**
     * 登录
     * @param param
     * @return
     */
    private Response login(ParamVO param, String timestamp) {
        log.info("用户{}登录，时间{}",param.getAgent() + "_" + param.getSiteCode() + "_" + param.getAccount(), TimeUtil.getNow());
        return loginService.login(param,timestamp);
    }

    /**
     * 上分
     * @param param
     * @return
     */
    private Response upperScore( ParamVO param, String timestamp) {
        log.info("用户{}上分，时间{}",param.getAgent() + "_" + param.getSiteCode() + "_" + param.getAccount(),TimeUtil.getNow());
        return upperScoreService.upperScore(param,timestamp);
    }


    /**
     * 查询可下分
     * @param param
     * @return
     */
    private Response searchLowerScore(ParamVO param) {
        String account = param.getAgent() + "_" + param.getSiteCode() + "_" + param.getAccount();
        log.info("用户：{}查询可下分操作,时间:{}",account,TimeUtil.getNow());
        return lowerScoreService.searchLowerScore(account,param.getAgent());
    }


    /**
     * 下分
     * @param param
     * @return
     */
    private Response lowerScore(ParamVO param, String timestamp) {
        log.info("用户{}下分，时间{}",param.getAgent() + "_" + param.getSiteCode() + "_" + param.getAccount(),TimeUtil.getNow());
        return lowerScoreService.lowerScore(param,timestamp);
    }


    /**
     * 订单查询
     * @param param
     * @return
     */
    private Response searchOrder(ParamVO param) {
        log.info("订单查询,订单号:{}，时间{},入参:{}",param.getOrderId(),TimeUtil.getNow(),param);
        return openApiService.searchOrder(param);
    }


    /**
     * 注单详情
     * @param param
     * @return
     */
    private Response gameRecord(ParamVO param) {
        log.info("注单详情,时间{},入参:{}",TimeUtil.getNow(),param);
        return openApiService.gameRecord(param);
    }

}
