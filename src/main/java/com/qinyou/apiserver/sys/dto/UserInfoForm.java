package com.qinyou.apiserver.sys.dto;

import com.qinyou.apiserver.core.base.Email;
import com.qinyou.apiserver.core.base.Phone;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;


@Data
@ApiModel(description = "修改用户信息 请求参数")
public class UserInfoForm {
    @ApiModelProperty(value = "昵称/姓名")
    private String nickname;

    @NotBlank(message = "{user.phoneBlank}")
    @Phone(message = "{user.phonePattern}")
    @ApiModelProperty(value = "手机")
    private String phone;

    @ApiModelProperty(value = "邮箱")
    @NotBlank(message = "{user.emailBlank}")
    @Email(message = "{user.emailPattern}")
    private String email;

    @ApiModelProperty(value = "介绍信息")
    private String intro;

    @ApiModelProperty(value = "头像")
    @NotBlank(message = "{user.avatarBlank}")
    private String avatar;
}
