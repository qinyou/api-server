package com.qinyou.apiserver.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 存放验证码
 * 用途：0 重置密码
 * 数据操作需保证： 同账号(account)、未使用(state=0)、同用途(purpose) 只有1条数据
 * </p>
 *
 * @author chuang
 * @since 2019-11-21
 */
@Data
@Accessors(chain = true)
@TableName("sys_verification_code")
@ApiModel(value = "VerificationCode对象", description = "各种验证码")
public class VerificationCode {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "创建时间")
    @TableField("create_time")
    public LocalDateTime createTime;
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;
    @ApiModelProperty(value = "账号")
    @TableField("account")
    private String account;
    @ApiModelProperty(value = "验证码")
    @TableField("code")
    private String code;
    @ApiModelProperty(value = "状态,0未用，1已用")
    @TableField("state")
    private String state;
    @ApiModelProperty(value = "用途")
    @TableField("purpose")
    private String purpose;
}
