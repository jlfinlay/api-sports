package com.jl.db.vo.invo;

import com.jl.db.vo.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


@Data
@ApiModel(description = "体育项目管理参数vo")
public class SportGameItemVO extends BaseVO {


    /**
     * id主键 自增长
     */
    @ApiModelProperty(value = "id")
    private Integer id;

    /**
     * 业主id
     */
    @ApiModelProperty(value = "业主id")
    private Integer agentId;

    /**
     * 游戏名称
     */
    @ApiModelProperty(value = "游戏名称")
    private String sportName;

    /**
     * 游戏类型
     */
    @ApiModelProperty(value = "游戏类型")
    private String sportType;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer sort;

    /**
     * 第三方状态 0:开启 1:关闭
     */
    @ApiModelProperty(value = "第三方状态 0:开启 1:关闭")
    private Integer thirdStatus;

    /**
     * 游戏状态 0:显示 1:不显示
     */
    @ApiModelProperty(value = "游戏状态 0:显示 1:不显示")
    private Integer gameStatus;

    /**
     * 维护状态 0:正常 1:维护
     */
    @ApiModelProperty(value = "维护状态 0:正常 1:维护")
    private Integer defendStatus;

    /**
     * 竞价包状态 0:显示 1:不显示
     */
    @ApiModelProperty(value = "竞价包状态 0:显示 1:不显示")
    private Integer priceStatus;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;


}
