package com.qinyou.apiserver.sys.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qinyou.apiserver.sys.entity.Resource;
import com.qinyou.apiserver.sys.entity.RoleResource;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 角色资源 中间表 Mapper 接口
 * </p>
 *
 * @author chuang
 * @since 2019-12-10
 */
public interface RoleResourceMapper extends BaseMapper<RoleResource> {
    /**
     * 查询角色拥有的资源(菜单列表）
     *
     * @param page
     * @param queryWrapper
     * @return
     */
    @Select("select a.id,a.name,a.state,b.creater,b.create_time from sys_resource a, sys_role_resource b " +
            "where a.id = b.resource_id and b.role_id = #{role_id} and a.type = 'menu' and  ${ew.sqlSegment} ")
    IPage<Resource> listHaveResourcesMenu(IPage<Resource> page, @Param("role_id") String roleId, @Param("ew") QueryWrapper<Resource> queryWrapper);

    /**
     * 查询拥有的菜单列表下 的按钮
     *
     * @param resourceId
     * @return
     */
    @Select("select a.id,a.name,a.state,b.creater,b.create_time from sys_resource a, sys_role_resource b " +
            "where a.id = b.resource_id and a.type = 'btn' and b.role_id = #{role_id} and b.resource_id like '${resource_id}:%' order by a.sort ")
    List<Resource> listHaveResourcesBtn(@Param("role_id") String roleId, @Param("resource_id") String resourceId);

    /**
     * 查询角色没有的资源(菜单列表)
     *
     * @param page
     * @param roleId
     * @param queryWrapper
     * @return
     */
    @Select(" select b.id,b.name,b.state from (select SUBSTR(id,1,LOCATE(':', id)-1) as id from sys_resource where type = 'btn' and id not in (select resource_id from sys_role_resource where  role_id  = #{role_id}) GROUP BY  SUBSTR(id,1,LOCATE(':', id)-1)) a , sys_resource b where a.id = b.id and  ${ew.sqlSegment} ")
    IPage<Resource> listNoResourcesMenu(IPage<Resource> page, @Param("role_id") String roleId, @Param("ew") QueryWrapper<Resource> queryWrapper);

    /**
     * 查询未有的菜单列表下 的按钮
     *
     * @param roleId
     * @param resourceId
     * @return
     */
    @Select("select id,name,state from sys_resource where id not in (select resource_id from sys_role_resource " +
            "where role_id = #{role_id}) and type = 'btn' and  id like '${resource_id}:%' order by sort ")
    List<Resource> listNoResourcesBtn(@Param("role_id") String roleId, @Param("resource_id") String resourceId);
}
