
package com.jl.openapi.exception;

/**
 *  * @ClassName HttpClientException
 *  * @Description 自定义转账SocketTimeOut异常
 *  
 **/
public class HttpClientException extends RuntimeException {

    public HttpClientException(String message) {
        super(message);
    }

    public HttpClientException(String message, Throwable cause) {
        super(message, cause);
    }

}
