package com.qinyou.apiserver.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
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
@TableName("sys_msg")
public class Msg implements Serializable {
    private static final long serialVersionUID = 1L;
    // 创建日期
    @TableField("create_time")
    public LocalDateTime createTime;
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;
    // 类型编码
    @TableField("type_code")
    private String typeCode;
    // 消息内容
    @TableField("content")
    private String content;
    // 过期日期
    @TableField("expiry_time")
    private LocalDateTime expiryTime;
    // 作废日期
    @TableField("dead_time")
    private LocalDateTime deadTime;
}
