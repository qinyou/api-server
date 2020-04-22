package com.qinyou.apiserver.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qinyou.apiserver.core.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 系统通知消息类型
 * </p>
 *
 * @author chuang
 * @since 2019-12-31
 */
@Data
@Accessors(chain = true)
@TableName("sys_msg_type")
@ApiModel(value = "MsgType对象", description = "系统通知消息类型")
public class MsgType extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "类型编码")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty(value = "推送方式")
    @TableField("push_mode")
    private String pushMode;

    @ApiModelProperty(value = "名称")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "LOGO图标")
    @TableField("logo")
    private String logo;

    @ApiModelProperty(value = "状态ON/OFF")
    @TableField("state")
    private String state;

    @ApiModelProperty(value = "消息模板")
    @TableField("template")
    private String template;

    @ApiModelProperty(value = "备注")
    @TableField("remark")
    private String remark;

    @ApiModelProperty(value = "过期天数（多少天过期）")
    @TableField("expiry")
    private Integer expiry;

    @ApiModelProperty(value = "存活天数 （多少天自动删除）")
    @TableField("dead")
    private Integer dead;

    @ApiModelProperty(value = "用户编码或角色编码，多个逗号分隔,user:xx,role:xx")
    @TableField("audience")
    private String audience;
}
