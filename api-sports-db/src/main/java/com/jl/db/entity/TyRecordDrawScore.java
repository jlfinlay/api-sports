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
 * 游戏记录从表
 * </p>
 *
 * @author 
 * @since 2021-03-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ty_record_draw_score")
public class TyRecordDrawScore extends Model<TyRecordDrawScore> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户标识
     */
    @TableField("user_id")
    private Integer UserId;

    /**
     * 我们平台保存的账号，代理编号+站点编号+登录账号
     */
    @TableField("account")
    private String Account;

    /**
     * 第三方账号
     */
    @TableField("original_account")
    private String OriginalAccount;

    /**
     * 第三方站点编号
     */
    @TableField("site_code")
    private String SiteCode;

    /**
     * 代理编号，等于业主ID
     */
    @TableField("owner_id")
    private Integer OwnerId;

    /**
     * 操作前账户余额
     */
    @TableField("before_score")
    private BigDecimal BeforeScore;

    /**
     * 操作后户余额
     */
    @TableField("after_score")
    private BigDecimal AfterScore;

    /**
     * 本次操作金额，下分和游戏输都是负数值
     */
    @TableField("operation_score")
    private BigDecimal OperationScore;

    /**
     * 录入时间
     */
    @TableField("inserte_date")
    private Date InsertDate;

    /**
     * 操作代码，参考API接口里的OP值，这里0表示玩游戏
     */
    @TableField("operation_code")
    private Integer OperationCode;

    /**
     * 操作描述
     */
    @TableField("operation_describe")
    private String OperationDescribe;




    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
