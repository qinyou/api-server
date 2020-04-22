package com.qinyou.apiserver.core.component;

import com.qinyou.apiserver.core.base.JwtClaim;
import com.qinyou.apiserver.core.base.RequestException;
import com.qinyou.apiserver.core.base.ResultEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;

/**
 * 将 Authorization 中数据 注入 controller 方法参数中
 */
@Component
public class JwtArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    JwtService jwtService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(JwtClaim.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        String authHeader = request.getHeader("Authorization");
        String tokenHead = "Bearer ";
        Object result = null;
        JwtClaim token = null;
        if (authHeader != null && authHeader.startsWith(tokenHead)) {
            authHeader = authHeader.substring(tokenHead.length());
            Annotation[] methodAnnotations = parameter.getParameterAnnotations();
            for (Annotation methodAnnotation : methodAnnotations) {
                if (methodAnnotation instanceof JwtClaim) {
                    token = (JwtClaim) methodAnnotation;
                    break;
                }
            }
            if (token != null) {
                result = jwtService.get(authHeader, token.value());
            }
        }

        // 无法解析
        if (result == null) {
            throw RequestException.fail(ResultEnum.NOT_SING_IN);
        }

        return result;
    }
}
