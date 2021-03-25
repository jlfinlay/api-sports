package com.jl.db.vo.invo;



import com.jl.db.vo.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 投注信息记录表
 * </p>
 *
 * @author 
 * @since 2020-04-15
 */
@Data
@ApiModel(description = "投注信息记录表VO")
public class YsbBettingRecordsVO extends BaseVO {


    /**
     * id主键 自增长
     */
    @ApiModelProperty(value = "主键id")
    private Integer id;

    /**
     * 投注信息表id
     */
    @ApiModelProperty(value = "投注信息表id")
    private Integer bettingId;

    /**
     * 投注数额
     */
    @ApiModelProperty(value = "投注数额")
    private BigDecimal betAmount;

    /**
     * YSB生成的投注ID
     */
    @ApiModelProperty(value = "YSB生成的投注ID")
    private String referenceId;

    /**
     * 投注类型
     */
    @ApiModelProperty(value = "投注类型")
    private String betType;

    /**
     * 投注终端
     */
    @ApiModelProperty(value = "投注终端")
    private String betModel;

    /**
     * 赛事ID
     */
    @ApiModelProperty(value = "赛事ID")
    private String eventId;

    /**
     * 赛事名称
     */
    @ApiModelProperty(value = "赛事名称")
    private String eventName;

    /**
     * 选项名称
     */
    @ApiModelProperty(value = "选项名称")
    private String selectionName;

    /**
     * 投注类型id
     */
    @ApiModelProperty(value = "投注类型id")
    private String betTypeId;

    /**
     * 赔率
     */
    @ApiModelProperty(value = "赔率")
    private String odds;

    /**
     * 盘口
     */
    @ApiModelProperty(value = "盘口")
    private String oddValues;

    /**
     * 盘口格式
     */
    @ApiModelProperty(value = "盘口格式")
    private String oddFormat;

    /**
     * 体育名称
     */
    @ApiModelProperty(value = "体育名称")
    private String sportName;

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

}
