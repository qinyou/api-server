package com.qinyou.apiserver.sys.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qinyou.apiserver.core.base.PageResult;
import com.qinyou.apiserver.core.base.Query;
import com.qinyou.apiserver.core.utils.WebUtils;
import com.qinyou.apiserver.sys.entity.Resource;
import com.qinyou.apiserver.sys.entity.RoleResource;
import com.qinyou.apiserver.sys.mapper.RoleResourceMapper;
import com.qinyou.apiserver.sys.service.IRoleResourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 角色资源 中间表 服务实现类
 * </p>
 *
 * @author chuang
 * @since 2019-12-10
 */
@SuppressWarnings("Duplicates")
@Service
@Slf4j
public class RoleResourceServiceImpl extends ServiceImpl<RoleResourceMapper, RoleResource> implements IRoleResourceService {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    RoleResourceMapper roleResourceMapper;

    @Transactional
    @Override
    public void addRoleResources(String roleId, List<String> resourceIds) {
        for (String resourceId : resourceIds) {
            RoleResource roleResource = new RoleResource()
                    .setRoleId(roleId).setResourceId(resourceId);
            if (this.getOne(new QueryWrapper<>(roleResource)) == null) {
                roleResource.setCreateTime(LocalDateTime.now())
                        .setCreater(WebUtils.getSecurityUsername());
                this.save(roleResource);
            }
        }
    }

    @Override
    public void delRoleResources(String roleId, List<String> resourceIds) {
        if (resourceIds.size() > 0) {
            this.remove(new QueryWrapper<RoleResource>().eq("role_id", roleId).in("resource_id", resourceIds));
        }
    }

    @Override
    public PageResult<Resource> listResources(boolean flag, String roleId, Query query) {
        // 查询参数
        QueryWrapper<Resource> queryWrapper = new QueryWrapper<>();
        String key = query.getFilter() != null ? query.getFilter().get("key") : null;
        if (StrUtil.isNotBlank(key)) {
            if (flag) {
                queryWrapper.like("a.name", key).or().like("a.id", key);
            } else {
                queryWrapper.like("b.name", key).or().like("b.id", key);
            }
        } else {
            queryWrapper.eq("1", 1);  // 因为 mapper 中已拼接了 and 语句
        }
        if (flag) {
            queryWrapper.orderByAsc("b.create_time");
        } else {
            queryWrapper.orderByAsc("b.sort");
        }

        // 分页参数
        IPage<Resource> page = WebUtils.buildSearchPage(query);
        if (flag) {
            roleResourceMapper.listHaveResourcesMenu(page, roleId, queryWrapper);
            for (Resource resource : page.getRecords()) {
                List<Resource> resources = roleResourceMapper.listHaveResourcesBtn(roleId, resource.getId());
                if (resources.size() > 0) resource.setChildren(resources);
            }
        } else {
            roleResourceMapper.listNoResourcesMenu(page, roleId, queryWrapper);
            for (Resource resource : page.getRecords()) {
                List<Resource> resources = roleResourceMapper.listNoResourcesBtn(roleId, resource.getId());
                if (resources.size() > 0) resource.setChildren(resources);
            }
        }
        // 结果封装
        PageResult<Resource> pageResult = WebUtils.buildPageResult(page);
        return pageResult;
    }
}
