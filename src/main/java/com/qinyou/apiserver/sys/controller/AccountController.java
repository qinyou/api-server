package com.qinyou.apiserver.sys.controller;

import cn.hutool.core.lang.Validator;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qinyou.apiserver.core.base.*;
import com.qinyou.apiserver.core.utils.WebUtils;
import com.qinyou.apiserver.sys.dto.*;
import com.qinyou.apiserver.sys.entity.Log;
import com.qinyou.apiserver.sys.service.IAccountService;
import com.qinyou.apiserver.sys.service.ILogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Api(tags = "账号")
@Slf4j
@RestController
@RequestMapping("/account")
public class AccountController {
    private final IAccountService accountService;
    private final ILogService logService;
    // token 持续时间，单位小时, 用于前台本地保存时间
    @Value("${app.jwt.expire-idle}")
    Integer expireIdle;

    @Autowired
    public AccountController(IAccountService accountService, ILogService logService) {
        this.accountService = accountService;
        this.logService = logService;
    }

    @ApiOperation("用户登录")
    @SysLog(type = "0")
    @PostMapping(value = "/login")
    public Result<Map<String, Object>> getToken(@RequestBody @Validated LoginForm loginForm) {
        String token = accountService.login(loginForm.getUsername(), loginForm.getPassword());
        Map<String, Object> ret = new HashMap<>();
        ret.put("token", "Bearer " + token);
        ret.put("expireIdle", expireIdle);
        return WebUtils.ok(ResultEnum.SUCCESS, ret);
    }

    @ApiOperation("获得用户信息、用户角色权限")
    @ApiImplicitParam(name = "Authorization", required = true, paramType = "header", value = "身份认证Token")
    @SysLog()
    @GetMapping("/user-info")
    public Result<UserInfo> getUserInfo(@JwtClaim String username) {
        UserInfo userInfo = accountService.getUserInfo(username);
        return WebUtils.ok(userInfo);
    }

    @ApiOperation("修改用户信息")
    @ApiImplicitParam(name = "Authorization", required = true, paramType = "header", value = "身份认证Token")
    @SysLog(type = "0")
    @PostMapping("/update-user-info")
    public Result updateUserInfo(@JwtClaim String username, @RequestBody @Validated UserInfoForm userInfoForm) {
        accountService.updateUserInfo(username, userInfoForm);
        return WebUtils.ok(ResultEnum.SUCCESS);
    }

    @ApiOperation("通过邮箱发送验证码, 用于重置账号密码")
    @SysLog()
    @GetMapping(value = "/send-email-code/{email}")
    public Result sendVerificationCodeByEmail(@PathVariable String email) {
        if (!Validator.isEmail(email)) {
            throw RequestException.fail(ResultEnum.BAD_PARAM);
        }
        accountService.sendMailCode(email);
        return WebUtils.ok(ResultEnum.SUCCESS);
    }

    @ApiOperation("通过邮箱发送验证码, 用于重置账号密码")
    @SysLog()
    @GetMapping(value = "/send-phone-code/{phone}")
    public Result sendVerificationCodeByPhone(@PathVariable String phone) {
        if (!Validator.isMobile(phone) && !"15238002477".equals(phone)) {
            throw RequestException.fail(ResultEnum.BAD_PARAM);
        }
        accountService.sendPhoneCode(phone);
        return WebUtils.ok(ResultEnum.SUCCESS);
    }


    @ApiOperation("通过验证码重置账号密码")
    @SysLog(type = "0")
    @PostMapping(value = "/reset-password")
    public Result resetPwd(@RequestBody @Validated ResetPwdForm resetPwdForm) {
        accountService.resetPwd(resetPwdForm);
        return WebUtils.ok(ResultEnum.SUCCESS);
    }


    @ApiOperation("用户修改密码")
    @ApiImplicitParam(name = "Authorization", required = true, paramType = "header", value = "身份认证Token")
    @SysLog(type = "0")
    @PostMapping(value = "/change-password")
    public Result changePwd(@JwtClaim String username, @RequestBody @Validated ChangePwdForm changePwdForm) {
        accountService.changePwd(username, changePwdForm);
        return WebUtils.ok(ResultEnum.SUCCESS);
    }

    @ApiOperation("查询用户日志,带分页")
    @ApiImplicitParam(name = "Authorization", required = true, paramType = "header", value = "身份认证Token")
    @PostMapping("/list-log")
    public Result<PageResult> list(@JwtClaim String username, @RequestBody Query query) {
        QueryWrapper<Log> queryWrapper = WebUtils.buildSearchQueryWrapper(query);
        queryWrapper.select("id", "username", "ip", "uri", "action_name", "create_time");
        queryWrapper.eq("username", username);
        queryWrapper.eq("type", "0"); // 0 代表用户可见，给用户看的日志
        queryWrapper.orderByDesc("create_time");

        Page<Log> page = WebUtils.buildSearchPage(query);
        PageResult<Log> pageResult = WebUtils.buildPageResult(logService.page(page, queryWrapper));
        return WebUtils.ok(pageResult);
    }

}
