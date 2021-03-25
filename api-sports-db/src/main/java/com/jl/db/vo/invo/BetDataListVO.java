package com.jl.db.vo.invo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(description = "获取投注列表参数封装VO")
public class BetDataListVO  {
    
    @ApiModelProperty(value = "用户id",name = "userId",required = true)
    @NotNull
    private Integer userId;

    @ApiModelProperty(value = "第三方游戏平台编码,如:YSB", name = "gameCode", required = true, allowableValues = "YSB")
    @NotNull
    private String gameCode;

    @ApiModelProperty(value = "开始时间 格式:yyyy-MM-dd HH:mm:ss",name = "startTime",required = true)
    @NotNull
    private String startTime;

    @ApiModelProperty(value = "结束时间 格式:yyyy-MM-dd HH:mm:ss",name = "endTime",required = true)
    @NotNull
    private String endTime;

    @ApiModelProperty(value = "状态 未派彩:Pending 已派彩:Proces",name = "status",required = true)
    @NotNull
    private String status;


    @ApiModelProperty(name = "pageIndex", value = "页码", required = true)
    @NotNull
    private Integer pageIndex;

    @ApiModelProperty(name = "pageSize", value = "页面大小", required = true)
    @NotNull
    private Integer pageSize;

    @ApiModelProperty(value = "用户名",name = "userName",hidden = true)
    private String userName;

    @ApiModelProperty(value = "类型  所有类型:all  体育:FO  乐透:lotto",name = "type",required = true)
    @NotNull
    private String type;

    @ApiModelProperty(name = "date,时间段 0：今天 1:昨天 2:近7天  3:一个月 4:三个月", value = "时间段 0：今天 1:昨天 2:近7天 3:个月 4:三个月",allowableValues = "0,1,2,3,4", required = true)
    @NotNull
    private Integer date;


}
