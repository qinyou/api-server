package com.qinyou.apiserver.core.component;

import com.qinyou.apiserver.core.base.SysLog;
import com.qinyou.apiserver.core.utils.WebUtils;
import com.qinyou.apiserver.sys.entity.Log;
import com.qinyou.apiserver.sys.service.ILogService;
import io.swagger.annotations.ApiOperation;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * 系统日志切面
 *
 * @author chuang
 */
@Aspect
@Component
public class SysLogAspect {
    private final ILogService logService;

    @Autowired
    public SysLogAspect(ILogService logService) {
        this.logService = logService;
    }

    @Pointcut("@annotation(com.qinyou.apiserver.core.base.SysLog)")
    public void log() {
    }

    @AfterReturning(value = "log()", returning = "returnValue")
    public void after(JoinPoint joinPoint, Object returnValue) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        Log log = new Log();
        log.setIp(WebUtils.getClientIp(request));
        log.setHttpMethod(request.getMethod());
        log.setClassMethod(joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName() + "()");
        log.setUsername(WebUtils.getSecurityUsername());
        log.setCreateTime(LocalDateTime.now());
        log.setUri(request.getRequestURI());

        SysLog sysLog = getMethodSysLogsAnnotationValue(joinPoint);
        // @SysLog value 为空，从@ApiOperation 的 value 取值
        log.setActionName("".equals(sysLog.value()) ? getApiOperationValue(joinPoint) : sysLog.value());

        if (!"".equals(sysLog.type())) {
            log.setType(sysLog.type());
        }
        if (sysLog.logParams()) {
            String paramJson = WebUtils.toJSONString(joinPoint.getArgs());
            log.setParams(paramJson.length() > 500 ? "数据过大，不给予记录" : paramJson);
        }
        if (sysLog.logResult() && returnValue != null) {
            String resultJson = WebUtils.toJSONString(returnValue);
            log.setResult(resultJson.length() > 500 ? "数据过大，不给予记录" : resultJson);
        }
        logService.save(log);
    }


    /**
     * 获得  SysLog 注解
     *
     * @param joinPoint
     * @return
     */
    private SysLog getMethodSysLogsAnnotationValue(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        return method.getAnnotation(SysLog.class);
    }


    /**
     * 获得 ApiOperation 注解内容
     *
     * @param joinPoint
     * @return
     */
    private String getApiOperationValue(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);
        if (apiOperation != null) {
            return apiOperation.value();
        }
        return "";
    }
}
