package com.jl.db.vo.invo;


import com.jl.db.vo.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(description = "获取投注卡数据入参封装VO")
public class BettingCardDataVO extends BaseVO {

    @ApiModelProperty(value = "第三方游戏平台编码,如:YSB", name = "gameCode", required = true, allowableValues = "YSB")
    @NotNull
    private String gameCode;

    @ApiModelProperty(value = "业主id", name = "agentId", required = true)
    @NotNull
    private Integer agentId;

    @ApiModelProperty(value = "用户id", name = "userId", required = true)
    @NotNull
    private Integer userId;

    @ApiModelProperty(value = "投注选项ID", name = "selId", required = true)
    @NotNull
    private String selId;


}
