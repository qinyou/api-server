package com.qinyou.apiserver.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qinyou.apiserver.core.base.PageResult;
import com.qinyou.apiserver.core.base.Query;
import com.qinyou.apiserver.sys.entity.Resource;
import com.qinyou.apiserver.sys.entity.RoleResource;

import java.util.List;

/**
 * <p>
 * 角色资源 中间表 服务类
 * </p>
 *
 * @author chuang
 * @since 2019-12-10
 */
public interface IRoleResourceService extends IService<RoleResource> {
    /**
     * 添加
     *
     * @param roleId
     * @param resourceIds
     */
    void addRoleResources(String roleId, List<String> resourceIds);

    /**
     * 删除
     *
     * @param roleId
     * @param resourceIds
     */
    void delRoleResources(String roleId, List<String> resourceIds);


    /**
     * 分页查询 角色拥有的资源 或 角色没有的资源
     *
     * @param haveFlag true 拥有的资源，false 没有的资源
     * @param roleId
     * @param query
     * @return
     */
    PageResult<Resource> listResources(boolean haveFlag, String roleId, Query query);
}
