package com.qinyou.apiserver.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 用户角色中间表
 * </p>
 *
 * @author chuang
 * @since 2019-12-10
 */
@Data
@Accessors(chain = true)
@TableName("sys_user_role")
public class UserRole {
    @TableField(value = "create_time", select = false)
    public LocalDateTime createTime;

    @TableField(value = "creater", select = false)
    public String creater;

    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @TableField("user_id")
    private String userId;

    @TableField("role_id")
    private String roleId;


}
