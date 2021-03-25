package com.jl.db.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 本地投注选项表
 * </p>
 *
 * @author 
 * @since 2021-03-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ty_bet_data_selection")
public class BetDataSelection extends Model<BetDataSelection> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 本地投注记录表ID,即:bet_data.ID
     */
    private String betDataId;

    /**
     * 投注选项ID
     */
    private String selectionId;

    /**
     * 选项的盘口值
     */
    private String selectionValue;

    /**
     * 赔率
     */
    private String decimalPrice;

    /**
     * 比分
     */
    private String score;

    /**
     * 赛事名称(如:利物浦 vs 曼联,阿森纳 vs 伯恩利...)
     */
    private String eventName;

    /**
     * 选项名称(如:和局,总分以上,阿森纳....)
     */
    private String selectionName;

    /**
     * 投注选项  独赢 让分盘 亚洲让分盘...(参考appendix A.5 投注选项)
     */
    private String betTypeId;

    /**
     * 体育赛项名称(如:soccer,tennis,basketball)
     */
    private String sportName;

    /**
     * 创建时间
     */
    private Date createTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
