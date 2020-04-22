package com.qinyou.apiserver.sys.service;

import com.qinyou.apiserver.sys.dto.UserInfoForm;
import com.qinyou.apiserver.sys.dto.ChangePwdForm;
import com.qinyou.apiserver.sys.dto.ResetPwdForm;
import com.qinyou.apiserver.sys.dto.UserInfo;


public interface IAccountService {


    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 操作结果
     */
    String login(String username, String password);


    /**
     * 通过用户名 获取用户信息
     *
     * @param username
     * @return
     */
    UserInfo getUserInfo(String username);


    /**
     * 修改用户个人信息
     *
     * @param username
     * @param userInfoForm
     */
    void updateUserInfo(String username, UserInfoForm userInfoForm);


    /**
     * 通过邮件发送验证码（同步发送）
     *
     * @param email
     */
    void sendMailCode(String email);


    /**
     * 通过手机号发送验证码
     *
     * @param phone
     */
    void sendPhoneCode(String phone);


    /**
     * 通过验证码重置账号验证码
     *
     * @param resetPwdForm
     */
    void resetPwd(ResetPwdForm resetPwdForm);

    /**
     * 用户修改密码
     *
     * @param username
     * @param changePwdForm
     */
    void changePwd(String username, ChangePwdForm changePwdForm);
}
