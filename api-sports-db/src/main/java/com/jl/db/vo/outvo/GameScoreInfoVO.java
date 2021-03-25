package com.jl.db.vo.outvo;


import com.jl.db.vo.BaseVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author 
 * @since 2020-03-11
 */
@Data
@ApiModel(description = "用户金币详情参数封装VO")
public class GameScoreInfoVO extends BaseVO {

    /**
     * 用户 ID
     */
    private Integer userId;

    /**
     * 用户积分（货币）
     */
    private BigDecimal score;

}
