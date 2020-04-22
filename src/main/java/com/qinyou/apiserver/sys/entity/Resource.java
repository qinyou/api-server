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

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 系统资源表
 * </p>
 *
 * @author chuang
 * @since 2019-11-12
 */
@Data
@Accessors(chain = true)
@TableName("sys_resource")
@ApiModel(value = "Resource对象", description = "系统资源")
public class Resource extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "资源编码", required = true)
    @NotBlank(message = "{com.codeBlank}")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty(value = "名称", required = true)
    @NotBlank(message = "{com.nameBlank}")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "状态ON开启OFF禁用", required = true, allowableValues = "ON,OFF")
    @NotBlank(message = "{com.stateBlank}")
    @TableField("state")
    private String state;

    @ApiModelProperty(value = "类型，menu菜单，btn按钮", required = true, allowableValues = "menu,btn")
    @NotBlank(message = "{com.typeBlank}")
    @TableField("type")
    private String type;

    @ApiModelProperty(value = "排序号", required = true)
    @NotNull(message = "{com.sortBlank}")
    @TableField("sort")
    private Integer sort;

    @ApiModelProperty(hidden = true)
    @TableField("icon")
    private String icon;

    @ApiModelProperty(value = "备注")
    @TableField("remark")
    private String remark;


    // 非数据表字段
    @ApiModelProperty(hidden = true) // 不加 swagger 无法使用
    @TableField(exist = false)
    private List<Resource> children;
}
