package com.qinyou.apiserver.core.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


/**
 * 查询查询参数
 */
@Getter
@Setter
@ApiModel("分页查询条件")
public class Query implements Serializable {
    @ApiModelProperty(value = "当前页", example = "1")
    public Long current = 1L;

    @ApiModelProperty(value = "每页大小", example = "30")
    public Long pageSize = 30L;

    @ApiModelProperty(value = "查询参数, key格式例子 search_EQ_id")
    public Map<String, String> filter;

    @ApiModelProperty(value = "排序参数")
    public List<Order> orders;
}
