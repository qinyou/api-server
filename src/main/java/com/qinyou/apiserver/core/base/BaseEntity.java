package com.qinyou.apiserver.core.base;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;


/**
 * Entity 基类
 *
 * @author chuang
 */
@Getter
@Setter
@Accessors(chain = true)
public class BaseEntity {

    @ApiModelProperty(hidden = true)
    @TableField(value = "create_time", select = false)
    public LocalDateTime createTime;

    @ApiModelProperty(hidden = true)
    @TableField(value = "creater", select = false)
    public String creater;

    @ApiModelProperty(hidden = true)
    @TableField(value = "update_time", select = false)
    public LocalDateTime updateTime;

    @ApiModelProperty(hidden = true)
    @TableField(value = "updater", select = false)
    public String updater;
}
