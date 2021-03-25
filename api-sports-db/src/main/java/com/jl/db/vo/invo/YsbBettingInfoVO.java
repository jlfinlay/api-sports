package com.jl.db.vo.invo;


import com.jl.db.vo.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@ApiModel(description = "用户投注信息封装vo")
public class YsbBettingInfoVO extends BaseVO {


    /**
     * id主键 自增长
     */
    @ApiModelProperty(value = "id主键")
    private Integer id;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private Integer userId;

    /**
     * 游戏编码
     */
    @ApiModelProperty(value = "游戏编码")
    private String gameCode;

    /**
     * ysb交易流水号
     */
    @ApiModelProperty(value = "ysb交易流水号")
    private Long transactionId;

    /**
     * 第三方用户名
     */
    @ApiModelProperty(value = "第三方用户名")
    private String userName;

    /**
     * 商户号
     */
    @ApiModelProperty(value = "商户号")
    private String vendorId;

    /**
     * 投注数额
     */
    @ApiModelProperty(value = "投注数额")
    private BigDecimal betAmount;

    /**
     * 投注状态
     */
    @ApiModelProperty(value = "投注状态")
    private String betStatus;

    /**
     * 总投注数量
     */
    @ApiModelProperty(value = "总投注数量")
    private Integer totalRecords;

    /**
     * 总投注额
     */
    @ApiModelProperty(value = "总投注额")
    private BigDecimal totalBetAmount;

    /**
     * 退款数额
     */
    @ApiModelProperty(value = "退款数额")
    private BigDecimal refundAmount;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createBy;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    /**
     * 修改人
     */
    @ApiModelProperty(value = "修改人")
    private String updateBy;

    @ApiModelProperty(value = "投注信息记录")
    List<YsbBettingRecordsVO> bettingRecordsList;


}
