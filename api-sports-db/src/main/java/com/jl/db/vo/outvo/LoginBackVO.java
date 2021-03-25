package com.jl.db.vo.outvo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "用户登录返回参数封装VO")
public class LoginBackVO {

	@ApiModelProperty(value = "跳转url")
	private String url;
}
