package com.jl.db.vo.invo;

import com.jl.db.vo.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(description = "获取连串过关组合参数封装VO")
public class ComboVO extends BaseVO {

    @ApiModelProperty(value = "用户id",name = "userId",required = true)
    @NotNull
    private Integer userId;

    @ApiModelProperty(value = "第三方游戏平台编码,如:YSB", name = "gameCode", required = true, allowableValues = "YSB")
    @NotNull
    private String gameCode;

    @ApiModelProperty(value = "连串过关投注ID / 连串过关投注流水号",name = "comboId",required = true)
    @NotNull
    private String  comboId;

    @ApiModelProperty(value = "用户名",name = "userName",hidden = true)
    private String userName;

}
