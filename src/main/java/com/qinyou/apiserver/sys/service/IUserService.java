package com.qinyou.apiserver.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qinyou.apiserver.sys.entity.User;

/**
 * <p>
 * 系统用户表 服务类
 * </p>
 *
 * @author chuang
 * @since 2019-10-19
 */
public interface IUserService extends IService<User> {
    void add(User user);

    void update(User user);

    void remove(String id);

    void toggleState(String id);

    void resetPwd(String id);

    /**
     * 手机号或邮箱否存在, 可排除自身
     *
     * @param username 可为null
     * @param phone
     * @param email
     * @return
     */
    boolean checkExist(String username, String phone, String email);
}
