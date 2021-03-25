package com.jl.db.vo.invo;

import com.jl.db.vo.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(description = "获取提前结算状态参数封装VO")
public class CashOutStatusVO extends BaseVO {

    @ApiModelProperty(value = "用户id",name = "userId",hidden = true)
    @NotNull
    private Integer userId;

    @ApiModelProperty(value = "第三方游戏平台编码,如:YSB", name = "gameCode", required = true, allowableValues = "YSB")
    @NotNull
    private String gameCode;

    @ApiModelProperty(value = "投注ID/投注流水号",name = "betId",required = true)
    @NotNull
    private String  betId;

    @ApiModelProperty(value = "用户名",name = "userName",hidden = true)
    private String userName;


}
