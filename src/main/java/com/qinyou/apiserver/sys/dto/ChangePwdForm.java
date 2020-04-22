package com.qinyou.apiserver.sys.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * 修改密码 dto
 */
@Getter
@Setter
@ApiModel(description = "修改密码请求参数")
public class ChangePwdForm {

    @ApiModelProperty(value = "原密码", required = true)
    @NotBlank(message = "{user.oldPwdBlank}")
    private String oldPwd;

    @ApiModelProperty(value = "新密码", required = true)
    @NotBlank(message = "{user.newPwdBlank}")
    private String newPwd;
}
