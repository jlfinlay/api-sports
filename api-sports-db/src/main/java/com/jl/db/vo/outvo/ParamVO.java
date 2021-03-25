package com.jl.db.vo.outvo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ParamVO {
	private Integer agent;
	
	private String op;
	
	private String account;
	
	private BigDecimal money = BigDecimal.ZERO;
	
	private String siteCode;
	
	private String orderId;
	
	private String ip;
	
	//private int gameId;
	
	private long startTime;
	
	private long endTime;

	private int gameType ;

	
	private String returnUrl;
	
	private String gameCode;
}


