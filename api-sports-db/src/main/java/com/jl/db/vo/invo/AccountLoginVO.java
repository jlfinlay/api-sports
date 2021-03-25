package com.jl.db.vo.invo;

import com.jl.db.vo.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;


@Data
@ApiModel(description = "用户登录入参封装VO")
public class AccountLoginVO extends BaseVO {

    @ApiModelProperty("业主id")
    private Integer agent;

    @ApiModelProperty("账号")
    private String account;

    @ApiModelProperty("金额")
    private BigDecimal money;

    @ApiModelProperty("订单号")
    private String orderId;

    @ApiModelProperty("站点标识")
    private String siteCode;

    @ApiModelProperty("登录ip")
    private String ip;

    @ApiModelProperty("头像标识")
    private Integer faceId = 1;

    @ApiModelProperty("性别")
    private Integer gender = 1;



}
