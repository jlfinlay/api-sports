package com.jl.db.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 本地投注记录表
 * </p>
 *
 * @author 
 * @since 2021-03-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ty_bet_data")
public class BetData extends Model<BetData> {

    private static final long serialVersionUID = 1L;
    public static final int BET_SUCCESS = 3; // 投注成功
    public static final int BET_FAIL = 4; // 投注失败
    public static final int NO_PAY_OUT = 0;//是否派彩 0: 未派彩
    public static final int PAY_OUT = 1;//是否派彩  1: 已派彩

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 游戏id
     */
    private Integer gameId;

    /**
     * 业主id
     */
    private Integer agentId;

    /**
     * 提交类型 1:单式投注 2:二串一 3:三串一 ...
     */
    private Integer submitType;

    /**
     * 投注额
     */
    private BigDecimal stake;

    /**
     * 本地投注单号
     */
    private String localBetNo;

    /**
     * 第三方投注ID
     */
    private String bettingId;

    /**
     * 投注类型 1:单式投注 2:串关
     */
    private Integer betType;

    /**
     * 第三方游戏编号
     */
    private String gameCode;

    /**
     * 盘口类型 3:欧洲盘 4:印尼盘 5:马来盘 6:香港盘
     */
    private Integer oddType;

    /**
     * 投注终端 0:PC端 1:H5  3:IOS 4:安卓
     */
    private Integer betModel;

    /**
     * 盘口选项 1:任何盘口 2:任何陪率 3:任何陪率和任何盘口
     */
    private Integer oddModel;

    /**
     * 投注状态 1:提交成功,待确认 2:提交失败 3:投注成功 4:投注失败
     */
    private Integer betStatus;

    /**
     * 描述信息
     */
    private String msg;

    /**
     * 投注详情
     */
    private String betInfo;

    /**
     * 投注时间
     */
    private Date betTime;

    /**
     * 投注人
     */
    private String betBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 是否派彩 0: 未派彩 1: 已派彩
     */
    private Integer isPayout;

    /**
     * 派彩次数 默认0次
     */
    private Integer payoutNum;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
