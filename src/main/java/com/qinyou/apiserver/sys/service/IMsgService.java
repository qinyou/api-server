package com.qinyou.apiserver.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qinyou.apiserver.sys.entity.Msg;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.util.Map;

/**
 * <p>
 * 消息主表，保存消息内容 服务类
 * </p>
 *
 * @author chuang
 * @since 2020-01-08
 */
public interface IMsgService extends IService<Msg> {
    /**
     * 触发消息
     *
     * @param typeCode
     * @param params
     */
    void triggerMsg(String typeCode, Map<String, Object> params) throws IOException, TemplateException;
}
