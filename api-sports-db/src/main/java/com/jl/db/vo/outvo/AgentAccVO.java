package com.jl.db.vo.outvo;

import com.jl.db.vo.BaseVO;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AgentAccVO extends BaseVO {
    private Integer agentId ;
    private String agentAcc = "";    //代理账号
    private String pwd = "";         //代理密码
    private Integer parentId = 0;
    private String parentAcc = "";
    private String agentLevel ="";
    private String safePwd ="";
    private Integer agentStatus = 0;
    private BigDecimal score= BigDecimal.ZERO;
    private BigDecimal agentRate = BigDecimal.ZERO;     //代理抽水
    private String qq = "";
    private String realName="";    //真实姓名
    private String agentDomain="";  //代理域名
    private String memo = "";          //备注
    private String bankAcc = "";
    private String bankName = "";
    private String bankAddress="";
    private String regIp = "";
    private String regDate = "";
    private String lastIp = "";
    private String lastDate = "";
    private Integer nagentNum=0;
    private Integer playerCount=0;
    private String weChat = "";           //微信
    private Integer isClient = 0;      //代理类别
    private String showName ="";   //显示名称
    private Integer showSort = 0;    //排序
    private Integer queryRight = 0;    //操作权限
    private String desKey = "";
    private String md5Key = "";
    private String clientIp; //操作IP
    private Integer operator; // 操作ID
    private String phoneName;
    private String phonePwd;
    private Integer qmMode;
    private Integer sendMode=0;
    private String agentUrl="";
    private Integer agentVersion;
    private String clientUrl;
    private String updateAddress;
    private String prompt="";   //维护提示
    private String PreUpdateAddress; //熱更地址
    private Integer status = 0;
    private String primaryDomain;
    private String hotVersion;
    private String loadingUrl;//加载页地址
    private String sportLoadingUrl;// 体育页加载地址

    /**
     * 域名字体大小  22:22号字体；26:26号字体；30:30号字体
     */
    private Integer domainFont;
    private Integer needSafeLink; // 是否需要安全推广域名 0：不需要 1：需要'
    private Integer isRiskOpen;// 风控开关
    private Integer isOpenJifen; //是否开启积分兑换(0-否，1-是)
    private String peUpdateAddress;
    private String peVersion;
    private String betterFast;// '加速域名'
}
