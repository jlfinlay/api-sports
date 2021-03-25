package com.jl.db.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author 
 * @since 2021-03-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ty_access_order")
public class TyAccessOrder extends Model<TyAccessOrder> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    @TableField("owner_id")
    private Integer ownerId;

    @TableField("account")
    private String account;

    @TableField("order_id")
    private String orderId;

    @TableField("order_state")
    private Integer orderState;

    @TableField("insert_date")
    private Date insertDate;

    @TableField("operation")
    private Integer operation;

    @TableField("operation_score")
    private BigDecimal operationScore;

    @TableField("reason_code")
    private Integer reasonCode;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
