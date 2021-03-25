package com.jl.db.vo.outvo;

import com.jl.db.vo.BaseVO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;


@Data
public class AccountsInfoVO extends BaseVO {

    /**
     * 用户标识
     */
    private Integer UserID;

    /**
     * 游戏标识
     */
    private Integer GameID;

    /**
     * 密保标识
     */
    private Integer ProtectID;

    /**
     * 口令索引
     */
    private Integer PasswordID;

    /**
     * 推广员标识
     */
    private Integer SpreaderID;

    /**
     * 用户帐号
     */
    private String Accounts;

    /**
     * 用户昵称
     */
    private String NickName;

    /**
     * 注册帐号
     */
    private String RegAccounts;

    /**
     * 个性签名
     */
    private String UnderWrite;

    /**
     * 身份证号
     */
    private String PassPortID;

    /**
     * 真实名字
     */
    private String Compellation;

    /**
     * 登录密码
     */
    private String LogonPass;

    /**
     * 安全密码
     */
    private String InsurePass;

    /**
     * 动态密码
     */
    private String DynamicPass;

    /**
     * 动态密码更新时间
     */
    private Date DynamicPassTime;

    /**
     * 头像标识
     */
    private Integer FaceID;

    /**
     * 自定标识
     */
    private Integer CustomID;

    /**
     * 赠送礼物
     */
    private Integer Present;

    /**
     * 用户奖牌
     */
    private Integer UserMedal;

    /**
     * 经验数值
     */
    private Integer Experience;

    private Integer GrowLevelID;

    /**
     * 用户魅力
     */

    private Integer LoveLiness;

    /**
     * 用户权限
     */
    private Integer UserRight;

    /**
     * 管理权限
     */
    private Integer MasterRight;

    /**
     * 服务权限
     */
    private Integer ServiceRight;

    /**
     * 管理等级
     */
    private Integer MasterOrder;

    /**
     * 会员等级
     */
    private Integer MemberOrder;

    /**
     * 过期日期
     */
    private Date MemberOverDate;

    /**
     * 切换时间
     */
    private Date MemberSwitchDate;

    /**
     * 头像版本
     */
    private Integer CustomFaceVer;

    /**
     * 用户性别
     */
    private Integer Gender;

    /**
     * 禁止服务
     */
    private Integer Nullity;

    /**
     * 禁止时间
     */
    private Date NullityOverDate;

    /**
     * 关闭标志
     */

    private Integer StunDown;

    /**
     * 固定机器
     */

    private Integer MoorMachine;

    /**
     * 是否机器人
     */
    private Integer IsAndroid;

    /**
     * 登录次数
     */
    private Integer WebLogonTimes;

    /**
     * 登录次数
     */
    private Integer GameLogonTimes;

    /**
     * 游戏时间
     */
    private Integer PlayTimeCount;

    /**
     * 在线时间
     */
    private Integer OnLineTimeCount;

    /**
     * 登录地址
     */
    private String LastLogonIP;

    /**
     * 登录时间
     */
    private Date LastLogonDate;

    /**
     * 登录手机
     */
    private String LastLogonMobile;

    /**
     * 登录机器
     */
    private String LastLogonMachine;

    /**
     * 注册地址
     */
    private String RegisterIP;

    /**
     * 注册时间
     */
    private Date RegisterDate;

    /**
     * 注册手机
     */
    private String RegisterMobile;

    /**
     * 注册机器
     */
    private String RegisterMachine;

    /**
     * PC       0x00     ,
ANDROID  0x10(cocos 0x11,u3d 0x12)     ,
ITOUCH   0x20     ,
IPHONE   0x30(cocos 0x31,u3d 0x32)     ,
IPAD     0x40(cocos 0x41,u3d 0x42)     ,
WEB      0x50     
     */
    private Integer RegisterOrigin;

    /**
     * =2 表示是游客
     */
    private Integer PlatformID;

    private String UserUin;

    private Integer RankID;

    private Integer AgentID;

    /**
     * T_Acc_Agent.AgentID 677这套代理
     */
    private Integer ParentID;

    /**
     * 变色用
     */
    private Integer UserType;

    /**
     * 试玩任务ID
     */
    private String Advertiser;

    private String LastLogonIPAddress;

    /**
     * 试玩平台名
     */
    private String AdvertPlat;

    private Integer QmSpreaderID;

    private BigDecimal ZzQmRatio;

    private Integer ZzIsAgent;

    private Integer H5AgentId;

    private String H5SiteCode;

    private String H5Account;

    private Integer Device;

    private Date FirstRechargeDate;

    private Integer TotalAgentID;

    private String PhoneType;

    private String LoginArea;

    private String ChannelAccount;

    private String ChannelPassword;

    /**
     * 是否为默认代理
     */
    private Integer IsDefaultAgent;

    /**
     * QQ号
     */
    private String Qq;

    /**
     * Email地址
     */
    private String Email;

    /**
     * 备注
     */
    private String Remark;

    /**
     * 负盈利方案ID
     */
    private String ProgramID;

    /**
     * 负盈利方案名称
     */
    private String ProgramName;

}
