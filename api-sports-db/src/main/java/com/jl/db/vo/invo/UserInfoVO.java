package com.jl.db.vo.invo;

import com.jl.db.vo.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel(description = "修改用户信息入参封装VO")
public class UserInfoVO extends BaseVO {

    @ApiModelProperty(value = "用户ID",name = "userId",required = true)
    private Integer userId;

    @ApiModelProperty(value = "头像标识",name = "faceId")
    private Integer faceId;

    @ApiModelProperty(value = "用户性别",name = "gender")
    private Integer gender;

    @ApiModelProperty(value = "昵称",name = "nickName" )
    private String nickName;

    @ApiModelProperty(value = "邮箱",name = "email" )
    private String email;

    @ApiModelProperty(value = "手机号",name = "mobileNo" )
    private String mobileNo;

    @ApiModelProperty(value = "验证码",name = "code" )
    private String code;

    @ApiModelProperty(value = "QQ号",name = "qq" )
    private String qq;

    @ApiModelProperty(value = "原密码",name = "oldPassword")
    private String oldPassword;

    @ApiModelProperty(value = "新密码",name = "newPassword")
    private String newPassword;

    @ApiModelProperty(value = "确认密码",name = "rePassword")
    private String rePassword;

    @ApiModelProperty(value = "修改类型",name = "type, 0:头像(头像,性别),1:基本信息(昵称),2:邮箱,3:电话,4:QQ,5:密码)",  allowableValues = "0,1,2,3,4,5",required = true)
    private Integer type;

    @ApiModelProperty(value = "微信号",name = "WeChat")
    private String WeChat;

    @ApiModelProperty(value = "真实姓名",name = "realName")
    private String realName;

    @ApiModelProperty(value = "生日",name = "birthday")
    private String birthday;





}
