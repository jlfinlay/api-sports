package com.jl.db.vo.outvo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "余额宝描述参数封装VO")
public class YebDescriptionVO {

    @ApiModelProperty(value = "id",name = "id")
    private Integer id;

    @ApiModelProperty(value = "业主id",name = "agentId")
    private Integer agentId;

    @ApiModelProperty(value = "描述内容",name = "yebDescription")
    private String yebDescription;

    @ApiModelProperty(value = "创建时间",name = "createTime")
    private String createTime;
}
