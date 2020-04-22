package com.qinyou.apiserver.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qinyou.apiserver.sys.entity.MsgType;

/**
 * <p>
 * 系统通知消息类型 服务类
 * </p>
 *
 * @author chuang
 * @since 2019-12-31
 */
public interface IMsgTypeService extends IService<MsgType> {
    // 增改删 切换状态
    void add(MsgType msgType);

    void update(MsgType msgType);

    void remove(String id);

    void toggleState(String id);
}
