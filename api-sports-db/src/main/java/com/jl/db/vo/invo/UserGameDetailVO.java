package com.jl.db.vo.invo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserGameDetailVO {

    @ApiModelProperty(value = "平台名称")
    private String kindName;

    @ApiModelProperty(value = "游戏名称")
    private String gameName ;

    @ApiModelProperty(value = "输赢额")
    private BigDecimal score;

    @ApiModelProperty(value = "投注时间")
    private String betTime;
}
