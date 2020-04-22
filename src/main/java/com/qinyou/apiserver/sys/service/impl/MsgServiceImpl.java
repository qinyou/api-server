package com.qinyou.apiserver.sys.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qinyou.apiserver.core.utils.WebUtils;
import com.qinyou.apiserver.sys.entity.Msg;
import com.qinyou.apiserver.sys.entity.MsgDetail;
import com.qinyou.apiserver.sys.entity.MsgType;
import com.qinyou.apiserver.sys.mapper.MsgMapper;
import com.qinyou.apiserver.sys.service.*;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * <p>
 * 消息主表，保存消息内容 服务实现类
 * </p>
 *
 * @author chuang
 * @since 2020-01-08
 */
@Slf4j
@Service
public class MsgServiceImpl extends ServiceImpl<MsgMapper, Msg> implements IMsgService {
    @Autowired
    IMsgTypeService msgTypeService;
    @Autowired
    IMsgService msgService;
    @Autowired
    IMsgDetailService msgDetailService;
    @Autowired
    IRoleService roleService;
    @Autowired
    IUserService userService;

    @Transactional
    @Override
    public void triggerMsg(String typeCode, Map<String, Object> params) throws IOException, TemplateException {
        MsgType msgType = Optional.of(msgTypeService.getById(typeCode)).get();
        String audience = msgType.getAudience();
        if (StrUtil.isBlank(audience)) {
            return;
        }
        Set<String> usernameSet = new HashSet<>();
        String[] ary = audience.split(",");
        String[] itemAry;
        for (String item : ary) {
            itemAry = item.split(":");
            if ("user".equals(itemAry[0])) {
                usernameSet.add(itemAry[1]);
            }
            if ("role".equals(itemAry[0])) {
                usernameSet.addAll(roleService.findUsersByRole(itemAry[1]));
            }
        }
        if (usernameSet.size() == 0) {
            return;
        }
        // IOException, TemplateException
        String content = WebUtils.processTpl(msgType.getTemplate(), params);

        // 保存主表
        Msg msg = new Msg().setContent(content)
                .setTypeCode(msgType.getId())
                .setCreateTime(LocalDateTime.now())
                .setDeadTime(LocalDateTime.now().plusDays(msgType.getDead()))
                .setExpiryTime(LocalDateTime.now().plusDays(msgType.getExpiry()));
        msgService.save(msg);

        MsgDetail detail;
        for (String username : usernameSet) {
            detail = new MsgDetail().setMsgId(msg.getId())
                    .setReceiver(username)
                    .setSender("SYS")
                    .setIsRead("0");
            msgDetailService.save(detail);
            // 消息推送逻辑
        }
    }
}
