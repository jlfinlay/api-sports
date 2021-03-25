package com.jl.db.vo.outvo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel(description = "体育投注分页入参数据VO")
public class SportBetDataVO {

    @ApiModelProperty(value = "游戏id")
    private Integer gameId;

    @ApiModelProperty(value = "本地投注编号")
    private String localOrderNo;

    @ApiModelProperty(value = "投注金额")
    private BigDecimal BetAmount;

    @ApiModelProperty(value = "竞猜结果 1:赢 2:输 3:未开奖 ")
    private Integer guessResult;

    @ApiModelProperty(value = "中奖金额: 未中奖或输时值都为0 ,小于0:未开奖")
    private BigDecimal winAmount;

    @ApiModelProperty(value = "系统盈利: 投注金额-中奖金额 ,如果中奖金额是未开奖,系统盈利也是未开奖(小于0)")
    private BigDecimal systemWinAmount;

    @ApiModelProperty(value = "投注时间")
    private Date betDate;

    @ApiModelProperty(value = "投注订单状态 1:投注成功 2:投注失败 3:投注已退回")
    private Integer betStatus ;

    @ApiModelProperty(value = "第三方投注单号")
    private String referenceId;



}
