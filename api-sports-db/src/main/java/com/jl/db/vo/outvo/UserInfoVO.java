package com.jl.db.vo.outvo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserInfoVO {
	private Integer level;
	
	private String mobilePhone;
	
	private String cardNo;
	
	private String cardOwner;
	
	private String registerDate;
	
	private Integer userId;
	
	private String startTime;
	
	private String endTime;
	
	private Integer activityId;
	
	private String ip;
	
	private Integer vipLevel;
	
	private BigDecimal score; 
}
