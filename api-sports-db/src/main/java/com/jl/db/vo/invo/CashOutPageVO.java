package com.jl.db.vo.invo;

import com.jl.db.vo.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(description = "获取提前结算列表分页参数封装VO")
public class CashOutPageVO extends BaseVO {

    @ApiModelProperty(value = "用户id",name = "userId",required = true)
    @NotNull
    private Integer userId;

    @ApiModelProperty(value = "第三方游戏平台编码,如:YSB", name = "gameCode", required = true, allowableValues = "YSB")
    @NotNull
    private String gameCode;

    @ApiModelProperty(value = "页码",name = "pageIndex",required = true)
    @NotNull
    private Integer pageIndex;

    @ApiModelProperty(value = "页数",name = "pageSize",required = true)
    @NotNull
    private Integer pageSize;

    @ApiModelProperty(value = "开始时间 格式:yyyy-MM-dd HH:mm:ss",name = "startTime",required = true)
    @NotNull
    private String startTime;

    @ApiModelProperty(value = "结束时间 格式:yyyy-MM-dd HH:mm:ss",name = "endTime",required = true)
    @NotNull
    private String endTime;

    @ApiModelProperty(value = "状态 未派彩:Pending 已派彩:Proces",name = "status",required = true)
    @NotNull
    private String status;

    @ApiModelProperty(value = "固定赔率 体育:FO  乐透:lotto",name = "fixedOdds",required = true)
    @NotNull
    private String fixedOdds; //固定赔率

    @ApiModelProperty(value = "用户名",name = "userName",hidden = true)
    private String userName;


}
