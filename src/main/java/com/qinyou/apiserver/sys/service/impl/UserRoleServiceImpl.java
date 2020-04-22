package com.qinyou.apiserver.sys.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qinyou.apiserver.core.base.PageResult;
import com.qinyou.apiserver.core.base.Query;
import com.qinyou.apiserver.core.utils.WebUtils;
import com.qinyou.apiserver.sys.entity.Role;
import com.qinyou.apiserver.sys.entity.UserRole;
import com.qinyou.apiserver.sys.mapper.UserRoleMapper;
import com.qinyou.apiserver.sys.service.IUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 用户角色中间表 服务实现类
 * </p>
 *
 * @author chuang
 * @since 2019-12-10
 */
@SuppressWarnings({"Duplicates", "SpringJavaInjectionPointsAutowiringInspection"})
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {

    @Autowired
    UserRoleMapper userRoleMapper;

    @Transactional
    @Override
    public void addUserRoles(String userId, List<String> roleIds) {
        for (String roleId : roleIds) {
            UserRole userRole = new UserRole()
                    .setUserId(userId).setRoleId(roleId)
                    .setCreateTime(LocalDateTime.now())
                    .setCreater(WebUtils.getSecurityUsername());
            this.save(userRole);
        }
    }

    @Override
    public void delUserRoles(String userId, List<String> roleIds) {
        if (roleIds.size() > 0) {
            this.remove(new QueryWrapper<UserRole>().eq("user_id", userId).in("role_id", roleIds));
        }
    }


    @Override
    public PageResult<Role> listRoles(boolean flag, String userId, Query query) {
        // 查询参数
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        String key = query.getFilter() != null ? query.getFilter().get("key") : null;
        if (StrUtil.isNotBlank(key)) {
            if (flag) {
                queryWrapper.like("a.name", key).or().like("a.id", key);
                queryWrapper.orderByDesc("b.create_time");
            } else {
                queryWrapper.like("name", key).or().like("id", key);
                queryWrapper.orderByAsc("create_time");
            }
        } else {
            queryWrapper.eq("1", 1);  // 因为 mapper 中已拼接了 and 语句
        }

        // 分页参数
        IPage<Role> page = WebUtils.buildSearchPage(query);
        if (flag) {
            userRoleMapper.listHaveRoles(page, userId, queryWrapper);
        } else {
            userRoleMapper.listNoRoles(page, userId, queryWrapper);
        }
        // 结果封装
        PageResult<Role> pageResult = WebUtils.buildPageResult(page);
        return pageResult;
    }
}
