package com.jl.db.vo.invo;

import com.jl.db.vo.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(description = "获取投注限额参数封装VO")
public class MaxBetLimitVO extends BaseVO {

    @ApiModelProperty(value = "用户id",name = "userId",hidden = true)
    @NotNull
    private Integer userId;

    @ApiModelProperty(value = "第三方游戏平台编码,如:YSB", name = "gameCode", required = true, allowableValues = "YSB")
    @NotNull
    private String gameCode;

    @ApiModelProperty(value = "投注选项id",name = "selectionId",required = true)
    @NotNull
    private String  selectionId;

    @ApiModelProperty(value = "欧洲盘赔率(无论客户选哪个盘口，您都传入欧州盘的陪率。)",name = "decimalOdds",required = true)
    @NotNull
    private String  decimalOdds;

    @ApiModelProperty(value = "用户名",name = "userName",hidden = true)
    private String userName;



}
