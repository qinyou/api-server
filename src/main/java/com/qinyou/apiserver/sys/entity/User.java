package com.qinyou.apiserver.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qinyou.apiserver.core.base.BaseEntity;
import com.qinyou.apiserver.core.base.Email;
import com.qinyou.apiserver.core.base.Phone;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * <p>
 * 系统用户表
 * </p>
 *
 * @author chuang
 * @since 2019-10-19
 */
@Data
@Accessors(chain = true)
@TableName("sys_user")
@ApiModel(value = "User对象", description = "系统用户表")
public class User extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户名")
    @NotBlank(message = "{user.usernameBlank}")
    @TableId(value = "id")
    private String id;

    @ApiModelProperty(hidden = true)
    @TableField("password")
    @JsonIgnore
    private String password;

    @ApiModelProperty(value = "昵称/姓名")
    @TableField("nickname")
    private String nickname;

    @ApiModelProperty(value = "状态，0正常，1禁用")
    @NotBlank(message = "{com.stateBlank}")
    @TableField("state")
    private String state;

    @ApiModelProperty(value = "手机")
    @Phone(message = "{user.phonePattern}")
    @TableField("phone")
    private String phone;

    @ApiModelProperty(value = "邮箱")
    @Email(message = "{user.emailPattern}")
    @TableField("email")
    private String email;

    @ApiModelProperty(value = "介绍信息")
    @TableField("intro")
    private String intro;

    @ApiModelProperty(value = "头像")
    @TableField("avatar")
    private String avatar;
}
