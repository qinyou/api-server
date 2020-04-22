package com.qinyou.apiserver.core.component;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Token过滤器，无状态拦截器实现
 *
 * @author chuang
 */
@Component
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    @Qualifier("jwtUserDetailsService")
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        // filter 方法 内部执行方法，此处操作不会 向顶层抛出异常
        // 如何重写 filter 才能向 底层抛出异常
        String authHeader = request.getHeader("Authorization");
        String tokenHead = "Bearer ";
        if (authHeader != null && authHeader.startsWith(tokenHead)) {
            // log.debug("check Authorization ");
            String authToken = authHeader.substring(tokenHead.length());

            // token 有效，且 当前上下文没有认证信息(排除登录）
            if (jwtService.verify(authToken) && SecurityContextHolder.getContext().getAuthentication() == null) {
                String username = jwtService.getUsername(authToken);

                // 加载用户 权限信息 用于权限认证，不再 认证用户名密码
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            // token 无效
            // token 过期 等
        }
        chain.doFilter(request, response);
    }

}

