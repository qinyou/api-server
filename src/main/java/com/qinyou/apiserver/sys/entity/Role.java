package com.qinyou.apiserver.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qinyou.apiserver.core.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * <p>
 * 系统角色表
 * </p>
 *
 * @author chuang
 * @since 2019-10-19
 */
@Data
@Accessors(chain = true)
@TableName("sys_role")
@ApiModel(value = "Role对象", description = "系统角色表")
public class Role extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "编码")
    @NotBlank(message = "{com.codeBlank}")
    @TableId(value = "id")
    private String id;

    @ApiModelProperty(value = "名称")
    @NotBlank(message = "{com.nameBlank}")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "说明信息")
    @TableField("intro")
    private String intro;

    @ApiModelProperty(value = "状态ON/OFF")
    @TableField("state")
    private String state;
}
