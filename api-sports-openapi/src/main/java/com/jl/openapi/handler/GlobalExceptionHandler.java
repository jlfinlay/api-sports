package com.jl.openapi.handler;

import com.alibaba.fastjson.JSONObject;
import com.jl.db.common.Response;
import com.jl.db.exception.ServiceException;
import com.jl.openapi.exception.GameCallBackException;
import com.jl.openapi.exception.ThirdGameException;
import com.jl.openapi.exception.TokenExpireException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@RestControllerAdvice
@Order
public class GlobalExceptionHandler {

    // 拦截程序错误信息，返回json给前端
    @ExceptionHandler(value = Exception.class)
    public Response defaultErrorHandler(Exception exception) {
        String errorPosition = "";
        //如果错误堆栈信息存在
        if (exception.getStackTrace().length > 0) {
            StackTraceElement element = exception.getStackTrace()[0];
            String fileName = element.getFileName() == null ? "未找到错误文件" : element.getFileName();
            int lineNumber = element.getLineNumber();
            errorPosition = fileName + ":" + lineNumber;
        }
        Response response = new Response();
        response.setCode(Response.ERROR);
        response.setMsg( "当前网速不稳定，请稍后再试");
        JSONObject errorObject = new JSONObject();
        errorObject.put("errorLocation", exception.toString() + "    错误位置:" + errorPosition);
        response.setData(errorObject);
        log.info("异常", exception);
        return response;
    }


    //validate框架效验异常
    @ExceptionHandler(BindException.class)
    public Response bindExceptionHandler(BindException e){
        log.info("validate框架效验异常BindException:{}", e.getMessage());
        return Response.fail(e.getBindingResult().getFieldError().getDefaultMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Response illegalArgumentExceptionHandler(IllegalArgumentException e){
        log.error("illegalArgumentExceptionHandler:{}", e.getMessage());
        return Response.fail(e.getMessage());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Response missingServletRequestParameterExceptionHandler(MissingServletRequestParameterException e){
        log.error("missingServletRequestParameterExceptionHandler:{}", e.getMessage());
        return Response.fail(e.getMessage());
    }

    @ExceptionHandler(ServiceException.class)
    public Response serviceException(ServiceException e){
        log.info("serviceExceptionHandler:{}", e.getMessage());
        return Response.fail(e.getMessage());
    }

    @ExceptionHandler(TokenExpireException.class)
    public Response tokenExpireException(TokenExpireException e) {
        log.info("tokenExpireExceptionHandler:{}", e.getMessage());
        return Response.tokenExpire(e.getMessage());
    }

    @ExceptionHandler(ThirdGameException.class)
    public Response thirdGameException(ThirdGameException e) {
        log.info("thirdGameExceptionHandler:{}", e.getMessage());
        return Response.fail(e.getMessage());
    }

    // 回调异常特殊处理
    @ExceptionHandler(GameCallBackException.class)
    public String gameCallBackException(GameCallBackException e) {
        log.info("gameCallBackExceptionHandler:{}", e.getMessage());
        JSONObject json = new JSONObject();
        Map<String,String> map = new HashMap<>();
        map.put("S",Response.FAIL);
        map.put("ED",e.getMessage());
        json.put("response",map);
        return json.toJSONString();
    }

}
