package com.jl.db.vo.outvo;


import com.jl.db.vo.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;


@Data
@ApiModel(description = "功能开关状态说明参数封装vo")
public class AgentSystemStatusInfoVO extends BaseVO {

    @ApiModelProperty(value= "id")
    private Integer id;

    @ApiModelProperty(value= "状态名称")
    private String statusName;

    @ApiModelProperty(value= "状态值")
    private BigDecimal statusValue;

    @ApiModelProperty(value= "状态字符")
    private String statusString;

    @ApiModelProperty(value= "状态显示名称")
    private String statusTip;

    @ApiModelProperty(value= "描述")
    private String statusDescription;

    @ApiModelProperty(value= "是否显示 0:显示 1:不显示")
    private Integer isShow;

    @ApiModelProperty(value= "业主id")
    private Integer agentId;

    @ApiModelProperty(value = "app类型 1:棋牌 2:体育")
    private Integer appType;


}
