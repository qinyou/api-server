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
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 数据字典
 * </p>
 *
 * @author chuang
 * @since 2019-10-30
 */
@Data
@Accessors(chain = true)
@TableName("sys_data_dict")
@ApiModel(value = "DataDict对象", description = "数据字典")
public class DataDict extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(hidden = true)
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty(value = "名称", required = true)
    @NotBlank(message = "{com.nameBlank}")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "编码", required = true)
    @NotBlank(message = "{com.codeBlank}")
    @TableField("code")
    private String code;

    @ApiModelProperty(value = "状态,ON/OFF", allowableValues = "ON,OFF", required = true)
    @NotBlank(message = "{com.stateBlank}")
    @TableField("state")
    private String state;

    @ApiModelProperty(value = "父级id")
    @TableField("pid")
    private String pid;

    @ApiModelProperty(value = "排序号")
    @TableField("sort")
    private Integer sort;

    // 非数据表字段
    @ApiModelProperty(hidden = true) // 不加 swagger 无法使用
    @TableField(exist = false)
    private List<DataDict> children;
}
