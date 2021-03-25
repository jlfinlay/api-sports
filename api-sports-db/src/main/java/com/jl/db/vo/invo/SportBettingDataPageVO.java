package com.jl.db.vo.invo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(description = "体育投注分页入参数据VO")
public class SportBettingDataPageVO {

    @ApiModelProperty(value = "页码", required = true)
    @NotNull(message = "pageNo不能为空")
    private Integer pageNo = 1;

    @ApiModelProperty(value = "页面大小", required = true)
    @NotNull(message = "pageSize不能为空")
    private Integer pageSize = 10;

    @ApiModelProperty(value = "业主ID", required = true)
    @NotNull(message = "业主ID不能为空")
    private Integer agentId;

    @ApiModelProperty(value = "本地投注编号")
    private String localOrderNo;

    @ApiModelProperty(value = "用户名")
    private String accounts;

    @ApiModelProperty(value = "游戏id")
    private Integer gameId;

    @ApiModelProperty(value = "第三方游戏平台编码,如:YSB", name = "gameCode", required = true, allowableValues = "YSB")
    @NotNull
    private String gameCode;

    @ApiModelProperty(value = "状态 ",name = "status")
    private String status;

    @ApiModelProperty(value = "开始时间 格式:yyyy-MM-dd HH:mm:ss",name = "startTime",required = true)
    private String startTime;

    @ApiModelProperty(value = "结束时间 格式:yyyy-MM-dd HH:mm:ss",name = "endTime",required = true)
    private String endTime;


}
