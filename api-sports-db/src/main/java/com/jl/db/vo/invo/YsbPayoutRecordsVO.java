package com.jl.db.vo.invo;

import com.jl.db.vo.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 投注派彩记录表
 * </p>
 *
 * @author 
 * @since 2020-04-16
 */
@Data
@ApiModel(description = "投注派彩记录表VO")
public class YsbPayoutRecordsVO extends BaseVO {

    @ApiModelProperty(value = "主键id")
    private Integer id;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private Integer userId;

    /**
     * 投注类型
     */
    @ApiModelProperty(value = "投注类型")
    private String betType;

    /**
     * 结算交易流水号
     */
    @ApiModelProperty(value = "结算交易流水号")
    private Long transactionId;

    /**
     * YSB生成的投注ID
     */
    @ApiModelProperty(value = "YSB生成的投注ID")
    private String referenceId;

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
     * 结算/派彩金额
     */
    @ApiModelProperty(value = "结算/派彩金额")
    private BigDecimal payoutAmount;

    /**
     * 结算时间
     */
    @ApiModelProperty(value = "结算时间")
    private String payoutTime;

    /**
     * 投注状态
     */
    @ApiModelProperty(value = "投注状态")
    private String betStatus;

    /**
     * 是否重新结算  0:不重新结算 1:重新结算
     */
    @ApiModelProperty(value = "是否重新结算")
    private String resettlement;

    /**
     * 仅在投注状态=结算时可用,如果下注状态= 10;结算ID为空为状态取消
     */
    @ApiModelProperty(value = "仅在投注状态=结算时可用,如果下注状态= 10;结算ID为空为状态取消")
    private String cashoutId;

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


}
