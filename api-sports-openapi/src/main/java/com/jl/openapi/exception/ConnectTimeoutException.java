package com.jl.openapi.exception;

import lombok.Data;


@Data
public class ConnectTimeoutException extends RuntimeException{

    private String orderNo;

    public ConnectTimeoutException() {
        super("请求超时,请重新再试!");
    }

    public ConnectTimeoutException(String orderNo) {
        super("请求超时,请重新再试!");
        this.orderNo = orderNo;
    }

}
