package com.qinyou.apiserver.core.component;

import com.qinyou.apiserver.core.base.Result;
import com.qinyou.apiserver.core.base.ResultEnum;
import com.qinyou.apiserver.core.utils.WebUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定403返回值，认证过的用户访问无权限资源时的异常
 * 经测试无效，会直接抛出 AccessDeniedException
 *
 * @author chuang
 */
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) {
        response.setHeader("Access-Control-Allow-Origin", "*");

        // 403
        // http 正常响应， 响应内容中有相关状态码 和提示信息
        response.setStatus(200);
        Result result = WebUtils.fail(ResultEnum.ACCESS_DENIED);
        WebUtils.outPrintJSON(response, result);
    }

}
