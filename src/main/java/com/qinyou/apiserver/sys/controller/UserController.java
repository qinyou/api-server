package com.qinyou.apiserver.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qinyou.apiserver.core.base.*;
import com.qinyou.apiserver.core.utils.WebUtils;
import com.qinyou.apiserver.sys.entity.Role;
import com.qinyou.apiserver.sys.entity.User;
import com.qinyou.apiserver.sys.service.IUserRoleService;
import com.qinyou.apiserver.sys.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 系统用户表 前端控制器
 * </p>
 *
 * @author chuang
 * @since 2019-10-19
 */
@SuppressWarnings("Duplicates")
@Api(tags = "3.用户管理")
@RestController
@RequestMapping("/sys/user")
public class UserController {
    @Autowired
    IUserService userService;

    @Autowired
    IUserRoleService userRoleService;

    @Value("${app.user-default-password}")
    String userDefaultPwd;

    @ApiOperation("查询用户列表")
    @PreAuthorize("hasAuthority('sysUser')")
    @PostMapping("/list")
    public Result<PageResult<User>> list(@RequestBody Query query) {
        QueryWrapper<User> queryWrapper = WebUtils.buildSearchQueryWrapper(query);
        queryWrapper.orderByAsc("create_time");
        IPage<User> page = WebUtils.buildSearchPage(query);
        PageResult<User> pageResult = WebUtils.buildPageResult(userService.page(page, queryWrapper));
        return WebUtils.ok(pageResult);
    }

    @ApiOperation("添加用户")
    @SysLog()
    @PreAuthorize("hasAuthority('sysUser:add')")
    @PostMapping("/add")
    public Result add(@RequestBody @Validated User user) {
        userService.add(user);
        return WebUtils.ok(ResultEnum.SUCCESS);
    }

    @ApiOperation(value = "修改用户")
    @SysLog()
    @PreAuthorize("hasAuthority('sysUser:update')")
    @PostMapping("/update")
    public Result update(@RequestBody @Validated User user) {
        userService.update(user);
        return WebUtils.ok(ResultEnum.SUCCESS);
    }

    @ApiOperation(value = "删除用户")
    @SysLog()
    @PreAuthorize("hasAuthority('sysUser:remove')")
    @GetMapping("/remove/{id}")
    public Result remove(@PathVariable("id") String id) {
        userService.remove(id);
        return WebUtils.ok(ResultEnum.SUCCESS);
    }

    @ApiOperation(value = "切换状态，0变1 或 1变0")
    @SysLog()
    @PreAuthorize("hasAuthority('sysUser:toggle')")
    @GetMapping("/toggle-state/{id}")
    public Result toggleState(@PathVariable String id) {
        userService.toggleState(id);
        return WebUtils.ok(ResultEnum.SUCCESS);
    }

    @ApiOperation(value = "重置用户密码")
    @SysLog()
    @PreAuthorize("hasAuthority('sysUser:resetPwd')")
    @GetMapping("/reset-pwd/{id}")
    public Result resetPassword(@PathVariable String id) {
        userService.resetPwd(id);
        return WebUtils.ok(ResultEnum.RESET_PWD_SUCCESS, new Object[]{userDefaultPwd});
    }


    /*****************************用户配置角色相关*********************************/
    @ApiOperation(value = "获得用户详情")
    @PreAuthorize("hasAuthority('sysUser:configRoles')")
    @GetMapping("/detail/{id}")
    public Result<User> detail(@PathVariable String id) {
        return WebUtils.ok(userService.getById(id));
    }

    @ApiOperation(value = "用户没有的角色列表")
    @PreAuthorize("hasAuthority('sysUser:configRoles')")
    @PostMapping("/list-no-roles/{userId}")
    public Result<PageResult<Role>> listNoRoles(@PathVariable String userId, @RequestBody Query query) {
        return WebUtils.ok(userRoleService.listRoles(false, userId, query));
    }

    @ApiOperation(value = "用户拥有的角色列表")
    @PreAuthorize("hasAuthority('sysUser:configRoles')")
    @PostMapping("/list-have-roles/{userId}")
    public Result<PageResult<Role>> listHaveRoles(@PathVariable String userId, @RequestBody Query query) {
        return WebUtils.ok(userRoleService.listRoles(true, userId, query));
    }

    @ApiOperation(value = "删除用户相关角色")
    @SysLog()
    @PreAuthorize("hasAuthority('sysUser:configRoles')")
    @PostMapping("/del-user-roles/{userId}")
    public Result deleteUserRoles(@PathVariable String userId, @RequestBody List<String> roleIds) {
        userRoleService.delUserRoles(userId, roleIds);
        return WebUtils.ok(ResultEnum.SUCCESS);
    }


    @ApiOperation(value = "增加用户相关角色")
    @SysLog()
    @PreAuthorize("hasAuthority('sysUser:configRoles')")
    @PostMapping("/add-user-roles/{userId}")
    public Result addUserRoles(@PathVariable String userId, @RequestBody List<String> roleIds) {
        userRoleService.addUserRoles(userId, roleIds);
        return WebUtils.ok(ResultEnum.SUCCESS);
    }
}

