package com.jl.db.vo.invo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;


@Data
@ApiModel(description = "用户信息参数封装VO")
public class AccountsVO  {

    @ApiModelProperty(value = "用户帐号")
    private String accounts;

    @ApiModelProperty(value = "用户昵称")
    private String nickName;

    @ApiModelProperty(value = "头像标识")
    private Integer faceId;

    @ApiModelProperty(value = "用户性别")
    private Integer gender;

    @ApiModelProperty(value = "生日")
    private Date birthday;

    @ApiModelProperty(value = "可用资金")
    private BigDecimal score;

    @ApiModelProperty(value = "vip等级")
    private Integer vipLevel;

    @ApiModelProperty(value = "vip等级名称")
    private String vipLevelName;





}
