package com.jl.db.vo.invo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 本地投注投注金额表
 * </p>
 *
 * @author 
 * @since 2020-09-23
 */
@Data
public class BetDataStakeVO implements Serializable{

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Integer id;

    /**
     * 本地投注记录表ID
     */
    private String betDataId;

    /**
     * 提交类型 1:单式投注 2:二串一 3:三串一 ...
     */
    private Integer submitType;

    /**
     * 投注额
     */
    private BigDecimal stake;

    /**
     * 创建时间
     */
    private Date createTime;


}
