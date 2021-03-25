package com.jl.openapi.game.service;

import com.alibaba.fastjson.JSONObject;
import com.jl.db.vo.invo.BetSelectionVO;
import com.jl.db.vo.invo.BetStakeVO;
import com.jl.db.vo.invo.BetSubmitRefNoVO;
import com.jl.db.vo.invo.BetSubmitVO;
import com.jl.openapi.utils.SHA256Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;


import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;

@Slf4j
public abstract class ThirdGameServiceTemplate implements IThirdGameService {

    /**
     * 注册生成SHA签名串
     * @param paramMap
     * @return
     */
    public String generatorRegisterSign(Map<String,String> paramMap,String secretKey) {
        log.info("用户注册生成SHA签名串开始,入参paramMap:{}",paramMap);
        StringBuffer sb = new StringBuffer();
        Iterator iterator = paramMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            String value = (String) entry.getValue();
            if(StringUtils.isBlank(value)){
                continue;
            }
            sb.append(value).append("|");
        }

        //拼接key
        sb.append(secretKey);
        String signStr = sb.toString();
        log.info("用户注册生成的待签名的串，signStr:{}",signStr);
        String sign = SHA256Utils.getSHA256String(signStr);
        log.info("用户注册生成加密签名串，sign：" + sign);
        return sign;
    }

    /**
     * 获取提前结算列表生成SHA签名串
     * @param paramMap
     * @return
     */
    public String generatorCashOutSign(Map<String,String> paramMap,String secretKey) {
        log.info("用户获取结算生成SHA签名串开始,入参paramMap:{}",paramMap);
        StringBuffer sb = new StringBuffer();
        //"HP":"{ hash password [ UN|VC|SS|DATE|{Secret key} ] }"
        sb.append(paramMap.get("UN")).append("|").append(paramMap.get("VC"))
                .append("|").append(paramMap.get("SS")).append("|").append(paramMap.get("DATE")).append("|");
        //拼接key
        sb.append(secretKey);
        String signStr = sb.toString();
        log.info("用户获取结算生成的待签名的串，signStr:{}",signStr);
        String sign = SHA256Utils.getSHA256String(signStr);
        log.info("用户获取结算生成加密签名串，sign：" + sign);
        return sign;
    }

    /**
     * 获取提前结算状态列表生成SHA签名串
     * @param paramMap
     * @return
     */
    public String generatorCashOutStatusSign(Map<String,String> paramMap,String secretKey) {
        log.info("用户获取结算状态生成SHA签名串开始,入参paramMap:{}",paramMap);
        StringBuffer sb = new StringBuffer();
        //"HP":"{ hash password [ UN|VC|BID|DATE|{Secret key} ] }"
        sb.append(paramMap.get("UN")).append("|").append(paramMap.get("VC"))
                .append("|").append(paramMap.get("BID")).append("|").append(paramMap.get("DATE")).append("|");
        //拼接key
        sb.append(secretKey);
        String signStr = sb.toString();
        log.info("用户获取结算状态生成的待签名的串，signStr:{}",signStr);
        String sign = SHA256Utils.getSHA256String(signStr);
        log.info("用户获取结算状态生成加密签名串，sign：" + sign);
        return sign;
    }


    /**
     * 获取投注列表生成SHA签名串
     * @param paramMap
     * @return
     */
    public String generatorGetBetListSign(Map<String,String> paramMap,String secretKey) {
        log.info("获取投注列表生成SHA签名串开始,入参paramMap:{}",paramMap);
        StringBuffer sb = new StringBuffer();
        //"HP":"{ hash password [ UN|VC|SS|DATE|{Secret key} ] }"
        sb.append(paramMap.get("UN")).append("|").append(paramMap.get("VC"))
                .append("|").append(paramMap.get("SS")).append("|").append(paramMap.get("DATE")).append("|");
        //拼接key
        sb.append(secretKey);
        String signStr = sb.toString();
        log.info("用户获取投注列表生成的待签名的串，signStr:{}",signStr);
        String sign = SHA256Utils.getSHA256String(signStr);
        log.info("用户获取投注列表生成加密签名串，sign：" + sign);
        return sign;
    }




    /**
     * 获取连串过关组合数据生成SHA签名串
     * @param paramMap
     * @return
     */
    public String generatorGetComboSign(Map<String,String> paramMap,String secretKey) {
        log.info("获取连串过关组合数据生成SHA签名串开始,入参paramMap:{}",paramMap);
        StringBuilder sb = new StringBuilder();
        //"HP":"{ hash password [ UN|VC|CID|DATE|{Secret key} ] }
        sb.append(paramMap.get("UN")).append("|").append(paramMap.get("VC"))
                .append("|").append(paramMap.get("CID")).append("|").append(paramMap.get("DATE")).append("|");
        //拼接key
        sb.append(secretKey);
        String signStr = sb.toString();
        log.info("获取连串过关组合数据生成的待签名的串，signStr:{}",signStr);
        String sign = SHA256Utils.getSHA256String(signStr);
        log.info("获取连串过关组合数据生成加密签名串，sign：" + sign);
        return sign;
    }



    /**
     * 用户投注提交生成SHA签名串
     * @param paramMap
     * @return
     */
    public Object generatorbetSubmitSign(Map<String, Object> paramMap, String secretKey) {
        log.info("用户投注提交生成SHA签名串开始,入参paramMap:{}",paramMap);
        StringBuilder sb = new StringBuilder();
        //"HP":"{ hash password [ UN|VC|DATE|{Secret key} ] }"
        sb.append(paramMap.get("UN")).append("|").append(paramMap.get("VC"))
                .append("|").append(paramMap.get("DATE")).append("|");
        //拼接key
        sb.append(secretKey);
        String signStr = sb.toString();
        log.info("用户投注提交生成的待签名串，signStr:{}",signStr);
        Function<String,String> func = SHA256Utils::getSHA256String;
        String sign = func.apply(signStr);
        log.info("用户投注提交生成加密签名串，sign：" + sign);
        return sign;
    }


    /**
     * 获取非滚球跟多赛事数据生成SHA签名串
     * @param paramMap
     * @return
     */
    public String generatorNonLiveBettingMoreSign(Map<String, String> paramMap, String secretkey) {
        log.info("获取非滚球跟多赛事数据生成SHA签名串开始,入参paramMap:{}",paramMap);

        StringBuffer sb = new StringBuffer();
        // //"HP":"{ hash password [ UN|VC|DATE|EI|{Secret key}] }"
        sb.append(paramMap.get("UN")).append("|").append(paramMap.get("VC"))
                .append("|").append(paramMap.get("DATE")).append("|").append(paramMap.get("EI")).append("|");
        //拼接key
        sb.append(secretkey);
        String signStr = sb.toString();
        log.info("获取非滚球跟多赛事数据生成的待签名串，signStr:{}",signStr);
        String sign = SHA256Utils.getSHA256String(signStr);
        log.info("获取非滚球跟多赛事数据生成加密签名串，sign：" + sign);
        return sign;
    }


    /**
     * 获取额外赔率生成SHA签名串
     * @param paramMap
     * @return
     */
    public  String generatorExtraBetOddsSign(Map<String, String> paramMap, String secretkey) {
        log.info("获取额外赔率生成SHA签名串开始,入参paramMap:{}",paramMap);

        //"HP":"{ hash password [ UN|VC|DATE|{Secret key} ] }"
        StringBuffer sb = new StringBuffer();
        sb.append(paramMap.get("UN")).append("|").append(paramMap.get("VC"))
                .append("|").append(paramMap.get("DATE")).append("|");
        //拼接key
        sb.append(secretkey);
        String signStr = sb.toString();
        log.info("获取额外赔率生成的待签名串，signStr:{}",signStr);
        Function<String,String> func = SHA256Utils::getSHA256String;
        String sign = func.apply(signStr);
        log.info("获取额外赔率生成加密签名串，sign：" + sign);
        return sign;
    }


    /**
     * 获取投注限额生成SHA签名串
     * @param paramMap
     * @return
     */
    public  String generatorMaxBetLimitSign(Map<String, String> paramMap, String secretkey) {
        log.info("获取投注限额生成SHA签名串开始,入参paramMap:{}",paramMap);

        //// "HP": "{ hash password [UN|VC|SI|DATE|{Secret key} ] }"
        StringBuffer sb = new StringBuffer();
        sb.append(paramMap.get("UN")).append("|").append(paramMap.get("VC")).append("|")
                .append(paramMap.get("SI")).append("|").append(paramMap.get("DATE")).append("|");
        //拼接key
        sb.append(secretkey);
        String signStr = sb.toString();
        log.info("获取投注限额生成的待签名串，signStr:{}",signStr);
        Function<String,String> func = SHA256Utils::getSHA256String;
        String sign = func.apply(signStr);
        log.info("获取投注限额生成加密签名串，sign：" + sign);
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


    public String assisDate(String dateStr){
        if(StringUtils.isBlank(dateStr)){
            return null;
        }
        String [] date = dateStr.split(" ");
        return date[0]+"T"+date[1];
    }

    // 组装投注参数
    protected JSONObject createSmlbData(BetSubmitVO vo){
        List<BetStakeVO> stakeVOS = vo.getBetStakeList();
        List<Map<String,Object>> stakeList = new ArrayList<>();
        stakeVOS.stream().forEach(x->{
            Map<String,Object> stakemap = new HashMap<>();
            stakemap.put("TYE",x.getSubmitType());
            stakemap.put("STK",x.getStake());
            stakeList.add(stakemap);
        });

        List<BetSelectionVO> selectionVOS = vo.getBetSelectionList();
        List<Map<String,Object>> selectionList = new ArrayList<>();
        selectionVOS.stream().forEach(x->{
            Map<String,Object> selectionMap = new HashMap<>();
            selectionMap.put("SI",x.getSelectionId());
            selectionMap.put("SV",x.getSelectionValue());
            selectionMap.put("DP",x.getDecimalPrice());
            selectionMap.put("SC",x.getScore());
            selectionList.add(selectionMap);
        });

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("bls",stakeList);
        jsonObject.put("sel",selectionList);
        return jsonObject;
    }


    // 组装投注参数(带订单号的)
    protected JSONObject createSmlbRefNoData(BetSubmitRefNoVO vo){
        List<BetStakeVO> stakeVOS = vo.getBetStakeList();
        List<Map<String,Object>> stakeList = new ArrayList<>();
        stakeVOS.stream().forEach(x->{
            Map<String,Object> stakemap = new HashMap<>();
            stakemap.put("TYE",x.getSubmitType());
            stakemap.put("STK",x.getStake());
            stakemap.put("RN",x.getLocalBetNo());
            stakemap.put("sel",getSelData(x));
            stakeList.add(stakemap);
        });
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sels",stakeList);
        return jsonObject;
    }

    private List<Map<String,Object>>  getSelData(BetStakeVO vo){
        List<BetSelectionVO> vos = vo.getBetSelectionList();
        List<Map<String,Object>> selectionList = new ArrayList<>();
        vos.stream().forEach(x->{
            Map<String,Object> selectionMap = new HashMap<>();
            selectionMap.put("SI",x.getSelectionId());
            selectionMap.put("SV",x.getSelectionValue());
            selectionMap.put("DP",x.getDecimalPrice());
            selectionMap.put("SC",x.getScore());
            selectionList.add(selectionMap);
        });
        return selectionList;
    }



    // 获取终端类型
    public String getTerminalType(Integer type){
        //0:PC端 1:H5  3:IOS 4:安卓
        if(type == 0){
            return "Web";
        }else if(type == 1){
            return "Mobile Web";
        }else if(type == 3){
            return "IOS";
        }else {
            return "Android";
        }

    }

}