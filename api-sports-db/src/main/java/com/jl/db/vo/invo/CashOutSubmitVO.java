package com.jl.db.vo.invo;

import com.jl.db.vo.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@ApiModel(description = "提前结算提交参数封装VO")
public class CashOutSubmitVO extends BaseVO {

    @ApiModelProperty(value = "用户id",name = "userId",hidden = true)
    @NotNull
    private Integer userId;

    @ApiModelProperty(value = "第三方游戏平台编码,如:YSB", name = "gameCode", required = true, allowableValues = "YSB")
    @NotNull
    private String gameCode;

    @ApiModelProperty(value = "投注ID/投注流水号",name = "betId",required = true)
    @NotNull
    private String  betId;

    @ApiModelProperty(value = "提前结算金额",name = "betId",required = true)
    @NotNull
    private BigDecimal cashoutAmount;

    @ApiModelProperty(value = "LivID 从getCashOutList接口获取提前结算所得的 liveID",name = "liveId",required = true)
    @NotNull
    private String liveId;

    @ApiModelProperty(value = "提前结算 陪率",name = "odds",required = true)
    @NotNull
    private String odds;

    @ApiModelProperty(value = "提前结算分",name = "value",required = true)
    @NotNull
    private String value;

    @ApiModelProperty(value = "用户名",name = "userName",hidden = true)
    private String userName;


}
