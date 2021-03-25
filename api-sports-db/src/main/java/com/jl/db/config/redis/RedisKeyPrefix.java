package com.jl.db.config.redis;

public class RedisKeyPrefix {

	public static final String API_SPORT_SERVER = "API_SPORTS_SERVER:";


	/**投注提交(带订单号的)**/
	public static final String BET_SUBMIT_REF_NO = API_SPORT_SERVER + "BET_SUBMIT_REF_NO:";

	/**提前结算提交**/
	public static final String CASH_OUT_SUBMIT = API_SPORT_SERVER + "CASH_OUT_SUBMIT:";

	/**投注余额查询**/
	public static final String REQBETAMT = API_SPORT_SERVER + "REQBETAMT:";

	/**投注确认**/
	public static final String BET_CONFIRM = API_SPORT_SERVER + "BET_CONFIRM:";

	/**投注状态确认**/
	public static final String BET_STATUS = API_SPORT_SERVER + "BET_STATUS:";

	/**派彩**/
	public static final String PAY_OUT = API_SPORT_SERVER + "PAY_OUT:";


	/**获取业主信息**/
	public static final String AGENT_INFO = API_SPORT_SERVER + "AGENT_INFO:";
	public static String getAgentKey(Integer agentId){
		return  AGENT_INFO + agentId;
	}

	/**登录**/
	public static final String LOGIN = API_SPORT_SERVER + "LOGIN:";
	public static String getLogin(String key) {
		return LOGIN + key;
	}

	/** 用户token **/
	public static final String USER_TOKEN = API_SPORT_SERVER + "USER_TOKEN:";
	public static String getTokenKey(String token) {
		return USER_TOKEN + token;
	}


	/** 用户上分 **/
	public static final String UPPER_SCORE = API_SPORT_SERVER + "UPPER_SCORE:";
	public static String getUserUpperScore(String key) {
		return UPPER_SCORE + key;
	}

	/** 用户下分 **/
	public static final String LOWER_SCORE = API_SPORT_SERVER + "LOWER_SCORE:";
	public static String getUserLowerScore(String key) {
		return LOWER_SCORE + key;
	}

	/** 订单查询 **/
	public static final String SEARCH_ORDER = API_SPORT_SERVER + "SEARCH_ORDER:";
	public static String getSearchOrderKey(String orderId) {
		return SEARCH_ORDER + orderId;
	}

	/** 拉取注单 **/
	public static final String GAME_RECORD = API_SPORT_SERVER + "GAME_RECORD:";
	public static String getGameRecordLockKey(Integer agent) {
		return GAME_RECORD + agent;
	}
}
