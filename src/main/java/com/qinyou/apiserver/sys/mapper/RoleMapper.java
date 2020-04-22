package com.qinyou.apiserver.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qinyou.apiserver.sys.entity.Role;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Set;

/**
 * <p>
 * 系统角色表 Mapper 接口
 * </p>
 *
 * @author chuang
 * @since 2019-10-19
 */
public interface RoleMapper extends BaseMapper<Role> {
    /**
     * 查询角色相关的用户名列表
     *
     * @param roleId 用户id（用户名）
     * @return
     */
    @Select("select distinct a.user_id from sys_user_role a, sys_role b  where a.role_id = b.id and b.state = 'ON' and b.id = #{role_id}  ")
    Set<String> getRoleUsers(@Param("role_id") String roleId);
}
