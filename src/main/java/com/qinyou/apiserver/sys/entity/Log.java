package com.qinyou.apiserver.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户操作日志
 * </p>
 *
 * @author chuang
 * @since 2019-12-15
 */
@Data
@Accessors(chain = true)
@TableName("sys_log")
@ApiModel(value = "Log对象", description = "")
public class Log implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "创建时间")
    @TableField("create_time")
    public LocalDateTime createTime;
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;
    @ApiModelProperty(value = "类型")
    @TableField("type")
    private String type;
    @ApiModelProperty(value = "用户名")
    @TableField("username")
    private String username;
    @ApiModelProperty(value = "ip地址")
    @TableField("ip")
    private String ip;
    @ApiModelProperty(value = "访问uri")
    @TableField("uri")
    private String uri;
    @ApiModelProperty(value = "参数")
    @TableField("params")
    private String params;
    @ApiModelProperty(value = "httpMethod")
    @TableField("http_method")
    private String httpMethod;
    @ApiModelProperty(value = "调用方法")
    @TableField("class_method")
    private String classMethod;
    @ApiModelProperty(value = "日志内容")
    @TableField("action_name")
    private String actionName;
    @ApiModelProperty(value = "方法返回参数")
    @TableField("result")
    private String result;


}
