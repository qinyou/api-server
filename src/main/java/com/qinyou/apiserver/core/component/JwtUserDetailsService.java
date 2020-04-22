package com.qinyou.apiserver.core.component;

import com.qinyou.apiserver.core.base.JwtUser;
import com.qinyou.apiserver.sys.dto.UserInfo;
import com.qinyou.apiserver.sys.service.IAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户验证
 *
 * @author chuang
 */
@Service
@Slf4j
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private IAccountService accountService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //log.debug("-- 获取用户信息&权限 --");
        UserInfo userDTO = accountService.getUserInfo(username);
        Set<String> authorities = new HashSet<>();
        authorities.addAll(userDTO.getRoles());
        authorities.addAll(userDTO.getResources());
        return new JwtUser(
                userDTO.getUsername(),
                userDTO.getPassword(),
                authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()),
                userDTO.getState()
        );
    }

}
