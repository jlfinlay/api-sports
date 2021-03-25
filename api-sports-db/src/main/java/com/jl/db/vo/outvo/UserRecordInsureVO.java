package com.jl.db.vo.outvo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel(description = "余额宝交易记录返回参数封装VO")
public class UserRecordInsureVO {

	@ApiModelProperty(value = "创建时间",name = "dateTime")
	private Date dateTime;

	@ApiModelProperty(value = "交易类型",name = "tradeType")
	private Integer tradeType;

	@ApiModelProperty(value = "变动金额",name = "changeScore")
	private BigDecimal changeScore;

	@ApiModelProperty(value = "余额宝金额",name = "beforeScore")
	private BigDecimal beforeScore;
}
