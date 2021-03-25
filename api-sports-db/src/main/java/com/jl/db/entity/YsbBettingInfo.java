package com.jl.db.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 投注信息表(第三方回调记录)
 * </p>
 *
 * @author 
 * @since 2021-03-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class YsbBettingInfo extends Model<YsbBettingInfo> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户标识
     */
    private Integer userId;

    /**
     * 游戏编码
     */
    private String gameCode;

    /**
     * ysb交易流水号
     */
    private Long transactionId;

    /**
     * 第三方用户名
     */
    private String userName;

    /**
     * 商户号
     */
    private String vendorId;

    /**
     * 投注金额
     */
    private BigDecimal betAmount;

    /**
     * 投注状态
     */
    private String betStatus;

    /**
     * 总投注笔数
     */
    private Integer totalRecords;

    /**
     * 总投注金额
     */
    private BigDecimal totalBetAmount;

    /**
     * 退款金额
     */
    private BigDecimal refundAmount;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 修改人
     */
    private String updateBy;

    /**
     * 本地订单号
     */
    private String localOrderNo;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
