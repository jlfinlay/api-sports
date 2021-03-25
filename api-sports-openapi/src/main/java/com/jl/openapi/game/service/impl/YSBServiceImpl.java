package com.jl.openapi.game.service.impl;



import com.alibaba.fastjson.JSONObject;

import com.jl.db.common.Response;
import com.jl.db.exception.ServiceException;
import com.jl.db.vo.invo.RegistrationVO;
import com.jl.db.vo.invo.*;
import com.jl.openapi.game.config.YsbPropertiesConfig;
import com.jl.openapi.game.service.ThirdGameServiceTemplate;
import com.jl.openapi.utils.HttpClientUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@Slf4j
public class YSBServiceImpl extends ThirdGameServiceTemplate {

    private final static String CODE = "YSB";


    /**会员注册**/
    private static final String CLIENT_REGISTER = "/clients/register";

    /**投注提交**/
    private static final String BET_SUBMIT = "/bet/bet_submit";

    /**投注提交对应投注订单号**/
    private static final String BET_SUBMIT_REFNO = "/bet/bet_submit_refno";


    /**获取额外赔率**/
    private static final String BET_EXTRA_ODDS = "/bet/bet_extra_odds";

    /**获取提前结算列表**/
    private static final String CASH_OUT_GET = "/history/cashout_get";

    /**获取提前结状态**/
    private static final String CASH_OUT_STATUS = "/history/cashout_status";

    /**提前结算提交*/
    private static final String CASH_OUT_SUBMIT = "/history/cashout_submit";

    /**获取投注列表*/
    private static final String GET_BET_LIST = "/history/betlist_get";

    /**获取连串过关数据*/
    private static final String GET_COMBO_DATA = "/history/combo_get";

    /**投注限额**/
    private static final String MAX_BET_LIMIT = "/bet/max_bet_limit";


    @Resource
    private YsbPropertiesConfig config;


    @Override
    public boolean match(String code) {
        return CODE.equals(code);
    }

    private static final Map<Integer, String> betSubmitCodeMap = new HashMap<>();
    static {
        betSubmitCodeMap.put(0, "投注提交成功");
        betSubmitCodeMap.put(-5, "最大限制");
        betSubmitCodeMap.put(120, "重复投注");
        betSubmitCodeMap.put(115, "活动结束");
        betSubmitCodeMap.put(-21, "余额不足");
        betSubmitCodeMap.put(-4, "代理解密校验码错误");
        betSubmitCodeMap.put(-1, "无效的日期");
        betSubmitCodeMap.put(-6, "价格变动");
        betSubmitCodeMap.put(116, "价格变动");
        betSubmitCodeMap.put(117, "价格变动");
        betSubmitCodeMap.put(500, "当前网速不稳定,请稍后再试");
    }

    /**
     * @description
     *  用户注册
     */
    @Override
    public Response<String> clientRegister(RegistrationVO vo) {
        log.info("账户注册,入参vo:{}",vo);
        String url = config.getBaseUrl() + CLIENT_REGISTER;
        // 组装参数
        Map<String,String> paramMap = new LinkedHashMap<>();
        paramMap.put("UN",config.getPrefix() +"_"+vo.getUserId());
        paramMap.put("VC",config.getPrefix());
        paramMap.put("CR",config.getCr());
        paramMap.put("LC",config.getLanguage()); // 语言类型 中文　zh-cn 英文　en
        paramMap.put("DATE", getUtcDate());
        paramMap.put("HP", generatorRegisterSign(paramMap,config.getSecretkey()));

        // 注册
        JSONObject result;
        try {
             result = HttpClientUtils.doPostOfReturnJson(url, JSONObject.toJSONString(paramMap),30000);
        } catch (Exception e) {
            log.info("账户注册请求异常,userId:{},异常信息:{}",vo.getUserId(),e);
            throw new ServiceException("账户注册请求异常:"+e.getMessage());
        }

        log.info("调用注册接口返回,userId:{},result:{}",vo.getUserId(),result);

        // 解析返回数据
        if(result.getInteger(HttpClientUtils.KEY_CODE).intValue() == HttpStatus.SC_OK){
            String accountsId = result.getString(HttpClientUtils.KEY_ENTITY);
            if(StringUtils.isBlank(accountsId)){
                throw new ServiceException("账户注册,AccountID获取失败!userId:"+vo.getUserId());
            }
            // 返回AccountID
            return Response.successData(accountsId);
        }
        return Response.fail("账户注册失败!"+ JSONObject.toJSONString(result));
    }


    /**
     * @description
     *  投注提交
     */
    @Override
    public Response betSubmit(BetSubmitVO vo) {
        log.info("投注提交,入参vo:{}",vo);
        String betSubmitUrl = config.getBaseUrl() + BET_SUBMIT;
        // 组装参数
        Map<String,Object> paramMap = new LinkedHashMap<>();
        paramMap.put("LC",config.getLanguage()); // 语言类型 中文　zh-cn 英文　en
        paramMap.put("UN",config.getPrefix() +"_"+vo.getUserId());
        paramMap.put("VC",config.getPrefix());
        paramMap.put("CR",config.getCr());
        paramMap.put("BM",getTerminalType(vo.getBetModel()));//0:PC端 1:H5  3:IOS 4:安卓
        paramMap.put("OM",String.valueOf(vo.getOddModel()));
        paramMap.put("OT",String.valueOf(vo.getOddType()));
        paramMap.put("DATE", getUtcDate());
        //"HP":"{ hash password [ UN|VC|DATE|{Secret key} ] }"
        paramMap.put("HP", generatorbetSubmitSign(paramMap,config.getSecretkey()));
        paramMap.put("RN", vo.getLocalBetNO()); // 传入本地订单号
        paramMap.put("smlb", createSmlbData(vo));

        // 投注提交
        JSONObject result;
        try {
            result = HttpClientUtils.doPostOfReturnJson(betSubmitUrl, JSONObject.toJSONString(paramMap),30000);
        } catch (Exception e) {
            log.info("投注提交请求异常,userId:{},异常信息:{}",vo.getUserId(),e);
            throw new ServiceException("投注提交请求异常:"+e.getMessage());
        }

        log.info("调用投注提交接口返回,userId:{},result:{}",vo.getUserId(),result);

        // 解析返回数据
        if(result.getInteger(HttpClientUtils.KEY_CODE).intValue() == HttpStatus.SC_OK){
            Integer code = result.getInteger(HttpClientUtils.KEY_ENTITY);
            if(code ==0 ){
                return Response.successData("投注提交成功");
            }else {
                return Response.fail("投注提交失败!"+betSubmitCodeMap.get(code));
            }
        }
        Integer code = result.getInteger(HttpClientUtils.KEY_CODE);
        if(code.intValue()==500){
            return Response.fail("投注提交失败!"+betSubmitCodeMap.get(code));
        }
        Integer entity = result.getInteger(HttpClientUtils.KEY_ENTITY);
        return Response.fail("投注提交失败!"+betSubmitCodeMap.get(entity));
    }


    /**
     * @description
     *  投注提交(带订单号的)
     */
    @Override
    public Response betSubmitRefNO(BetSubmitRefNoVO vo) {
        log.info("投注提交(带订单号的),入参vo:{}",vo);
        String ubmitUrl = config.getBaseUrl() + BET_SUBMIT_REFNO;
        // 组装参数
        Map<String,Object> paramMap = new LinkedHashMap<>();
        paramMap.put("LC",config.getLanguage()); // 语言类型 中文　zh-cn 英文　en
        paramMap.put("UN",config.getPrefix() +"_"+vo.getUserId());
        paramMap.put("VC",config.getPrefix());
        paramMap.put("BM",getTerminalType(vo.getBetModel()));//0:PC端 1:H5  3:IOS 4:安卓
        paramMap.put("CR",config.getCr());
        paramMap.put("OM",String.valueOf(vo.getOddModel()));
        paramMap.put("OT",String.valueOf(vo.getOddType()));
        paramMap.put("DATE", getUtcDate());
        //"HP":"{ hash password [ UN|VC|DATE|{Secret key} ] }"
        paramMap.put("HP", generatorbetSubmitSign(paramMap,config.getSecretkey()));
        paramMap.put("smlb", createSmlbRefNoData(vo));

        // 投注提交
        JSONObject result;
        try {
            result = HttpClientUtils.doPostOfReturnJson(ubmitUrl, JSONObject.toJSONString(paramMap),30000);
        } catch (Exception e) {
            log.info("投注提交请求异常,userId:{},异常信息:{}",vo.getUserId(),e);
            throw new ServiceException("投注提交请求异常:"+e.getMessage());
        }

        log.info("调用投注提交接口返回,userId:{},result:{}",vo.getUserId(),result);

        // 解析返回数据
        if(result.getInteger(HttpClientUtils.KEY_CODE).intValue() == HttpStatus.SC_OK){
            Integer code = result.getInteger(HttpClientUtils.KEY_ENTITY);
            if(code ==0 ){
                return Response.successData("投注提交成功");
            }else {
                return Response.fail("投注提交失败!"+betSubmitCodeMap.get(code));
            }
        }
        Integer entity = result.getInteger(HttpClientUtils.KEY_ENTITY);
        return Response.fail("投注提交失败!"+betSubmitCodeMap.get(entity));
    }



    /**
     * @description
     *  获取结算列表
     */
    @Override
    public Response getCashOutList(CashOutPageVO vo) {
        log.info("获取结算列表,入参vo:{}",vo);
        String cashOutUrl = config.getBaseUrl() + CASH_OUT_GET;

        // 组装参数
        Map<String,String> paramMap = new LinkedHashMap<>();
        paramMap.put("UN",config.getPrefix() +"_"+vo.getUserId());
        paramMap.put("VC",config.getPrefix());
        paramMap.put("ST",vo.getFixedOdds());
        paramMap.put("SS",vo.getStatus());
        paramMap.put("PN",String.valueOf(vo.getPageSize()));
        paramMap.put("PP",String.valueOf(vo.getPageIndex()));
        paramMap.put("FD",assisDate(vo.getStartTime()));//yyyy-MM-ddTHH:mm:ss
        paramMap.put("TD",assisDate(vo.getEndTime()));
        paramMap.put("LC",config.getLanguage()); // 语言类型 中文　zh-cn 英文　en
        paramMap.put("DATE", getUtcDate());
        paramMap.put("HP", generatorCashOutSign(paramMap,config.getSecretkey()));

        // 获取结算列表
        JSONObject result;
        try {
            result = HttpClientUtils.doPostOfReturnJson(cashOutUrl, JSONObject.toJSONString(paramMap),30000);
        } catch (Exception e) {
            log.info("获取结算列表请求异常,userId:{},异常信息:{}",vo.getUserId(),e);
            throw new ServiceException("获取结算列表请求异常:"+e.getMessage());
        }

        log.info("获取结算列表请求异常接口返回,userId:{},result:{}",vo.getUserId(),result);

        // 解析返回数据
        if(result.getInteger(HttpClientUtils.KEY_CODE).intValue() == HttpStatus.SC_OK){
            String data = result.getString(HttpClientUtils.KEY_ENTITY);
            return Response.successData(data);
        }
        return Response.fail("获取结算列表数据失败!"+ JSONObject.toJSONString(result));
    }

    /**
     * @description
     *  获取结算状态
     * @param vo
     * @return
     */
    @Override
    public Response getCashOutStatus(CashOutStatusVO vo) {
        log.info("获取结算状态,入参vo:{}",vo);
        String cashOutStatusUrl = config.getBaseUrl() + CASH_OUT_STATUS;

        // 组装参数
        Map<String,String> paramMap = new LinkedHashMap<>();
        paramMap.put("UN",config.getPrefix() +"_"+vo.getUserId());
        paramMap.put("VC",config.getPrefix());
        paramMap.put("BID",vo.getBetId());
        paramMap.put("DATE", getUtcDate());
        paramMap.put("HP", generatorCashOutStatusSign(paramMap,config.getSecretkey()));

        // 获取结算状态
        JSONObject result;
        try {
            result = HttpClientUtils.doPostOfReturnJson(cashOutStatusUrl, JSONObject.toJSONString(paramMap),30000);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("获取结算状态请求异常,userId:{},异常信息:{}",vo.getUserId(),e);
            throw new ServiceException("获取结算状态请求异常:"+e.getMessage());
        }

        log.info("获取结算状态请求异常接口返回,userId:{},result:{}",vo.getUserId(),result);

        // 解析返回数据
        if(result.getInteger(HttpClientUtils.KEY_CODE).intValue() == HttpStatus.SC_OK){
            String data = result.getString(HttpClientUtils.KEY_ENTITY);
            // 0表示提前结算成功, -3标识提前结算失败
            return Response.successData(data);
        }
        return Response.fail("获取结算状态数据失败!"+ JSONObject.toJSONString(result));
    }


    /**
     * @description
     * 提前结算提交
     * @return
     */
    @Override
    public Response cashOutSubmit(CashOutSubmitVO vo) {
        log.info("提前结算提交,入参vo:{}",vo);
        String cashOutSubmitUrl = config.getBaseUrl() + CASH_OUT_SUBMIT;

        // 组装参数
        Map<String,String> paramMap = new LinkedHashMap<>();
        paramMap.put("UN",config.getPrefix() +"_"+vo.getUserId());
        paramMap.put("VC",config.getPrefix());
        paramMap.put("BID",vo.getBetId());
        paramMap.put("COA",String.valueOf(vo.getCashoutAmount()));
        paramMap.put("LID",vo.getLiveId());
        paramMap.put("O",vo.getOdds());
        paramMap.put("V",vo.getValue());
        paramMap.put("DATE", getUtcDate());
        paramMap.put("HP", generatorCashOutStatusSign(paramMap,config.getSecretkey()));

        // 提前结算提交
        JSONObject result;
        try {
            result = HttpClientUtils.doPostOfReturnJson(cashOutSubmitUrl, JSONObject.toJSONString(paramMap),30000);
        } catch (Exception e) {
            log.info("提前结算提交请求异常,userId:{},异常信息:{}",vo.getUserId(),e);
            throw new ServiceException("提前结算提交请求异常:"+e.getMessage());
        }

        log.info("提前结算提交请求异常接口返回,userId:{},result:{}",vo.getUserId(),result);

        // 解析返回数据
        if(result.getInteger(HttpClientUtils.KEY_CODE).intValue() == HttpStatus.SC_OK){
            String data = result.getString(HttpClientUtils.KEY_ENTITY);
            return Response.successData(data);
        }
        return Response.fail("提前结算提交数据失败!"+ JSONObject.toJSONString(result));
    }


    /**
     * @description
     * 获取投注列表
     * @return
     */
    @Override
    public Response getBetList(BetDataListVO vo) {
        log.info("获取投注列表,入参vo:{}",vo);
        String getBetListUrl = config.getBaseUrl() + GET_BET_LIST;

        // 组装参数
        Map<String,String> paramMap = new LinkedHashMap<>();
        paramMap.put("UN",config.getPrefix() +"_"+vo.getUserId());
        paramMap.put("VC",config.getPrefix());
        paramMap.put("T",vo.getType());
        paramMap.put("SS",vo.getStatus());
        paramMap.put("FD",assisDate(vo.getStartTime()));
        paramMap.put("TD",assisDate(vo.getEndTime()));
        paramMap.put("LC",config.getLanguage());
        paramMap.put("DATE", getUtcDate());
        paramMap.put("HP", generatorGetBetListSign(paramMap,config.getSecretkey()));

        // 获取投注列表
        JSONObject result;
        try {
            result = HttpClientUtils.doPostOfReturnJson(getBetListUrl, JSONObject.toJSONString(paramMap),30000);
        } catch (Exception e) {
            log.info("获取投注列表请求异常,userId:{},异常信息:{}",vo.getUserId(),e);
            throw new ServiceException("获取投注列表请求异常:"+e.getMessage());
        }

        log.info("获取投注列表请求异常接口返回,userId:{},result:{}",vo.getUserId(),result);

        // 解析返回数据
        if(result.getInteger(HttpClientUtils.KEY_CODE).intValue() == HttpStatus.SC_OK){
            String data = result.getString(HttpClientUtils.KEY_ENTITY);
            return Response.successData(data);
        }
        return Response.fail("获取投注列表数据失败!"+ JSONObject.toJSONString(result));
    }

    /**
     * @description
     * 获取连串过关组合数据
     * @return
     */
    @Override
    public Response getComboData(ComboVO vo) {
        log.info("获取连串过关组合数据,入参vo:{}",vo);
        String getComboDataUrl = config.getBaseUrl() + GET_COMBO_DATA;

        // 组装参数
        //"HP":"{ hash password [ UN|VC|CID|DATE|{Secret key} ] }
        Map<String,String> paramMap = new LinkedHashMap<>();
        paramMap.put("UN",config.getPrefix() +"_"+vo.getUserId());
        paramMap.put("VC",config.getPrefix());
        paramMap.put("CID",vo.getComboId());
        paramMap.put("LC",config.getLanguage());
        paramMap.put("DATE", getUtcDate());
        paramMap.put("HP", generatorGetComboSign(paramMap,config.getSecretkey()));

        // 获取连串过关组合数据
        JSONObject result;
        try {
            result = HttpClientUtils.doPostOfReturnJson(getComboDataUrl, JSONObject.toJSONString(paramMap),30000);
        } catch (Exception e) {
            log.info("获取连串过关组合数据请求异常,userId:{},异常信息:{}",vo.getUserId(),e);
            throw new ServiceException("获取连串过关组合数据请求异常:"+e.getMessage());
        }

        log.info("获取连串过关组合数据异常接口返回,userId:{},result:{}",vo.getUserId(),result);

        // 解析返回数据
        if(result.getInteger(HttpClientUtils.KEY_CODE).intValue() == HttpStatus.SC_OK){
            String data = result.getString(HttpClientUtils.KEY_ENTITY);
            return Response.successData(data);
        }
        return Response.fail("获取连串过关组合数据失败!"+ JSONObject.toJSONString(result));
    }

    /**
     * @description
     * 获取投注限额
     * @return
     */
    @Override
    public Response getMaxBetLimit(MaxBetLimitVO vo) {
        log.info("获取投注限额,入参MaxBetLimitVO:{}",vo);
        String maxLlimitUrl = config.getBaseUrl() + MAX_BET_LIMIT;

        // 组装参数
        Map<String,String> paramMap = new LinkedHashMap<>();
        paramMap.put("UN",config.getPrefix() +"_"+vo.getUserId());
        paramMap.put("VC",config.getPrefix());
        paramMap.put("SI",vo.getSelectionId());
        paramMap.put("DO",vo.getDecimalOdds());
        paramMap.put("DATE", getUtcDate());
        paramMap.put("HP", generatorMaxBetLimitSign(paramMap,config.getSecretkey()));

        // 获取投注限额
        JSONObject result;
        try {
            result = HttpClientUtils.doPostOfReturnJson(maxLlimitUrl, JSONObject.toJSONString(paramMap),30000);
        } catch (Exception e) {
            log.info("获取投注限额数据请求异常,userId:{},异常信息:{}",vo.getUserId(),e);
            throw new ServiceException("获取投注限额数据请求异常:"+e.getMessage());
        }

        log.info("获取投注限额数据接口返回,userId:{},result:{}",vo.getUserId(),result);

        // 解析返回数据
        if(result.getInteger(HttpClientUtils.KEY_CODE).intValue() == HttpStatus.SC_OK){
            String data = result.getString(HttpClientUtils.KEY_ENTITY);
            return Response.successData(data);
        }
        return Response.fail("获取投注限额数据失败!"+ JSONObject.toJSONString(result));
    }



    /**
     * @description
     *  获取额外赔率
     */
    @Override
    public Response getExtraBetOdds(Integer userId) {
        log.info("获取额外赔率,入参userId:{}",userId);
        String betExtraoddsUrl = config.getBaseUrl() + BET_EXTRA_ODDS;

        // 组装参数
        Map<String,String> paramMap = new LinkedHashMap<>();
        paramMap.put("UN",config.getPrefix() +"_"+userId);
        paramMap.put("VC",config.getPrefix());
        paramMap.put("DATE", getUtcDate());
        paramMap.put("HP", generatorExtraBetOddsSign(paramMap,config.getSecretkey()));

        // 获取额外赔率
        JSONObject result;
        try {
            result = HttpClientUtils.doPostOfReturnJson(betExtraoddsUrl, JSONObject.toJSONString(paramMap),30000);
        } catch (Exception e) {
            log.info("获取额外赔率数据请求异常,userId:{},异常信息:{}",userId,e);
            throw new ServiceException("获取额外赔率数据请求异常:"+e.getMessage());
        }

        log.info("获取额外赔率数据接口返回,userId:{},result:{}",userId,result);

        // 解析返回数据
        if(result.getInteger(HttpClientUtils.KEY_CODE).intValue() == HttpStatus.SC_OK){
            String data = result.getString(HttpClientUtils.KEY_ENTITY);
            return Response.successData(data);
        }
        return Response.fail("获取额外赔率数据失败!"+ JSONObject.toJSONString(result));
    }



}
