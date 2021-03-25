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
 * 用户信息表
 * </p>
 *
 * @author 
 * @since 2021-03-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ty_account_info")
public class TyAccountInfo extends Model<TyAccountInfo> {

    private static final long serialVersionUID = 1L;

    /**
     * 用户标识
     */
    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userID;

    /**
     * 用户账号
     */
    @TableField("account")
    private String accounts;

    /**
     * 昵称
     */
    @TableField("nick_name")
    private String nickName;

    /**
     * 登录密码
     */
    @TableField("logon_pass")
    private String logonPass;

    /**
     * 用户平台余额
     */
    @TableField("score")
    private BigDecimal score;

    /**
     * 头像标识
     */
    @TableField("face_id")
    private Integer faceId;

    /**
     * 赠送礼物
     */
    @TableField("present")
    private Integer present;

    /**
     * 经验数值
     */
    @TableField("experience")
    private Integer experience;


    /**
     * 成长等级
     */
    @TableField("grow_level")
    private Integer growLevel;

    /**
     * 用户魅力
     */
    @TableField("love_liness")
    private Integer loveLiness;


    /**
     * 会员等级
     */
    @TableField("member_rank")
    private Integer memberRank;


    /**
     * 用户性别
     */
    @TableField("gender")
    private Integer gender;

    /**
     * 用户状态：0-有效，1-禁用
     */
    @TableField("nullity")
    private Integer nullity;


    /**
     * 生日
     */
    @TableField("birthday")
    private Date birthDay;

    /**
     * 业主ID，对应T_Acc_Agent表的AgentID
     */
    @TableField("owner_id")
    private Integer ownerID;


    /**
     * 站点标识
     */
    @TableField("site_code")
    private String siteCode;

    @Override
    protected Serializable pkVal() {
        return this.userID;
    }

}
