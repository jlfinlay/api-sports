package com.jl.db.vo.invo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GameScoreVO {
	private BigDecimal score;
	
	private BigDecimal insureScore;
	
	private Integer userId;
	
	private String kindName;
	
	private BigDecimal gold;
	
	private String note;
	
	private Integer recordId;

	private Integer appType;
}
