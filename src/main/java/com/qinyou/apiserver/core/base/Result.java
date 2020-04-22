package com.qinyou.apiserver.core.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
@ApiModel(value = "数据响应")
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 8992436576262574064L;
    @ApiModelProperty(value = "时间戳")
    private final long timestamps = System.currentTimeMillis();
    @ApiModelProperty(value = "状态")
    private boolean status;
    @ApiModelProperty(value = "状态码")
    private Integer code;
    @ApiModelProperty(value = "消息文本")
    private String msg;
    @ApiModelProperty(value = "数据体")
    private T data;
}
