package com.jl.db.vo.invo;

import com.jl.db.vo.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;


@Data
@ApiModel(description = "用户上分入参封装VO")
public class UpperScoreVo extends BaseVO {

    @ApiModelProperty("业主id")
    private Integer agent;

    @ApiModelProperty("账号")
    private String account;

    @ApiModelProperty("金额")
    private BigDecimal money = BigDecimal.ZERO;

    @ApiModelProperty("订单号")
    private String orderId;

    @ApiModelProperty("站点编号")
    private String siteCode;

}
