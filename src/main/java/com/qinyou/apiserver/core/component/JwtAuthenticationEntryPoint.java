package com.qinyou.apiserver.core.component;

import com.qinyou.apiserver.core.base.Result;
import com.qinyou.apiserver.core.base.ResultEnum;
import com.qinyou.apiserver.core.utils.WebUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定401返回值, 匿名用户(或认证失败）访问无权限资源时的异常
 *
 * @author chuang
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        // 401
        // http 正常响应， 响应内容中有相关状态码 和提示信息
        response.setStatus(200);
        Result result = WebUtils.fail(ResultEnum.NOT_SING_IN);
        WebUtils.outPrintJSON(response, result);
    }
}
