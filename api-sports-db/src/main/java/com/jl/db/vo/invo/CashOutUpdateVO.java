package com.jl.db.vo.invo;

import com.jl.db.vo.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "投注金额入参封装VO")
public class CashOutUpdateVO extends BaseVO {

    @ApiModelProperty(value = "用户id")
    private Integer userId;

    @ApiModelProperty(value = "提前结算状态 0:提前结算成功,-3:提前结算失败")
    private String status;

    @ApiModelProperty(value = "投注ID/投注流水号")
    private String  betId;
}
