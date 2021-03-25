package com.jl.db.vo.outvo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountInfoVO {
    private String account;
    private BigDecimal money;
    private String clientIp;
}
