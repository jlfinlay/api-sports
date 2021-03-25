package com.jl.db.vo.invo;

import com.jl.db.vo.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;


@Data
@ApiModel(description = "用户连串过关组合记录VO")
public class YsbUserComboDataVO extends BaseVO {

    /**
     * id主键 自增长
     */
    @ApiModelProperty(value = "id主键")
    private Integer id;

    /**
     * 用户id
     *
     */
    @ApiModelProperty(value = "用户id")
    private Integer userId;

    /**
     * 投注ID/投注流水号 (唯一的)
     */
    @ApiModelProperty(value = "投注ID/投注流水号 (唯一的)")
    private String betId;

    /**
     * 投注状况(pending = 未决状态/提前结算提交 invalid:无效 void:賽事取消 failed:失败 cashout:提前结算成功)
     */
    @ApiModelProperty(value = "投注状况(pending = 未决状态/提前结算提交 invalid:无效 void:賽事取消 failed:失败 cashout:提前结算成功)")
    private String betStatus;

    /**
     * 下注赔率
     */
    @ApiModelProperty(value = "下注赔率")
    private String betPrice;

    /**
     * 欧洲盘的赔率
     */
    @ApiModelProperty(value = "欧洲盘的赔率")
    private String betOdd;

    /**
     * 赛事id
     */
    @ApiModelProperty(value = "赛事id")
    private String eventId;

    /**
     * 选项ID
     */
    @ApiModelProperty(value = "选项ID")
    private String selectionId;

    /**
     * 连串过关ID
     */
    @ApiModelProperty(value = "连串过关ID")
    private String comboId;

    /**
     * 连串过关状态
     */
    @ApiModelProperty(value = "连串过关状态")
    private String comboStatus;

    /**
     * 连串过关陪率0:不重新结算 1:重新结算
     */
    @ApiModelProperty(value = "连串过关陪率0:不重新结算 1:重新结算")
    private String comboPrice;


    /**
     * 投注额
     */
    @ApiModelProperty(value = "投注额")
    private BigDecimal betAmount;

    /**
     * 选项位置
     */
    @ApiModelProperty(value = "选项位置")
    private String selectionPlace;

    /**
     * 选项分数
     */
    @ApiModelProperty(value = "选项分数")
    private String selectionValue;

    /**
     * 队伍类型 Home/Away /Draw
     */
    @ApiModelProperty(value = "队伍类型 Home/Away /Draw")
    private String teamType;

    /**
     * 投注选项
     */
    @ApiModelProperty(value = "投注选项")
    private String betType;

    /**
     * 投注模式
     */
    @ApiModelProperty(value = "投注模式")
    private String betMode;

    /**
     * 投注日期
     */
    @ApiModelProperty(value = "投注日期")
    private String transactionDate;

    /**
     * 选项名称
     */
    @ApiModelProperty(value = "选项名称")
    private String selectionName;

    /**
     * 赛事名称
     */
    @ApiModelProperty(value = "赛事名称")
    private String eventName;

    /**
     * 比分
     */
    @ApiModelProperty(value = "比分")
    private String score;

    /**
     * 局/节/场/回合
     */
    @ApiModelProperty(value = "/节/场/回合")
    private String eventSession;

    /**
     * 体育名称
     */
    @ApiModelProperty(value = "体育名称")
    private String sportName;

    /**
     * 任何盘口(Any Value) 任何陪率Any Odd）任何陪率和任何盘口(Both）
     */
    @ApiModelProperty(value = "任何盘口(Any Value) 任何陪率Any Odd）任何陪率和任何盘口(Both）")
    private String oddsMode;

    /**
     * 局/节/场
     */
    @ApiModelProperty(value = "局/节/场")
    private String inning;

    /**
     * 体育类型
     */
    @ApiModelProperty(value = "体育类型")
    private String sportType;


    /**
     * 提前结算盘口/分
     */
    @ApiModelProperty(value = "赛事日期")
    private String eventDate;

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


}
