package com.jl.db.vo.outvo;

import com.jl.db.vo.BaseVO;
import lombok.Data;

import java.math.BigDecimal;


@Data
public class BindMobileSendVO extends BaseVO {

    private BigDecimal bindMobileSend ;// StatusValue 状态数值
    private Integer isShow;            // IsShow 是否显示   0-显示，1-不显示
}
