package com.jl.db.vo.invo;

import com.jl.db.vo.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(description = "获取投注列表参数封装VO")
public class BettingRecordPageVO extends BaseVO {

    @ApiModelProperty(value = "类型 5.视讯，4.电子，2.棋牌,3.捕鱼",name = "type",required = true)
    @NotNull
    private Integer type;

    @ApiModelProperty(name = "pageIndex", value = "页码", required = true)
    @NotNull
    private Integer pageIndex;

    @ApiModelProperty(name = "pageSize", value = "页面大小", required = true)
    @NotNull
    private Integer pageSize;

    @ApiModelProperty(value = "用户id",name = "userId",hidden = true)
    private Integer userId;

    @ApiModelProperty(value = "业主id",name = "agentId",hidden = true)
    private Integer agentId;


    @ApiModelProperty(value = "开始时间",name = "startTime")
    private String startTime;

    @ApiModelProperty(value = "结束时间",name = "endTime")
    private String endTime;


}
