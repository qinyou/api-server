package com.qinyou.apiserver.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qinyou.apiserver.core.utils.WebUtils;
import com.qinyou.apiserver.sys.entity.Role;
import com.qinyou.apiserver.sys.entity.RoleResource;
import com.qinyou.apiserver.sys.entity.UserRole;
import com.qinyou.apiserver.sys.mapper.RoleMapper;
import com.qinyou.apiserver.sys.service.IRoleResourceService;
import com.qinyou.apiserver.sys.service.IRoleService;
import com.qinyou.apiserver.sys.service.IUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

/**
 * <p>
 * 系统角色表 服务实现类
 * </p>
 *
 * @author chuang
 * @since 2019-10-19
 */
@SuppressWarnings({"Duplicates", "SpringJavaInjectionPointsAutowiringInspection"})
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {
    @Autowired
    IUserRoleService userRoleService;
    @Autowired
    IRoleResourceService roleResourceService;
    @Autowired
    RoleMapper roleMapper;

    @Override
    public void add(Role role) {
        role.setCreater(WebUtils.getSecurityUsername())
                .setCreateTime(LocalDateTime.now());
        this.save(role);
    }

    @Override
    public void update(Role role) {
        role.setUpdater(WebUtils.getSecurityUsername())
                .setUpdateTime(LocalDateTime.now());
        this.updateById(role);
    }

    @Transactional
    @Override
    public void remove(String id) {
        Role role = Optional.of(this.getById(id)).get();
        this.removeById(id);
        userRoleService.remove(new QueryWrapper<UserRole>().eq("role_id", id));
        roleResourceService.remove(new QueryWrapper<RoleResource>().eq("role_id", id));
    }

    @Override
    public void toggleState(String id) {
        Role role = Optional.of(this.getById(id)).get();
        String state = "ON".equals(role.getState()) ? "OFF" : "ON";
        UpdateWrapper<Role> wraper = new UpdateWrapper<>();
        wraper.set("state", state)
                .set("updater", WebUtils.getSecurityUsername())
                .set("update_time", LocalDateTime.now())
                .eq("id", id);
        this.update(wraper);
    }

    @Override
    public Set<String> findUsersByRole(String roleId) {
        return roleMapper.getRoleUsers(roleId);
    }
}
