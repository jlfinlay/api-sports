package com.jl.db.vo.invo;

import com.jl.db.vo.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@ApiModel(description = "投注金额入参封装VO")
public class BetStakeVO extends BaseVO {

    @ApiModelProperty(value = "提交类型",name = "submitType",required = true)
    @NotNull
    private Integer submitType;

    @ApiModelProperty(value = "投注金额",name = "stake",required = true)
    @NotNull
    private BigDecimal stake;

    @ApiModelProperty(value = "投注选项列表", name = "betSelectionList", required = true)
    @NotNull
    private List<BetSelectionVO> betSelectionList;

    @ApiModelProperty(value = "本地投注订单号",name = "referenceNumber",hidden = true)
    private String localBetNo;


    @ApiModelProperty(value = "投注状态", name = "betStatus 1:提交成功,待确认 2:提交失败 3:投注成功 4:投注失败", hidden = true)
    private Integer betStatus;

    @ApiModelProperty(value = "描述", hidden = true)
    private String errMsg;

    @ApiModelProperty(value = "投注时间", hidden = true)
    private Date betTime;

}
