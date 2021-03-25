package com.jl.db.aop;

import com.alibaba.fastjson.JSON;

import com.jl.db.utils.IPUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;


@Slf4j
@Aspect
@Component
public class TraceLog {

    @Pointcut("execution(* com.jl.*.controller.*.*(..))")
    public void traceLogAspect() {
    }

    @Before("traceLogAspect()")
    public void doBefore(JoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
        Map<String, String> maps = new LinkedHashMap<>();
        Enumeration enu = request.getParameterNames();
        while (enu.hasMoreElements()) {
            String paraName = (String) enu.nextElement();
            maps.put(paraName, request.getParameter(paraName));
        }
        String classMethodName =
                joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
        maps.put("URL", request.getRequestURL().toString());
        maps.put("HTTP_METHOD", request.getMethod());
        maps.put("IP", IPUtils.getIp(request));
        log.info("\n 执行方法：[{}] \n 请求参数：{}", classMethodName, JSON.toJSONString(maps));
    }

    @AfterReturning(returning = "returnValue", pointcut = "traceLogAspect()")
    public void doAfterReturning(JoinPoint joinPoint, Object returnValue) {
        log.info("\n 执行方法：[{}] \n 返回结果：{}",
                joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName(),
                JSON.toJSONString(returnValue));
    }

    @AfterThrowing(value = "traceLogAspect()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Exception e){
        log.info("\n 执行方法：[{}] \n 异常信息返回：{}",
                joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName(),
                e.getMessage());
    }

}
