package com.jl.db.vo.invo;

import com.jl.db.vo.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(description = "第三方用户信息参数封装VO")
public class YsbAccountInfoVO extends BaseVO {

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
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private Integer userId;

    /**
     * 游戏编码
     */
    @ApiModelProperty(value = "游戏编码")
    private String gameCode;

    /**
     * 第三方账户id
     */
    @ApiModelProperty(value = "第三方账户id")
    private String accountId;

    /**
     * 第三方注册时间
     */
    @ApiModelProperty(value = "第三方注册时间")
    private Date registTime;


}
