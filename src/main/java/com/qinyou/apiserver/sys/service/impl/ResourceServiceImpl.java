package com.qinyou.apiserver.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qinyou.apiserver.core.utils.WebUtils;
import com.qinyou.apiserver.sys.entity.Resource;
import com.qinyou.apiserver.sys.entity.RoleResource;
import com.qinyou.apiserver.sys.mapper.ResourceMapper;
import com.qinyou.apiserver.sys.service.IResourceService;
import com.qinyou.apiserver.sys.service.IRoleResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * <p>
 * 系统资源表 服务实现类
 * </p>
 *
 * @author chuang
 * @since 2019-10-19
 */
@SuppressWarnings({"Duplicates", "SpringJavaInjectionPointsAutowiringInspection"})
@Service
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource> implements IResourceService {

    @Autowired
    IRoleResourceService roleResourceService;

    @Override
    public void add(Resource resource) {
        resource.setCreater(WebUtils.getSecurityUsername())
                .setCreateTime(LocalDateTime.now());
        this.save(resource);
    }

    @Transactional
    @Override
    public void update(Resource resource) {
        Resource resourceOld = Optional.of(this.getById(resource.getId())).get();
        String oldState = resourceOld.getState();
        String type = resourceOld.getType();
        resource.setType(type) // 限制类型不可修改
                .setUpdater(WebUtils.getSecurityUsername())
                .setUpdateTime(LocalDateTime.now());
        this.updateById(resource);
        // 变更子级状态
        if (!resource.getState().equals(oldState) && "menu".equals(resource.getType())) {
            this.update(
                    new UpdateWrapper<Resource>()
                            .set("state", resource.getState())
                            .set("updater", WebUtils.getSecurityUsername())
                            .set("update_time", LocalDateTime.now())
                            .ne("id", resource.getId())
                            .likeLeft("id", resource.getId())
            );
        }
    }

    @Transactional
    @Override
    public void remove(String id) {
        Resource resource = Optional.of(this.getById(id)).get();
        // 删除自己以及子级
        this.remove(new QueryWrapper<Resource>().likeLeft("id", id));
        // 删中间表数据
        roleResourceService.remove(new QueryWrapper<RoleResource>().likeRight("resource_id", id));
    }

    @Override
    public void toggleState(String id) {
        Resource resource = Optional.of(this.getById(id)).get();
        String state = "ON".equals(resource.getState()) ? "OFF" : "ON";
        this.update(
                new UpdateWrapper<Resource>()
                        .set("state", state)
                        .set("updater", WebUtils.getSecurityUsername())
                        .set("update_time", LocalDateTime.now())
                        .likeRight("id", id)
        );
    }
}
