package com.jl.db.vo.outvo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel(description = "VIP奖励明细返回参数封装VO")
public class UserRewardDetailVO {

	@ApiModelProperty(value = "时间",name = "insertDate")
	private Date insertDate;

	@ApiModelProperty(value = "奖励金额",name = "amount")
	private BigDecimal amount;

	@ApiModelProperty(value = "经验变动",name = "score")
	private BigDecimal score;

	@ApiModelProperty(value = "类型",name = "type")
	private String type;

	@ApiModelProperty(value = "用户id",name = "userId")
	private Integer userId;

	@ApiModelProperty(value = "id",name = "id")
	private Integer id;

	@ApiModelProperty(value = "业主id",name = "parentId")
	private Integer parentId;
}
