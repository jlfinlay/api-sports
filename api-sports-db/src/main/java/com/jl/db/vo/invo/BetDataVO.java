package com.jl.db.vo.invo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 本地投注记录表
 * </p>
 *
 * @author 
 * @since 2020-09-23
 */
@Data
@ApiModel(description = "本地投注记录表")
public class BetDataVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "本地投注选项")
    List<BetDataSelectionVO> betDataSelectionList;

    @ApiModelProperty(value = "本地投注投注金额")
    List<BetDataStakeVO> betDataStakeList;

    @ApiModelProperty(value = "主键")
    private Integer id;

    @ApiModelProperty(value = "用户id")
    private Integer userId;

    @ApiModelProperty(value = "业主id")
    private Integer agentId;

    @ApiModelProperty(value = "本地投注单号")
    private String localBetNo;

    @ApiModelProperty(value = "第三方游戏编号")
    private String gameCode;

    @ApiModelProperty(value = "盘口类型 3:欧洲盘 4:印尼盘 5:马来盘 6:香港盘")
    private Integer oddType;

    @ApiModelProperty(value = "投注终端 0:PC端 1:H5  3:IOS 4:安卓")
    private Integer betModel;

    @ApiModelProperty(value = "盘口选项 1:任何盘口 2:任何陪率 3:任何陪率和任何盘口")
    private Integer oddModel;

    @ApiModelProperty(value = "投注状态 0:投注成功 1:投注失败")
    private Integer betStatus;

    @ApiModelProperty(value = "描述信息")
    private String msg;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;


}
