package com.qinyou.apiserver.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qinyou.apiserver.core.base.*;
import com.qinyou.apiserver.core.utils.WebUtils;
import com.qinyou.apiserver.sys.entity.MsgType;
import com.qinyou.apiserver.sys.service.IMsgTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 系统通知消息类型 前端控制器
 * </p>
 *
 * @author chuang
 * @since 2019-12-31
 */
@SuppressWarnings({"Duplicates"})
@Api(tags = "5.消息类型")
@RestController
@RequestMapping("/sys/msg-type")
public class MsgTypeController {
    @Autowired
    IMsgTypeService msgTypeService;

    @ApiOperation("查询消息类型列表")
    @PreAuthorize("hasAuthority('sysMsgType')")
    @PostMapping("/list")
    public Result<PageResult<MsgType>> list(@RequestBody Query query) {
        QueryWrapper<MsgType> queryWrapper = WebUtils.buildSearchQueryWrapper(query);
        IPage<MsgType> page = WebUtils.buildSearchPage(query);
        PageResult<MsgType> pageResult = WebUtils.buildPageResult(msgTypeService.page(page, queryWrapper));
        return WebUtils.ok(pageResult);
    }

    @ApiOperation("添加消息类型")
    @SysLog()
    @PreAuthorize("hasAuthority('sysMsgType:add')")
    @PostMapping("/add")
    public Result add(@RequestBody @Validated MsgType msgType) {
        msgTypeService.add(msgType);
        return WebUtils.ok(ResultEnum.SUCCESS);
    }

    @ApiOperation(value = "修改消息类型")
    @SysLog()
    @PreAuthorize("hasAuthority('sysMsgType:update')")
    @PostMapping("/update")
    public Result update(@RequestBody @Validated MsgType msgType) {
        msgTypeService.update(msgType);
        return WebUtils.ok(ResultEnum.SUCCESS);
    }

    @ApiOperation(value = "删除消息类型")
    @SysLog()
    @PreAuthorize("hasAuthority('sysMsgType:remove')")
    @GetMapping("/remove/{id}")
    public Result remove(@PathVariable("id") String id) {
        msgTypeService.remove(id);
        return WebUtils.ok(ResultEnum.SUCCESS);
    }

    @ApiOperation(value = "切换状态，ON变OFF 或 OFF变ON")
    @SysLog()
    @PreAuthorize("hasAuthority('sysMsgType:toggle')")
    @GetMapping("/toggle-state/{id}")
    public Result toggleState(@PathVariable String id) {
        msgTypeService.toggleState(id);
        return WebUtils.ok(ResultEnum.SUCCESS);
    }
}
