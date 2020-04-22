package com.qinyou.apiserver.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qinyou.apiserver.core.base.*;
import com.qinyou.apiserver.core.utils.WebUtils;
import com.qinyou.apiserver.sys.entity.Log;
import com.qinyou.apiserver.sys.service.ILogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 用户操作日志 前端控制器
 * </p>
 *
 * @author chuang
 * @since 2019-12-15
 */
@SuppressWarnings({"Duplicates"})
@Api(tags = "7.操作日志")
@RestController
@RequestMapping("/sys/log")
public class LogController {
    @Autowired
    ILogService logService;

    @ApiOperation(value = "查询列表,带分页")
    @PreAuthorize("hasAuthority('sysLog')")
    @PostMapping("/list")
    public Result<PageResult<Log>> list(@RequestBody Query query) {
        QueryWrapper<Log> queryWrapper = WebUtils.buildSearchQueryWrapper(query);
        IPage<Log> page = WebUtils.buildSearchPage(query);
        PageResult<Log> pageResult = WebUtils.buildPageResult(logService.page(page, queryWrapper));
        return WebUtils.ok(pageResult);
    }


    @ApiOperation(value = "批量删除操作日志")
    @SysLog()
    @PreAuthorize("hasAuthority('sysLog:remove')")
    @PostMapping("/batch-remove")
    public Result remove(@RequestBody List<String> ids) {
        logService.removeByIds(ids);
        return WebUtils.ok(ResultEnum.SUCCESS);
    }
}
