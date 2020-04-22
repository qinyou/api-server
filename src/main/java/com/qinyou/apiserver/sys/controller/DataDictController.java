package com.qinyou.apiserver.sys.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qinyou.apiserver.core.base.PageResult;
import com.qinyou.apiserver.core.base.Query;
import com.qinyou.apiserver.core.base.Result;
import com.qinyou.apiserver.core.base.ResultEnum;
import com.qinyou.apiserver.core.utils.WebUtils;
import com.qinyou.apiserver.sys.entity.DataDict;
import com.qinyou.apiserver.sys.service.IDataDictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 数据字典 前端控制器
 * </p>
 *
 * @author chuang
 * @since 2019-10-23
 */
@Api(tags = "4.数据字典")
@RestController
@RequestMapping("/sys/data-dict")
public class DataDictController {
    @Autowired
    IDataDictService dataDictService;

    @ApiOperation("数组字典列表,不翻页，树形结构数据")
    @PreAuthorize("hasAuthority('sysDict')")
    @PostMapping(value = "/list")
    public Result<PageResult<DataDict>> list(@RequestBody Query query) {
        QueryWrapper<DataDict> queryWrapper = new QueryWrapper<>();
        String type = (String) query.getFilter().get("type");
        String key = (String) query.getFilter().get("key");

        if (StrUtil.isNotBlank(key)) {
            // 有查询条件
            if ("LIKE".equals(type)) {
                // 名称或编码 模糊匹配
                queryWrapper.like("name", key).or().like("code", key);
            } else if ("EQ".equals(type)) {
                // 编码精确查找
                queryWrapper.eq("code", key);
            }
        } else {
            // 无查询条件
            queryWrapper.eq("pid", "0");
        }
        queryWrapper.orderByAsc("sort");

        IPage<DataDict> page = WebUtils.buildSearchPage(query);
        PageResult<DataDict> pageResult = WebUtils.buildPageResult(dataDictService.page(page, queryWrapper));

        // 没有查询条件 或 编码精确匹配 查询层级结构
        if (StrUtil.isBlank(key) || "EQ".equals(type)) {
            for (DataDict dataDict : pageResult.getRecords()) {
                dataDictService.findList(dataDict);
            }
        }

        return WebUtils.ok(pageResult);
    }

    @ApiOperation("treeSelect 组件数据")
    @PreAuthorize("hasAuthority('sysDict')")
    @GetMapping(value = {"/list-tree-select/{currentKey}", "/list-tree-select"})
    public Result<Map<String, Object>> listTreeSelect(@PathVariable(required = false) String currentKey) {
        Map<String, Object> root = new HashMap<>();
        root.put("key", "0");
        root.put("label", "根节点");
        root.put("value", "0");
        Set<String> childrenKey = new HashSet<>();
        if (StrUtil.isNotBlank(currentKey)) {
            dataDictService.findChildrenIds(currentKey, childrenKey);
            childrenKey.add(currentKey);
        }
        dataDictService.findPForTreeSelect(root, childrenKey);
        return WebUtils.ok(root);
    }

    @ApiOperation("添加字典")
    @PreAuthorize("hasAuthority('sysDict:add')")
    @PostMapping("/add")
    public Result add(@RequestBody @Validated DataDict dataDict) {
        dataDictService.add(dataDict);
        return WebUtils.ok(ResultEnum.SUCCESS);
    }

    @ApiOperation("修改字典")
    @PreAuthorize("hasAuthority('sysDict:update')")
    @PostMapping("/update")
    public Result update(@RequestBody @Validated DataDict dataDict) {
        dataDictService.update(dataDict);
        return WebUtils.ok(ResultEnum.SUCCESS);
    }


    @ApiOperation("删除字典")
    @PreAuthorize("hasAuthority('sysDict:remove')")
    @GetMapping("/remove/{id}")
    public Result delete(@PathVariable String id) {
        dataDictService.remove(id);
        return WebUtils.ok(ResultEnum.SUCCESS);
    }

    @ApiOperation("切换状态，如果为ON变更为OFF，如果为OFF变更为ON")
    @PreAuthorize("hasAuthority('sysDict:toggle')")
    @GetMapping("/toggle-state/{id}")
    @Transactional
    public Result toggleState(@PathVariable String id) {
        dataDictService.toggleState(id);
        return WebUtils.ok(ResultEnum.SUCCESS);
    }
}
