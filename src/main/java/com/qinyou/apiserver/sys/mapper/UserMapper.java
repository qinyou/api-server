package com.qinyou.apiserver.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qinyou.apiserver.sys.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Set;

/**
 * <p>
 * 系统用户表 Mapper 接口
 * </p>
 *
 * @author chuang
 * @since 2019-10-19
 */
public interface UserMapper extends BaseMapper<User> {

    /**
     * 查询用户可用的角色编码
     *
     * @param userId 用户id（用户名）
     * @return
     */
    @Select("select distinct a.role_id from sys_user_role a, sys_role b  where a.role_id = b.id and b.state = 'ON' and a.user_id = #{user_id}  ")
    Set<String> getUserRoles(@Param("user_id") String userId);


    /**
     * 查询用户可用的资源编码
     *
     * @param userId
     * @return
     */
    @Select("select distinct a.resource_id from sys_role_resource a, sys_user_role b, sys_resource c, sys_role d  " +
            "where a.resource_id = c.id and a.role_id = d.id and b.role_id = d.id and b.user_id = #{user_id} and c.state = 'ON' and d.state = 'ON' ")
    Set<String> getUserResources(@Param("user_id") String userId);
}
