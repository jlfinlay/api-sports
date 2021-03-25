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
 * 投注信息记录表(第三方回调记录)
 * </p>
 *
 * @author 
 * @since 2021-03-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class YsbBettingRecords extends Model<YsbBettingRecords> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    /**
     * 投注信息表id，取自表ysb_betting_info.id
     */
    private Integer bettingId;

    /**
     * 投注数额
     */
    private BigDecimal betAmount;

    /**
     * YSB生成的投注ID
     */
    private String referenceId;

    /**
     * 投注类型
     */
    private String betType;

    /**
     * 投注终端
     */
    private String betModel;

    /**
     * 赛事ID
     */
    private String eventId;

    /**
     * 赛事名称
     */
    private String eventName;

    /**
     * 选项名称
     */
    private String selectionName;

    /**
     * 投注类型id
     */
    private String betTypeId;

    /**
     * 赔率
     */
    private String odds;

    /**
     * 盘口
     */
    private String oddValues;

    /**
     * 盘口格式
     */
    private String oddFormat;

    /**
     * 体育名称
     */
    private String sportName;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private String createBy;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
