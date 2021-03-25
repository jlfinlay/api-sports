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
 * 用户投注列表
 * </p>
 *
 * @author 
 * @since 2021-03-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class YsbUserBetData extends Model<YsbUserBetData> {

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
     * 投注ID/投注流水号 (唯一的)
     */
    private String betId;

    /**
     * 投注状况(pending = 未决状态/提前结算提交 invalid:无效 void:賽事取消 failed:失败 cashout:提前结算成功)
     */
    private String betStatus;

    /**
     * 下注赔率
     */
    private String betPrice;

    /**
     * 欧洲盘的赔率
     */
    private String betOdd;

    /**
     * 赛事id
     */
    private String eventId;

    /**
     * 选项ID
     */
    private String selectionId;

    /**
     * 连串过关ID
     */
    private String comboId;

    /**
     * 连串过关状态
     */
    private String comboStatus;

    /**
     * 连串过关陪率0:不重新结算 1:重新结算
     */
    private String comboPrice;

    /**
     * 连串过关按赛事来计算
     */
    private String comboNumber;

    /**
     * 投注额
     */
    private BigDecimal betAmount;

    /**
     * 选项位置
     */
    private String selectionPlace;

    /**
     * 选项分数
     */
    private String selectionValue;

    /**
     * 队伍类型 Home/Away /Draw
     */
    private String teamType;

    /**
     * 投注选项
     */
    private String betType;

    /**
     * 投注模式
     */
    private String betMode;

    /**
     * 投注日期
     */
    private String transactionDate;

    /**
     * 选项名称
     */
    private String selectionName;

    /**
     * 赛事名称
     */
    private String eventName;

    /**
     * 比分
     */
    private String score;

    /**
     * 局/节/场/回合
     */
    private String eventSession;

    /**
     * 体育名称
     */
    private String sportName;

    /**
     * 任何盘口(Any Value) 任何陪率Any Odd）任何陪率和任何盘口(Both）
     */
    private String oddsMode;

    /**
     * 局/节/场
     */
    private String inning;

    /**
     * 体育类型
     */
    private String sportType;

    /**
     * 盘口格式
     */
    private String oddFormat;

    /**
     * 母赛事名称
     */
    private String parentSportName;

    /**
     * 赛事成绩
     */
    private String eventResult;

    /**
     * 角球
     */
    private String corner;

    /**
     * 红牌
     */
    private String booking;

    /**
     * 黄牌
     */
    private String yellowCard;

    /**
     * 赛事状态
     */
    private String eventStatus;

    /**
     * 是否滚球 yes:是 no:否
     */
    private String isLive;

    /**
     * 可否提前结算？yes:是 no:否
     */
    private String cashoutable;

    /**
     * 如果可以提前结算，liveid 是个数字。可以用来提交提前结算。如果不可以提前结算，liveid是空
     */
    private String liveId;

    /**
     * 从滚球赛事获取
     */
    private String liveEventId;

    /**
     * 提前结算数额
     */
    private BigDecimal cashoutAmount;

    /**
     * 提前结算陪率
     */
    private String cashoutOdd;

    /**
     * 提前结算盘口/分
     */
    private String cashoutValue;

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


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
