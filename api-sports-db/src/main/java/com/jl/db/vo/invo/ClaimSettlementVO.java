package com.jl.db.vo.invo;

import com.jl.db.vo.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

//申请结算
@Data
@ApiModel(description = "申请结算参数封装VO")
public class ClaimSettlementVO extends BaseVO {

    @ApiModelProperty(value = "id",name = "ids", required = true)
    @NotNull
    private List<Integer> ids;

    @ApiModelProperty(value = "金额",name = "sumReward", required = true)
    @NotNull
    private BigDecimal sumReward;

    @ApiModelProperty(value = "用户ID",name = "userId", required = true)
    @NotNull
    private Integer userId;

}
