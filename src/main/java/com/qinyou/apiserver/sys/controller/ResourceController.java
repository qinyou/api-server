package com.qinyou.apiserver.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qinyou.apiserver.core.base.*;
import com.qinyou.apiserver.core.utils.WebUtils;
import com.qinyou.apiserver.sys.entity.Resource;
import com.qinyou.apiserver.sys.service.IResourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 系统资源表 前端控制器
 * </p>
 *
 * @author chuang
 * @since 2019-10-19
 */
@Api(tags = "1.资源管理")
@RestController
@RequestMapping("/sys/resource")
public class ResourceController {
    @Autowired
    IResourceService resourceService;

    @ApiOperation("资源列表")
    @PreAuthorize("hasAuthority('sysResource')")
    @PostMapping("/list")
    public Result<PageResult<Resource>> list(@RequestBody Query query) {
        QueryWrapper<Resource> queryWrapper = WebUtils.buildSearchQueryWrapper(query);
        queryWrapper.eq("type", "menu").orderByAsc("sort");
        IPage<Resource> pageQuery = WebUtils.buildSearchPage(query);
        IPage<Resource> pageData = resourceService.page(pageQuery, queryWrapper);
        for (Resource resource : pageData.getRecords()) {
            List<Resource> children = resourceService.list(new QueryWrapper<Resource>()
                    .likeRight("id", resource.getId() + ":")
                    .orderByAsc("sort"));
            if (children.size() > 0) resource.setChildren(children);
        }

        return WebUtils.ok(WebUtils.buildPageResult(pageData));
    }

    @ApiOperation("添加资源")
    @SysLog()
    @PreAuthorize("hasAuthority('sysResource:add')")
    @PostMapping("/add")
    public Result add(@RequestBody @Validated Resource resource) {
        resourceService.add(resource);
        return WebUtils.ok(ResultEnum.SUCCESS);
    }

    @ApiOperation("修改资源")
    @SysLog()
    @PreAuthorize("hasAuthority('sysResource:update')")
    @PostMapping("/update")
    public Result update(@RequestBody @Validated Resource resource) {
        resourceService.update(resource);
        return WebUtils.ok(ResultEnum.SUCCESS);
    }


    @ApiOperation("删除资源")
    @SysLog()
    @PreAuthorize("hasAuthority('sysResource:remove')")
    @GetMapping("/remove/{id}")
    public Result remove(@PathVariable("id") String id) {
        resourceService.remove(id);
        return WebUtils.ok(ResultEnum.SUCCESS);
    }


    @ApiOperation("切换状态")
    @SysLog()
    @PreAuthorize("hasAuthority('sysResource:toggle')")
    @GetMapping("/toggle-state/{id}")
    public Result toggleState(@PathVariable String id) {
        resourceService.toggleState(id);
        return WebUtils.ok(ResultEnum.SUCCESS);
    }
}

