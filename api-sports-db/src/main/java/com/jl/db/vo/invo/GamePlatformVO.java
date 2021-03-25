package com.jl.db.vo.invo;


import com.jl.db.vo.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "会员列表列表分页数据封装类")
public class GamePlatformVO extends BaseVO {

    @ApiModelProperty("页码")
    private Integer pageIndex;

    @ApiModelProperty("页面大小")
    private Integer pageSize;

    @ApiModelProperty("游戏分类ID")
    private Integer typeId;

    @ApiModelProperty("平台分类ID")
    private Integer gameKindID;

    @ApiModelProperty("业主ID")
    private Integer agentId;


    @ApiModelProperty("游戏名字")
    private String gameName;

}
