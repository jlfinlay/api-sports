package com.jl.db.vo.invo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(description = "投注选项入参封装VO")
public class BetSelectionVO {

    //投注选项ID
    @ApiModelProperty(value = "投注选项ID",name = "selectionId",required = true)
    @NotNull
    private String selectionId;

    // 盘口值
    @ApiModelProperty(value = "盘口值",name = "userId",required = true)
    @NotNull
    private String selectionValue;

    // 赔率
    @ApiModelProperty(value = "赔率",name = "userId",required = true)
    @NotNull
    private String decimalPrice;

    //比分
    @ApiModelProperty(value = "比分",name = "score",required = true)
    @NotNull
    private String score;

    // 赛事名称
    @ApiModelProperty(value = "赛事名称(如:利物浦 vs 曼联,阿森纳 vs 伯恩利...)",name = "eventName",required = true)
    @NotNull
    private String eventName;

    // 选项名称
    @ApiModelProperty(value = "选项名称(和局,总分以上,阿森纳....)",name = "selectionName",required = true)
    @NotNull
    private String selectionName;

    @ApiModelProperty(value = "投注选项 1:独赢 2:让分盘 3:亚洲让分盘...(参考appendix A.5 投注选项)",name = "betTypeId",required = true)
    @NotNull
    private String betTypeId;

    //赛项名称
    @ApiModelProperty(value = "体育赛项名称(如:soccer,tennis,basketball)",name = "sportName",required = true)
    @NotNull
    private String sportName;

}
