package com.jl.db.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 第三方账户信息表
 * </p>
 *
 * @author 
 * @since 2021-03-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class YsbAccountInfo extends Model<YsbAccountInfo> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    /**
     * 业主ID
     */
    private Integer agentId;

    /**
     * 用户标识
     */
    private Integer userId;

    /**
     * 游戏编码
     */
    private String gameCode;

    /**
     * 第三方账户id
     */
    private String accountId;

    /**
     * 第三方注册时间
     */
    private Date registTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
