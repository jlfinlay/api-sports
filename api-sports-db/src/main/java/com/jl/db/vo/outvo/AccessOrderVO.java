package com.jl.db.vo.outvo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccessOrderVO {
	private Integer orderState;
	private Integer reasonCode;
	private String operation;
	private BigDecimal money;
}
