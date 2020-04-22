package com.qinyou.apiserver.sys.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 消息主表，保存消息内容
 * </p>
 *
 * @author chuang
 * @since 2020-01-08
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "响应数据", description = "消息列表数据")
public class MsgInfo {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "消息类型logo")
    private String logo;

    @ApiModelProperty(value = "消息标题")
    private String title;

    @ApiModelProperty(value = "消息内容")
    private String content;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;


    @ApiModelProperty(value = "查看时间")
    private LocalDateTime readTime;
}
