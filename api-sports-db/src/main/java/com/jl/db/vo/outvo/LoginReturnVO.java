package com.jl.db.vo.outvo;


import com.jl.db.vo.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel(description = "登录返回参数封装VO")
public class LoginReturnVO extends BaseVO {

    @ApiModelProperty(value = "用户ID",name = "userId")
    private Integer userId;

    @ApiModelProperty(value = "游戏ID",name = "gameId")
    private Integer gameId;

    @ApiModelProperty(value = "用户名",name = "accounts")
    private String accounts;

    @ApiModelProperty(value = "昵称",name = "nickName")
    private String nickName;

    @ApiModelProperty(value = "个性签名",name = "underWrite")
    private String underWrite;

    @ApiModelProperty(value = "头像标识",name = "faceId")
    private Integer faceId;

    @ApiModelProperty(value = "自定标识",name = "customId")
    private Integer customId;

    @ApiModelProperty(value = "用户性别",name = "gender")
    private Integer gender;

    @ApiModelProperty(value = "用户奖牌",name = "ingot")
    private Integer ingot;//UserMedal

    @ApiModelProperty(value = "经验数值",name = "experience")
    private Integer experience;

    @ApiModelProperty(value = "用户魅力",name = "loveLiness")
    private Integer loveLiness;

    @ApiModelProperty(value = "会员等级",name = "memberOrder")
    private Integer memberOrder;

    @ApiModelProperty(value = "过期日期" , name = "memberOverDate")
    private Date memberOverDate;

    @ApiModelProperty(value = "固定机器" , name = "moorMachine")
    private Integer moorMachine;

    @ApiModelProperty(value = "登录类型" , name = "logonMode")
    private  Integer  logonMode;//PlatformID

    @ApiModelProperty(value = "代理Id",name = "isAgent")
    private Integer isAgent;//ParentID

    @ApiModelProperty(value = "注册时间",name = "RegisterDate")
    private Date registerDate;

    @ApiModelProperty(value = "注册手机",name = "registerMobile")
    private String registerMobile;

    private Integer freeNum;

    @ApiModelProperty(value = "充值增益",name = "buff")
    private Float  buff;

    @ApiModelProperty(value = "动态密码",name = "dynamicPass")
    private String dynamicPass;

    @ApiModelProperty(value = "黑名单:0正常，1白名单，2黑名单",name = "BlackStatus")
    private Integer BlackStatus;

    @ApiModelProperty(value = "vip等级",name = "vipLevel")
    private Long vipLevel;

    @ApiModelProperty(value = "地址描述",name = "lastLogonIp")
    private String  lastLogonIp; // ifnull(@SetIPAddress,'')

    @ApiModelProperty(value = "类型标识",name = "kindId")
    private Integer kindId;// 默认0

    @ApiModelProperty(value = "锁定服务Id",name = "lockServerId")
    private Integer lockServerId;

    @ApiModelProperty(value = "用户积分（货币）",name = "score")
    private BigDecimal score;

    @ApiModelProperty(value = "银行金币",name = "insure")
    private BigDecimal insure;

    @ApiModelProperty(value = "游戏豆",name = "beans")
    private BigDecimal beans;

    @ApiModelProperty(value = "房卡",name = "roomCard")
    private Long roomCard;

    @ApiModelProperty(value = "红包",name = "redEnvelope")
    private BigDecimal redEnvelope;

    @ApiModelProperty(value = "银行标识",name = "insureEnabled")
    private Integer insureEnabled;

    @ApiModelProperty(value = "是否首次登陆",name = "isFirst")
    private Boolean isFirst;
}
