package com.jl.openapi.service;

import cn.hutool.http.HttpStatus;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.jl.db.common.Response;
import com.jl.db.config.redis.RedisKeyPrefix;
import com.jl.db.utils.IPUtils;
import com.jl.db.utils.redis.RedisClient;
import com.jl.db.vo.invo.YsbBettingInfoVO;
import com.jl.db.vo.invo.YsbBettingRecordsVO;
import com.jl.db.vo.invo.YsbPayoutRecordsVO;
import com.jl.db.vo.outvo.GameScoreInfoVO;
import com.jl.openapi.game.config.YsbPropertiesConfig;
import com.jl.openapi.exception.GameCallBackException;
import com.jl.openapi.utils.SHA256Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
public class NotifyService {

    /***获取余额***/
    private static final String GET_BALANCE = "REQBETAMT";

    /***投注状态确认***/
    private static final String BET_STATUS = "BET_STATUS";

    /***投注确认***/
    private static final String BET_CONFIRM = "BETCONFIRM";

    /***派彩***/
    private static final String PAYOUT = "PAYOUT";


    @Resource
    private RedisClient redisClient;

    @Resource
    private YsbPropertiesConfig config;

    @Resource
    private GameService gameService;



    /**
     * @description
     *     第三方投注回调
     * @author finlay
     * @return
     */
    public String  gameCallBack(HttpServletRequest request, JSONObject json) {
        // ip白名单校验
        String ip = IPUtils.getIp(request);
        if (!IPUtils.isContainIp(config.getThirdIp(),ip)) {
            log.info("第三方投注回调来源ip与配置ip不匹配,来源ip:{}",ip);
            throw new GameCallBackException("第三方投注回调来源ip与配置ip不匹配,来源ip:"+ip);
        }

        //获取回调请求参
        if(null == json
                || !json.containsKey("request")
                || StringUtils.isBlank(json.getString("request"))){
            log.info("第三方投注回调失败,请求参数为空");
            throw new GameCallBackException("第三方投注回调失败,请求参数为空");
        }

        // 解析请求参数
        JSONObject resultJson = json.getJSONObject("request");
        if((!resultJson.containsKey("ACT"))
                || StringUtils.isBlank(resultJson.getString("ACT"))){
            throw new GameCallBackException("第三方投注回调失败,参数ACT为空");
        }

        switch (resultJson.getString("ACT")){
            case GET_BALANCE:
                // 获取余额
                return getBalance(resultJson);
            case BET_STATUS:
                // 投注状态确认
                return betStatus(resultJson);
            case BET_CONFIRM:
                // 投注确认 成功或失败都会确认,失败加款
                return betConfirmation(resultJson);
            case PAYOUT:
                // 派彩
                return payOut(resultJson);
            default:
                throw new GameCallBackException("第三方投注回调失败,无法识别的回调类型");
        }
    }




    /*****************************************派彩*****************************************/

    /**
     * @description
     * 第三方回调,派彩
     */
    private String payOut(JSONObject json){
        log.info("第三方投注回调,派彩,入参json:{}",json);

        // 验证签名
        checkPayOutSign(json);
        // YSB 生成的交易流水号。第三方将原样返回，用于审核合法返回用途。
        Long betTransactionId = json.getLong("TRX");

        // 获取userId 前缀_userId
        String userName = json.getString("UN");
        String userId = userName.split("_")[1];

        // 加锁
        String key = RedisKeyPrefix.PAY_OUT + userId + betTransactionId;
        try {
            if (!redisClient.setDistributedLock(key, String.valueOf(betTransactionId), 3)) {
                throw new GameCallBackException("第三方回调派彩失败：请求太频繁，请稍后重试");
            }

            // 入库
            YsbPayoutRecordsVO vo = new YsbPayoutRecordsVO();
            vo.setUserId(Integer.parseInt(userId));
            vo.setTransactionId(betTransactionId);
            vo.setUserName(userName);
            GameScoreInfoVO gameScoreInfoVO = saveBetPayOutData(json,vo);

            // 获取余额 返回添加返还后的余额
            BigDecimal balance = null == gameScoreInfoVO ? BigDecimal.ZERO : gameScoreInfoVO.getScore();

            // 组装返回参数
            Map<String, Object> param = new LinkedHashMap<>();
            param.put("ACT", json.getString("ACT"));
            param.put("TRX", betTransactionId); // YSB 生成的交易流水号
            param.put("UN", userName); // 前缀_userId
            param.put("CC", "RMB");
            param.put("BAL", balance);
            param.put("S", 0); // 状态值 0:成功
            param.put("ED", ""); // 状态值 0:成功
            param.put("DATE", getUtcDate());
            param.put("HP", generatorReqBetamtSign(param, config.getSecretkey()));
            log.info("第三方投注回调,派彩成功,返回数据,balanceMap:{}", param);

            JSONObject returnJson = new JSONObject();
            // 返回第三方数据
            returnJson.put("response", param);
            return returnJson.toJSONString();

        } finally {
            redisClient.releaseDistributedLock(key, String.valueOf(betTransactionId));
        }
    }

    /**
     * @description
     * 第三方回调,派彩数据入库
     */
    private GameScoreInfoVO saveBetPayOutData(JSONObject json, YsbPayoutRecordsVO vo) {
        log.info("第三方投注回调,派彩数据入库,入参,json:{}",json);
        vo.setBetType(json.getString("BETTYPE"));
        vo.setReferenceId(json.getString("REFID"));
        vo.setVendorId(json.getString("VID"));
        vo.setPayoutAmount(json.getBigDecimal("PA"));
        vo.setPayoutTime(json.getString("PT"));
        vo.setBetStatus(json.getString("S"));
        vo.setResettlement(json.getString("RESETTLEMENT"));
        vo.setCashoutId(json.getString("CASHOUTID"));
        Response<GameScoreInfoVO> result = gameService.saveBetPayOutData(vo);
        if(String.valueOf(HttpStatus.HTTP_OK).equals(result.getCode())){
            return result.getData();
        }else {
            throw new GameCallBackException(result.getMsg());
        }
    }


    /**
     * @description
     * 第三方回调,派彩验签
     */
    private void checkPayOutSign(JSONObject json){
        log.info("第三方投注回调,派彩验签开始,入参:{}",json);
        String sourceSign = json.getString("HP");
        log.info("第三方投注回调,派彩验签,原签名串,sourceSign:{}",sourceSign);

        // "HP": "Hash(PAYOUT|711|1|206803700|surewin|888|RMB|18.50|2020-02-02T13:59:59|1|0||2020-02-02 16:59:59|cnkHC1Hsdo)"
        StringBuilder sb = new StringBuilder();
        sb.append(json.getString("ACT")).append("|")
                .append(json.getString("TRX")).append("|")
                .append(json.getString("BETTYPE")).append("|")
                .append(json.getString("REFID")).append("|")
                .append(json.getString("UN")).append("|")
                .append(json.getString("VID")).append("|")
                .append(json.getString("CC")).append("|")
                .append(json.getString("PA")).append("|")
                .append(json.getString("PT")).append("|")
                .append(json.getString("S")).append("|")
                .append(json.getString("RESETTLEMENT")).append("|")
                .append(json.getString("CASHOUTID")).append("|")
                .append(json.getString("DATE")).append("|")
                .append(config.getSecretkey());

        String signStr = sb.toString();
        log.info("第三方投注回调派彩生成的待签名串，signStr:{}",signStr);
        String sign = SHA256Utils.getSHA256String(signStr);
        log.info("第三方投注回调派彩生成加密签名串，sign：" + sign);

        if(!sourceSign.equalsIgnoreCase(sign)){
            throw new GameCallBackException("第三方投注回调失败,派彩验签失败");
        }
    }


    /*****************************************投注状态确认*****************************************/

    /**
     * @description
     * 第三方回调,投注状态确认
     */
    private String betStatus(JSONObject json) {
        log.info("第三方投注回调,投注状态确认,入参json:{}",json);
        // 验证签名
        checkBetStatusSign(json);

        // 获取userId 前缀_userId
        String userName = json.getString("UN");
        String userId = userName.split("_")[1];
        String refNo = json.getString("RN");

        // 加锁
        String key = RedisKeyPrefix.BET_STATUS + userId + refNo;
        try {
            if (!redisClient.setDistributedLock(key, key, 3)) {
                throw new GameCallBackException("第三方回调投注状态确认失败：请求太频繁，请稍后重试");
            }
            // 入库
            saveBetStatusData(json);
            // 组装返回参数
            Map<String, Object> param = new LinkedHashMap<>();
            param.put("ACT", json.getString("ACT"));
            param.put("UN", userName); // 前缀_userId
            param.put("VC", json.getString("VC"));
            param.put("RN", json.getString("RN"));
            param.put("RESP", 0); // 状态值 0:成功
            param.put("DATE", getUtcDate());
            param.put("HP", generatorReqBetamtSign(param, config.getSecretkey()));
            log.info("第三方投注状态确认,返回数据,param:{}", param);

            JSONObject returnJson = new JSONObject();
            // 返回第三方数据
            returnJson.put("response", param);
            return returnJson.toJSONString();

        } finally {
            redisClient.releaseDistributedLock(key, key);
        }
    }

    /**
     * @description
     * 第三方回调,投注状态确认数据入库
     */
    private void saveBetStatusData(JSONObject json){
        Response result = gameService.saveBetStatusData(json);
        if(!String.valueOf(HttpStatus.HTTP_OK).equals(result.getCode())){
            throw new GameCallBackException(result.getMsg());
        }
    }

    /**
     * @description
     * 第三方回调,投注状态确认验签
     */
    private void checkBetStatusSign(JSONObject json){
        log.info("第三方投注回调,投注状态确认,验签开始,入参:{}",json);
        String sourceSign = json.getString("HP");
        log.info("第三方投注回调,投注状态确认,原签名串,sourceSign:{}",sourceSign);

        //Hash(BET_STATUS|UN|VC|RN|STATUS|BID|TT|ERR_CODE|ODDS|VALUE|DATE|{Secretkey})
        StringBuilder sb = new StringBuilder();
        sb.append(json.getString("ACT")).append("|")
                .append(json.getString("UN")).append("|")
                .append(json.getString("VC")).append("|")
                .append(json.getString("RN")).append("|")
                .append(json.getString("STATUS")).append("|")
                .append(json.getString("BID")).append("|")
                .append(json.getString("TT")).append("|")
                .append(json.getString("ERR_CODE")).append("|")
                .append(json.getString("ODDS")).append("|")
                .append(json.getString("VALUE")).append("|")
                .append(json.getString("DATE")).append("|")
                .append(config.getSecretkey());

        String signStr = sb.toString();
        log.info("第三方投注回调投注状态确认生成的待签名串，signStr:{}",signStr);
        String sign = SHA256Utils.getSHA256String(signStr);
        log.info("第三方投注回调投注状态确认生成加密签名串，sign：" + sign);

        if(!sourceSign.equalsIgnoreCase(sign)){
            throw new GameCallBackException("第三方投注回调失败,投注状态确认验签失败");
        }
    }


    /*********************** 投注确认 ***************************/

    /**
     * @description
     * 第三方回调,投注确认
     */
    private String betConfirmation(JSONObject json){
        log.info("第三方投注回调,投注确认,入参json:{}",json);

        // 验证签名
        checkBetConfirmationSign(json);

        // YSB 生成的交易流水号。第三方将原样返回，用于审核合法返回用途。
        Long betTransactionId = json.getLong("TRX");

        // 获取userId 前缀_userId
        String userName = json.getString("UN");
        String userId = userName.split("_")[1];

        // 加锁
        String key = RedisKeyPrefix.BET_CONFIRM + userId + betTransactionId;
        try {
            if (!redisClient.setDistributedLock(key, key, 2)) {
                throw new GameCallBackException("第三方回调投注确认失败：请求太频繁，请稍后重试");
            }

            // 入库
            YsbBettingInfoVO vo = new YsbBettingInfoVO();
            vo.setUserId(Integer.parseInt(userId));
            vo.setTransactionId(betTransactionId);
            vo.setUserName(userName);
            GameScoreInfoVO gameScoreInfoVO = saveBetConfirmationData(json,vo);

            // 获取余额 返回添加返还后的余额
            BigDecimal balance = null == gameScoreInfoVO ? BigDecimal.ZERO : gameScoreInfoVO.getScore();

            // 组装返回参数
            Map<String, Object> param = new LinkedHashMap<>();
            param.put("ACT", json.getString("ACT"));
            param.put("TRX", betTransactionId); // YSB 生成的交易流水号
            param.put("UN", userName); // 前缀_userId
            param.put("CC", "RMB");
            param.put("BAL", balance);
            param.put("S", 0); // 状态值 0:成功
            param.put("DATE", getUtcDate());
            param.put("HP", generatorReqBetamtSign(param, config.getSecretkey()));
            log.info("第三方投注回调,获取余额成功,返回数据,balanceMap:{}", param);

            JSONObject returnJson = new JSONObject();
            // 返回第三方数据
            returnJson.put("response", param);
            return returnJson.toJSONString();

        } finally {
            redisClient.releaseDistributedLock(key, key);
        }
    }

    /**
     * @description
     * 第三方回调,投注确认入库
     */
    private GameScoreInfoVO saveBetConfirmationData(JSONObject json, YsbBettingInfoVO vo ){
        log.info("第三方投注回调,投注确认,数据入库,入参,json:{}",json);
        vo.setBetStatus(json.getString("S"));
        vo.setTotalRecords(json.getInteger("RECS"));
        vo.setTotalBetAmount(json.getBigDecimal("TOTAL"));
        vo.setRefundAmount(json.getBigDecimal("REFUND"));
        vo.setUpdateBy("SYSTEM");
        vo.setUpdateTime(new Date());

        // 解析records
        if(json.containsKey("records")){
            List<YsbBettingRecordsVO> list = new ArrayList<>();
            JSONArray redordArray = json.getJSONArray("records");
            if(null != redordArray && redordArray.size()>0){
                for(int i = 0;i<redordArray.size();i++){
                    JSONObject one = redordArray.getJSONObject(i);
                    if(null != one && one.containsKey("REFID")){
                        YsbBettingRecordsVO recordsVO = new YsbBettingRecordsVO();
                        recordsVO.setBetAmount(one.getBigDecimal("AMT"));
                        recordsVO.setReferenceId(one.getString("REFID"));
                        recordsVO.setBetType(one.getString("BETTYPE"));
                        recordsVO.setBetModel(one.getString("BETMODE"));
                        recordsVO.setEventId(one.getString("EVID"));
                        recordsVO.setEventName(one.getString("EVNAME"));
                        recordsVO.setSelectionName(one.getString("SELNAME"));
                        recordsVO.setBetTypeId(one.getString("BETTYPEID"));
                        recordsVO.setOdds(one.getString("ODDS"));
                        recordsVO.setOddValues(one.getString("VALUE"));
                        recordsVO.setOddFormat(one.getString("ODDFORMAT"));
                        recordsVO.setSportName(one.getString("SPORT"));
                        list.add(recordsVO);
                    }

                }
            }
            if(list.size()>0){
                vo.setBettingRecordsList(list);
            }
        }

        Response<GameScoreInfoVO> result = gameService.saveBetConfirmationData(vo);
        if(String.valueOf(HttpStatus.HTTP_OK).equals(result.getCode())){
            return result.getData();
        }else {
            throw new GameCallBackException(result.getMsg());
        }


    }

    /**
     * @description
     * 第三方回调,投注确认验签
     */
    private void checkBetConfirmationSign(JSONObject json){
        log.info("第三方投注回调,投注确认,验签开始,入参:{}",json);
        String sourceSign = json.getString("HP");
        log.info("第三方投注回调,投注确认,原签名串,sourceSign:{}",sourceSign);

        //Hash(ACT|TRX|UN|CC|S|RECS|TOTAL|REFUND|DATE|Secret Key)
        StringBuilder sb = new StringBuilder();
        sb.append(json.getString("ACT")).append("|")
                .append(json.getString("TRX")).append("|")
                .append(json.getString("UN")).append("|")
                .append(json.getString("CC")).append("|")
                .append(json.getString("S")).append("|")
                .append(json.getString("RECS")).append("|")
                .append(json.getString("TOTAL")).append("|")
                .append(json.getString("REFUND")).append("|")
                .append(json.getString("DATE")).append("|")
                .append(config.getSecretkey());

        String signStr = sb.toString();
        log.info("第三方投注回调投注确认生成的待签名串，signStr:{}",signStr);
        String sign = SHA256Utils.getSHA256String(signStr);
        log.info("第三方投注回调投注确认生成加密签名串，sign：" + sign);

        if(!sourceSign.equalsIgnoreCase(sign)){
            throw new GameCallBackException("第三方投注回调失败,投注确认验签失败");
        }
    }


    /**********************************************获取余额*******************************************************/

    /**
     * @description
     * 第三方回调,获取余额
     */
    private String  getBalance( JSONObject json) {
        log.info("第三方投注回调,获取余额,入参json:{}", json);
        // YSB 生成的交易流水号。第三方将原样返回，用于审核合法返回用途。
        Long betTransactionId = json.getLong("TRX");

        // 获取userId 前缀_userId
        String userName = json.getString("UN");
        String userId = userName.split("_")[1];

        // 验签
        checkReqBetamtSign(json);


        // 数据入库,扣除投注数额
        YsbBettingInfoVO vo = new YsbBettingInfoVO();
        vo.setUserId(Integer.parseInt(userId));
        vo.setTransactionId(betTransactionId);
        vo.setUserName(userName);
        GameScoreInfoVO scoreVo = saveReqBetamtInfo(json ,vo);

        // 获取余额 返回扣除(投注数额)后
        BigDecimal balance = null == scoreVo ? BigDecimal.ZERO : scoreVo.getScore();

        // 组装返回参数
        Map<String, Object> param = new LinkedHashMap<>();
        param.put("ACT", json.getString("ACT"));
        param.put("TRX", betTransactionId); // YSB 生成的交易流水号
        param.put("UN", userName); // 前缀_userId
        param.put("VID", config.getVid()); // 商户号
        param.put("CC", "RMB");
        param.put("BAL", balance);
        param.put("S", 0); // 状态值 0:成功
        param.put("ED", ""); // 错误信息描述
        param.put("DATE", getUtcDate());
        param.put("HP", generatorReqBetamtSign(param, config.getSecretkey()));
        log.info("第三方投注回调,获取余额成功,返回数据,balanceMap:{}", param);

        JSONObject returnJson = new JSONObject();
        // 返回第三方数据
        returnJson.put("response", param);
        return returnJson.toJSONString();
    }


    /**
     * @description
     * 第三方回调,数据入库
     */
    private GameScoreInfoVO saveReqBetamtInfo(JSONObject json, YsbBettingInfoVO vo){
        log.info("第三方投注回调,获取余额,数据入库,入参,json:{},vo:{}",json,vo);
        vo.setGameCode("YSB");
        vo.setVendorId(json.getString("VID"));
        vo.setBetAmount(json.getBigDecimal("AMT"));
        vo.setCreateTime(new Date());
        vo.setCreateBy("SYSTEM");
        log.info("第三方投注回调数据入库,入参,YsbBettingInfoVO:{}", vo);

        Response<GameScoreInfoVO> result = gameService.saveReqBetamtInfo(vo);
        if(Response.SUCCESS.equals(result.getCode())){
            return result.getData();
        }else {
            throw new GameCallBackException(result.getMsg());
        }
    }

    /**
     * @description
     * 第三方回调,获取余额验签
     */
    private void checkReqBetamtSign(JSONObject json){
        log.info("第三方投注回调,获取余额,验签开始,入参:{}",json);
        String sourceSign = json.getString("HP");
        log.info("第三方投注回调,原签名串,sourceSign:{}",sourceSign);

        StringBuilder sb = new StringBuilder();
        sb.append(json.getString("ACT")).append("|")
                .append(json.getString("TRX")).append("|")
                .append(json.getString("UN")).append("|")
                .append(json.getString("VID")).append("|")
                .append(json.getString("CC")).append("|")
                .append(json.getString("AMT")).append("|")
                .append(json.getString("DATE")).append("|")
                .append(config.getSecretkey());

        String signStr = sb.toString();
        log.info("第三方投注回调获取余额生成的待签名串，signStr:{}",signStr);
        String sign = SHA256Utils.getSHA256String(signStr);
        log.info("第三方投注回调获取余额生成加密签名串，sign：" + sign);

        if(!sourceSign.equalsIgnoreCase(sign)){
            throw new GameCallBackException("第三方投注回调失败,获取余额验签失败");
        }
    }

    /**
     * 第三方投注回调生成SHA签名串
     * @param paramMap
     * @return
     */
    public String generatorReqBetamtSign(Map<String,Object> paramMap,String secretKey) {
        log.info("第三方投注回调生成SHA签名串开始,入参paramMap:{}",paramMap);
        StringBuffer sb = new StringBuffer();
        Iterator iterator = paramMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            Object value =  entry.getValue();
            sb.append(value).append("|");
        }

        //拼接key
        sb.append(secretKey);
        String signStr = sb.toString();
        log.info("第三方投注回调生成的待签名的串，signStr:{}",signStr);
        String sign = SHA256Utils.getSHA256String(signStr);
        log.info("第三方投注回调生成加密签名串，sign：" + sign);
        return sign;
    }


    /**
     * @description
     *  获取UTC时间
     * @return
     */
    public String getUtcDate(){
        OffsetDateTime now = OffsetDateTime.now( ZoneOffset.UTC );
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'hh:mm:ss");
        String  DT = now.format(formatter);
        log.info("DT:{}", DT);
        return DT;
    }

}
