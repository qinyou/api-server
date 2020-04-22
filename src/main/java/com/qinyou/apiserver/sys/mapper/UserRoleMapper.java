package com.qinyou.apiserver.sys.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qinyou.apiserver.sys.entity.Role;
import com.qinyou.apiserver.sys.entity.UserRole;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 用户角色中间表 Mapper 接口
 * </p>
 *
 * @author chuang
 * @since 2019-12-10
 */
public interface UserRoleMapper extends BaseMapper<UserRole> {

    /**
     * 查询用户拥有的角色列表
     *
     * @param page
     * @param queryWrapper
     * @return
     */
    @Select("select a.id,a.name,a.intro,a.state,b.creater,b.create_time from sys_role a, sys_user_role b where a.id = b.role_id and b.user_id = #{user_id} and  ${ew.sqlSegment} ")
    IPage<Role> listHaveRoles(IPage<Role> page, @Param("user_id") String userId, @Param("ew") QueryWrapper<Role> queryWrapper);


    /**
     * 用户没有的 角色列表
     *
     * @param page
     * @param userId
     * @param queryWrapper
     * @return
     */
    @Select("select id,name,state from sys_role where id not in (select role_id from sys_user_role where user_id = #{user_id}) and  ${ew.sqlSegment} ")
    IPage<Role> listNoRoles(IPage<Role> page, @Param("user_id") String userId, @Param("ew") QueryWrapper<Role> queryWrapper);
}
