package com.qinyou.apiserver.core.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


/**
 * 排序参数
 */
@Getter
@Setter
@ApiModel("排序条件")
public class Order implements Serializable {
    @ApiModelProperty(value = "排序字段")
    public String column;

    @ApiModelProperty(value = "排序方式")
    public String sort = "asc";
}
