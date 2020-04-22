package com.qinyou.apiserver.core.base;

import com.qinyou.apiserver.core.component.SpringContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * 响应信息枚举类
 *
 * @author chuang
 */
@Slf4j
public enum ResultEnum {
    // 成功
    SUCCESS(1000, "操作成功"),
    RESET_PWD_SUCCESS(1000, "重置密码成功, 新密码: "),    // 管理员 重置其它用户

    // 操作失败提示信息
    FAIL(2000, "操作失败"),
    BAD_PARAM(2000, "参数格式错误"),
    UPLOAD_FAIL(2000, "上传失败"),
    PHONE_EXIST(2000, "手机号已存在"),
    EMAIL_EXIST(2000, "邮箱已存在"),
    USERNAME_NOT_FOUND(2000, "用户名 {0} 不存在"),
    EMAIL_NOT_FOUND(2000, "邮箱不存在"),
    PHONE_NOT_FOUND(2000, "手机号不存在"),
    ROLE_NOT_FOUND(2000, "角色 {0} 不存在"),
    CODE_INVALID(2000, "验证码无效"),

    // 账号认证、权限 异常
    ACCOUNT_NOT_EXIST(3000, "账号不存在"),
    ACCOUNT_LOCKED(3001, "账号已锁定"),
    BAD_PASSWORD(3002, "密码错误"),
    NOT_SING_IN(3003, "账号未认证或认证失败"),
    ACCESS_DENIED(3004, "无权访问"),

    // 系统故障异常信息
    EXCEPTION(500, "服务器飘了，管理员去拿刀修理了~");

    private Integer code;       // 编码
    private String defaultMsg;  // 默认消息，当 message_xx.properties 中没有时使用

    ResultEnum(Integer code, String defaultMsg) {
        this.code = code;
        this.defaultMsg = defaultMsg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return getMsg(new Object[]{});
    }

    public String getMsg(Object[] args) {
        return SpringContext.getMessageSource().getMessage(this.toString(), args, defaultMsg, LocaleContextHolder.getLocale());
    }
}


