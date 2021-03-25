package com.jl.openapi.game.service;


import com.jl.db.common.Response;
import com.jl.db.vo.invo.RegistrationVO;
import com.jl.db.vo.invo.*;

public interface IThirdGameService {

    boolean match(String code);

    /**
     * @description
     *  账户注册
     * @param vo
     * @return
     */
    Response<String> clientRegister(RegistrationVO vo);


    /**
     * @description
     *  投注提交
     * @param vo
     * @return
     */
    Response betSubmit(BetSubmitVO vo);




    /**
     * @description
     *  获取额外赔率
     * @param userId
     * @return
     */
    Response getExtraBetOdds(Integer userId);

    /**
     * @description
     *  获取结算列表
     * @param vo
     * @return
     */
    Response getCashOutList(CashOutPageVO vo);

    /**
     * @description
     *  获取结算状态
     * @param vo
     * @return
     */
    Response getCashOutStatus(CashOutStatusVO vo);

    /**
     * @description
     * 提前结算提交
     * @return
     */
    Response cashOutSubmit(CashOutSubmitVO vo);

    /**
     * @description
     * 获取投注列表
     * @return
     */
    Response getBetList(BetDataListVO vo);

    /**
     * @description
     * 获取连串过关组合数据
     * @return
     */
    Response getComboData(ComboVO vo);

    /**
     * @description
     * 获取投注限额
     * @return
     */
    Response getMaxBetLimit(MaxBetLimitVO vo);


    /**
     * @description
     *  投注提交(带订单号)
     * @param vo
     * @return
     */
    Response betSubmitRefNO(BetSubmitRefNoVO vo);
}


