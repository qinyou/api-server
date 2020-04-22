package com.qinyou.apiserver.sys.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qinyou.apiserver.core.base.RequestException;
import com.qinyou.apiserver.core.base.ResultEnum;
import com.qinyou.apiserver.core.utils.WebUtils;
import com.qinyou.apiserver.sys.entity.User;
import com.qinyou.apiserver.sys.entity.UserRole;
import com.qinyou.apiserver.sys.mapper.UserMapper;
import com.qinyou.apiserver.sys.service.IUserRoleService;
import com.qinyou.apiserver.sys.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * <p>
 * 系统用户表 服务实现类
 * </p>
 *
 * @author chuang
 * @since 2019-10-19
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Value("${app.user-default-password}")
    String userDefaultPwd;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    IUserRoleService userRoleService;

    @Override
    public void add(User user) {
        if (StrUtil.isNotBlank(user.getPhone()) && checkExist(null, user.getPhone(), null)) {
            throw RequestException.fail(ResultEnum.PHONE_EXIST);
        }
        if (StrUtil.isNotBlank(user.getEmail()) && checkExist(null, null, user.getEmail())) {
            throw RequestException.fail(ResultEnum.EMAIL_EXIST);
        }
        user.setPassword(passwordEncoder.encode(userDefaultPwd))
                .setCreater(WebUtils.getSecurityUsername())
                .setCreateTime(LocalDateTime.now());
        this.save(user);
    }

    @Override
    public void update(User user) {
        // 验证手机号邮箱是否存在
        if (StrUtil.isNotBlank(user.getPhone()) && checkExist(user.getId(), user.getPhone(), null)) {
            throw RequestException.fail(ResultEnum.PHONE_EXIST);
        }
        if (StrUtil.isNotBlank(user.getEmail()) && checkExist(user.getId(), null, user.getEmail())) {
            throw RequestException.fail(ResultEnum.EMAIL_EXIST);
        }
        user.setUpdateTime(LocalDateTime.now())
                .setUpdater(WebUtils.getSecurityUsername());
        this.updateById(user);
    }


    @Transactional
    @Override
    public void remove(String id) {
        User user = Optional.of(this.getById(id)).get();
        this.removeById(id);
        userRoleService.remove(new QueryWrapper<UserRole>().eq("user_id", id));
    }

    @Override
    public void toggleState(String id) {
        User user = Optional.of(this.getById(id)).get();
        String state = "0".equals(user.getState()) ? "1" : "0";
        this.update(
                new UpdateWrapper<User>().set("state", state)
                        .set("updater", WebUtils.getSecurityUsername())
                        .set("update_time", LocalDateTime.now())
                        .eq("id", id)
        );
    }

    /**
     * 重置密码
     *
     * @param id
     */
    @Override
    public void resetPwd(String id) {
        User user = Optional.of(this.getById(id)).get();
        user.setPassword(passwordEncoder.encode(userDefaultPwd))
                .setUpdater(WebUtils.getSecurityUsername())
                .setUpdateTime(LocalDateTime.now());
        this.updateById(user);
    }


    /**
     * 手机号或邮箱否存在
     * 参数 username 不为 null时, 排除此用户名判断
     *
     * @param username
     * @param phone
     * @param email
     * @return
     */
    @Override
    public boolean checkExist(String username, String phone, String email) {
        if ((phone == null && email == null) || (phone != null && email != null)) {
            log.debug("email: {}, phone: {}", email, phone);
            throw new IllegalArgumentException("参数 phone 、email 必须 一个 为 null, 一个 不为 null");
        }
        QueryWrapper<User> query = new QueryWrapper<>();
        query.ne(username != null, "username", username)
                .eq(phone != null, "phone", phone)
                .eq(email != null, "email", email);
        int count = this.count(query);
        return count != 0;
    }
}
