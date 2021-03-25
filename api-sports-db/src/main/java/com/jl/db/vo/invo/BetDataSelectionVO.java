package com.jl.db.vo.invo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 本地投注选项表
 * </p>
 *
 * @author 
 * @since 2020-09-23
 */
@Data
public class BetDataSelectionVO implements Serializable {

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
     * 投注选项ID
     */
    private String selectionId;

    /**
     * 选项的盘口值
     */
    private String selectionValue;

    /**
     * 赔率
     */
    private String decimalPrice;

    /**
     * 比分
     */
    private String score;

    /**
     * 创建时间
     */
    private Date createTime;


}
