package com.jl.db.vo.invo;

import com.jl.db.vo.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Data
@ApiModel(description = "投注提交入参封装VO")
public class BetSubmitRefNoVO extends BaseVO {

    @ApiModelProperty(value = "用户id",name = "userId",hidden = true)
    @NotNull
    private Integer userId;

    @ApiModelProperty(value = "第三方游戏平台编码,如:YSB", name = "gameCode", required = true, allowableValues = "YSB")
    @NotNull
    private String gameCode;


    @ApiModelProperty(value = "投注终端 0:PC端 1:H5  3:IOS 4:安卓", name = "BetModel", required = true)
    @NotNull
    private Integer betModel;

    @ApiModelProperty(value = "盘口选项 1:任何盘口 2:任何陪率 3:任何陪率和任何盘口", name = "OddModel", required = true)
    @NotNull
    private Integer oddModel;

    @ApiModelProperty(value = "盘口类型 3:欧洲盘 4:印尼盘 5:马来盘 6:香港盘", name = "oddType", required = true)
    @NotNull
    private Integer oddType;

    @ApiModelProperty(value = "投注金额列表", name = "betStakeList", required = true)
    @NotNull
    private List<BetStakeVO> betStakeList;


    @ApiModelProperty(value = "用户名",name = "userName",hidden = true)
    private String userName;

    @ApiModelProperty(value = "总投注额",name = "totalBetAmount",hidden = true)
    private BigDecimal totalBetAmount;

}
