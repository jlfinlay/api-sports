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
 * 投注派彩记录表
 * </p>
 *
 * @author 
 * @since 2021-03-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class YsbPayoutRecords extends Model<YsbPayoutRecords> {

    private static final long serialVersionUID = 1L;
    public static final int  WIN=1; // 赢
    public static final int  LOSE=2; // 输
    public static final int  VOID=3; // 赛事取消
    public static final int  WIN_HALF=5; // 赢半
    public static final int  LOSE_HALF=6; // 输半
    public static final int  CANCEL=10; // 提前结算取消


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
     * 投注类型
     */
    private String betType;

    /**
     * ysb结算交易流水号
     */
    private Long transactionId;

    /**
     * YSB生成的投注ID
     */
    private String referenceId;

    /**
     * 第三方用户名
     */
    private String userName;

    /**
     * 商户号
     */
    private String vendorId;

    /**
     * 结算/派彩金额
     */
    private BigDecimal payoutAmount;

    /**
     * 结算时间
     */
    private String payoutTime;

    /**
     * 投注状态
     */
    private String betStatus;

    /**
     * 是否重新结算  0:不重新结算 1:重新结算
     */
    private Integer resettlement;

    /**
     * 仅在投注状态=结算时可用,如果下注状态= 10;结算ID为空为状态取消
     */
    private String cashoutId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 业主id
     */
    private Integer agentId;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
