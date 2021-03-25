package com.jl.db.vo.invo;


import com.jl.db.vo.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(description = "获取投注卡数据封装VO")
public class BettingCardVO extends BaseVO {

    @ApiModelProperty(value = "第三方游戏平台编码,如:YSB")
    private String gameCode;

    @ApiModelProperty(value = "业主id")
    private Integer agentId;

    @ApiModelProperty(value = "用户id")
    private Integer userId;

    @ApiModelProperty(value = "投注选项ID")
    private String selId;

    /**
     * id主键 自增长
     */
    @ApiModelProperty(value = "id主键")
    private Integer id;


    /**
     * 体育赛项ID
     */
    @ApiModelProperty(value = "体育赛项ID")
    private String sportId;

    /**
     * 选项状态 a: 开启/活跃 c:终止/关闭
     */
    @ApiModelProperty(value = "选项状态 a: 开启/活跃 c:终止/关闭")
    private String sportStatus;

    /**
     * 当前体育选项的盘口值
     */
    @ApiModelProperty(value = "当前体育选项的盘口值")
    private String pv;

    /**
     * 在让分盘 V是让分. 比如-1.50 (复数) /1.50 ; 在大小盘 V是总分。比如大１.50
     */
    @ApiModelProperty(value = "在让分盘 V是让分. 比如-1.50 (复数) /1.50 ; 在大小盘 V是总分。比如大１.50")
    private String dataValues;

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
