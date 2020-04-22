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
 * 消息从表（保存接收人）
 * </p>
 *
 * @author chuang
 * @since 2020-01-08
 */
@Data
@Accessors(chain = true)
@TableName("sys_msg_detail")
public class MsgDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @TableField("msg_id")
    private String msgId;

    // 发送人
    @TableField("sender")
    private String sender;

    // 接收人
    @TableField("receiver")
    private String receiver;

    // 是否已读
    @TableField("is_read")
    private String isRead;

    // 查看时间
    @TableField("read_time")
    private LocalDateTime readTime;


}
