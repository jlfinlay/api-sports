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
 * 业主信息表
 * </p>
 *
 * @author 
 * @since 2021-03-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ty_owner_info")
public class TyOwnerInfo extends Model<TyOwnerInfo> {

    private static final long serialVersionUID = 1L;

    /**
     * 业主ID--主键
     */
    @TableId(value = "owner_id", type = IdType.AUTO)
    private Integer ownerId;

    /**
     * 业主账号
     */
    @TableField("owner_account")
    private String ownerAccount;

    /**
     * 登录密码
     */
    @TableField("password")
    private String password;

    /**
     * 上级代理
     */
    @TableField("parent_id")
    private Integer parentId;

    /**
     * 上级代理账号
     */
    @TableField("parent_account")
    private String parentAccount;

    /**
     * 代理级别
     */
    @TableField("agent_level")
    private Integer agentLevel;


    /**
     * 代理状态
     */
    @TableField("agent_status")
    private Integer agentStatus;

    /**
     * 代理金币
     */
    @TableField("score")
    private BigDecimal score;

    /**
     * 抽水比例
     */
    @TableField("agent_rate")
    private BigDecimal agentRate;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;


    @TableField("des_key")
    private String desKey;

    @TableField("md5_key")
    private String md5Key;


    @Override
    protected Serializable pkVal() {
        return this.ownerId;
    }

}
