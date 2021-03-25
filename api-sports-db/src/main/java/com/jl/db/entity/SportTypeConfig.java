package com.jl.db.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 体育类型定义表
 * </p>
 *
 * @author 
 * @since 2021-03-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ty_sport_type_config")
public class SportTypeConfig extends Model<SportTypeConfig> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 业主id
     */
    private Integer agentId;

    /**
     * 体育游戏编号
     */
    private String gameCode;

    /**
     * 体育游戏名称
     */
    private String gameName;

    /**
     * 是否开启 0:开启 1:关闭
     */
    private Integer isOpen;

    /**
     * 排序
     */
    private Integer sort;

    private Date createTime;

    private String createBy;

    private Date updateTime;

    private String updateBy;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
