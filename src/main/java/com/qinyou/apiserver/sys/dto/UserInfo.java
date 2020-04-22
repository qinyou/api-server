package com.qinyou.apiserver.sys.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Set;

@Data
@ApiModel(description = "用户个人信息")
public class UserInfo {

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private String password;

    @ApiModelProperty(value = "姓名")
    private String nickname;

    @ApiModelProperty(value = "电话")
    private String phone;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "简介")
    private String intro;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @JsonIgnore
    private String state;

    @ApiModelProperty(value = "角色集合")
    private Set<String> roles;

    @ApiModelProperty(value = "资源集合")
    private Set<String> resources;
}
