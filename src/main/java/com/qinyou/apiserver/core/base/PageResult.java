package com.qinyou.apiserver.core.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

/**
 * 分页查询结果封装
 */
@Getter
@AllArgsConstructor
@ApiModel("分页查询结果")
public class PageResult<T> implements Serializable {
    @ApiModelProperty(value = "当前第几页")
    Long current;

    @ApiModelProperty(value = "每页大小")
    Long pageSize;

    @ApiModelProperty(value = "数据总条数")
    Long total;

    @ApiModelProperty(value = "总页数")
    Long pages;

    @ApiModelProperty(value = "数据记录")
    List<T> records;
}
