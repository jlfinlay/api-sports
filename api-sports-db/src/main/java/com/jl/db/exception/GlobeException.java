package com.jl.db.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GlobeException extends RuntimeException {	
	private static final long serialVersionUID = 2855111773363461056L;	
	//成功
	public static final String SUCCESS = "0";
	//服务器异常
	public static final String FAIL_EXCEPTION = "-1";
	//参数错误
	public static final String FAIL = "1";
	//代理不存在
	public static final String FAIL_NOTEXISTS_AGENT = "2";
	//验证时间超时
	public static final String FAIL_TIMESTAMP_OUT = "3";
	//账号不存在
	public static final String FAIL_NOTEXISTS_ACCOUNT = "4";
	//白名单错误
	public static final String FAIL_WHITE_LIST = "5";
	//参数签名错误
	public static final String FAIL_SIGNATURE = "6";
	//订单不存在
	public static final String FAIL_NOTEXISTS_ORDER = "7";
	//订单重复
	public static final String FAIL_EXISTS_ORDER = "8";
	//参数解密失败
	public static final String FAIL_DECRYPT_PARAM = "9";
	//不存在的请求（请检查子操作类型是否正确）
	public static final String FAIL_ERR_OP = "10";
	//上下分金额小于等于0或超出最大值
	public static final String FAIL_ERR_MONEY = "11";
	//游戏中等金额被锁定或并发操作,禁止带分登录、上分以及下分
	public static final String FAIL_LOCK_MONEY = "12";
	//金额不足
	public static final String FAIL_NOT_ENOUGH_MONEY = "13";
	//订单号格式错误
	public static final String FAIL_ERR_ORDER = "14";
	//TOKEN信息不存在或已过期
	public static final String FAIL_TIMEOUT_TOKEN = "15";
	//拉单过于频繁
	public static final String FAIL_GAMERECORD_LOCK = "16";
	public static final String FAIL_GAMERECORD_TIME_ERR = "17";
	//代理商金额不足
	public static final String FAIL_AGENT_NOT_ENOUGH_MONEY = "100";
	//游戏不存在或已经被禁用掉
	public static final String FAIL_NOT_AGENT_GAME = "20";
	//游戏牌局号不能为空
	public static final String FAIL_NOT_GAME_CODE = "21";
	
	//上一个上下分订单未处理完成
	public static final String FAIL_LastOrderState = "22";
	
	//操作频繁
	public static final String FAIL_LOCK_OFTEN = "25";
	
    private String code;
    private String msg;

    public GlobeException(String code, String message) {
        this.msg = message;
        this.code = code;
    }
}
