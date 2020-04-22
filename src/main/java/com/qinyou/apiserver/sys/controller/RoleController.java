package com.qinyou.apiserver.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qinyou.apiserver.core.base.*;
import com.qinyou.apiserver.core.utils.WebUtils;
import com.qinyou.apiserver.sys.entity.Resource;
import com.qinyou.apiserver.sys.entity.Role;
import com.qinyou.apiserver.sys.service.IRoleResourceService;
import com.qinyou.apiserver.sys.service.IRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 系统角色表 前端控制器
 * </p>
 *
 * @author chuang
 * @since 2019-10-19
 */
@SuppressWarnings("Duplicates")
@Api(tags = "2.角色管理")
@RestController
@RequestMapping("/sys/role")
public class RoleController {
    @Autowired
    IRoleService roleService;

    @Autowired
    IRoleResourceService roleResourceService;

    @ApiOperation("查询列表,带分页")
    @PreAuthorize("hasAuthority('sysRole')")
    @PostMapping("/list")
    public Result<PageResult<Role>> list(@RequestBody Query query) {
        QueryWrapper<Role> queryWrapper = WebUtils.buildSearchQueryWrapper(query);
        queryWrapper.orderByAsc("create_time");
        IPage<Role> page = WebUtils.buildSearchPage(query);
        PageResult<Role> pageResult = WebUtils.buildPageResult(roleService.page(page, queryWrapper));
        return WebUtils.ok(pageResult);
    }

    @ApiOperation("添加角色")
    @SysLog()
    @PreAuthorize("hasAuthority('sysRole:add')")
    @PostMapping("/add")
    public Result add(@RequestBody @Validated Role role) {
        roleService.add(role);
        return WebUtils.ok(ResultEnum.SUCCESS);
    }

    @ApiOperation(value = "修改角色")
    @SysLog()
    @PreAuthorize("hasAuthority('sysRole:update')")
    @PostMapping("/update")
    public Result update(@RequestBody @Validated Role role) {
        roleService.update(role);
        return WebUtils.ok(ResultEnum.SUCCESS);
    }

    @ApiOperation(value = "删除角色")
    @SysLog()
    @PreAuthorize("hasAuthority('sysRole:remove')")
    @GetMapping("/remove/{id}")
    public Result remove(@PathVariable("id") String id) {
        roleService.remove(id);
        return WebUtils.ok(ResultEnum.SUCCESS);
    }

    @ApiOperation(value = "切换状态，如果为ON变为OFF，如果为OFF变更为ON")
    @SysLog()
    @PreAuthorize("hasAuthority('sysRole:toggle')")
    @GetMapping("/toggle-state/{id}")
    public Result toggleState(@PathVariable String id) {
        roleService.toggleState(id);
        return WebUtils.ok(ResultEnum.SUCCESS);
    }

    /********************************角色配置资源*********************************/
    @ApiOperation(value = "获得角色详情")
    @PreAuthorize("hasAuthority('sysRole:configResources')")
    @GetMapping("/detail/{id}")
    public Result<Role> detail(@PathVariable String id) {
        return WebUtils.ok(roleService.getById(id));
    }


    @ApiOperation(value = "角色没有的资源列表")
    @PreAuthorize("hasAuthority('sysRole:configResources')")
    @PostMapping("/list-no-resources/{roleId}")
    public Result<PageResult<Resource>> listNoResources(@PathVariable String roleId, @RequestBody Query query) {
        return WebUtils.ok(roleResourceService.listResources(false, roleId, query));
    }

    @ApiOperation(value = "角色拥有的资源列表")
    @PreAuthorize("hasAuthority('sysRole:configResources')")
    @PostMapping("/list-have-resources/{roleId}")
    public Result<PageResult<Resource>> listHaveResources(@PathVariable String roleId, @RequestBody Query query) {
        return WebUtils.ok(roleResourceService.listResources(true, roleId, query));
    }

    @ApiOperation(value = "删除角色相关资源")
    @SysLog()
    @PreAuthorize("hasAuthority('sysRole:configResources')")
    @PostMapping("/del-role-resources/{roleId}")
    public Result deleteRoleResources(@PathVariable String roleId, @RequestBody List<String> resourceIds) {
        roleResourceService.delRoleResources(roleId, resourceIds);
        return WebUtils.ok(ResultEnum.SUCCESS);
    }


    @ApiOperation(value = "增加角色相关资源")
    @SysLog()
    @PreAuthorize("hasAuthority('sysRole:configResources')")
    @PostMapping("/add-role-resources/{roleId}")
    public Result addRoleResources(@PathVariable String roleId, @RequestBody List<String> resourceIds) {
        roleResourceService.addRoleResources(roleId, resourceIds);
        return WebUtils.ok(ResultEnum.SUCCESS);
    }
}
