package com.qinyou.apiserver.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qinyou.apiserver.core.base.RequestException;
import com.qinyou.apiserver.core.base.ResultEnum;
import com.qinyou.apiserver.core.utils.WebUtils;
import com.qinyou.apiserver.sys.entity.MsgType;
import com.qinyou.apiserver.sys.mapper.MsgTypeMapper;
import com.qinyou.apiserver.sys.service.IMsgTypeService;
import com.qinyou.apiserver.sys.service.IRoleService;
import com.qinyou.apiserver.sys.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * <p>
 * 系统通知消息类型 服务实现类
 * </p>
 *
 * @author chuang
 * @since 2019-12-31
 */
@Service
public class MsgTypeServiceImpl extends ServiceImpl<MsgTypeMapper, MsgType> implements IMsgTypeService {
    @Autowired
    IRoleService roleService;
    @Autowired
    IUserService userService;

    @Override
    public void add(MsgType msgType) {
        checkAudience(msgType.getAudience());
        msgType.setCreater(WebUtils.getSecurityUsername())
                .setCreateTime(LocalDateTime.now());
        this.save(msgType);
    }

    @Override
    public void update(MsgType msgType) {
        checkAudience(msgType.getAudience());
        msgType.setUpdateTime(LocalDateTime.now())
                .setUpdater(WebUtils.getSecurityUsername());
        this.updateById(msgType);
    }

    @Override
    public void remove(String id) {
        MsgType msgType = Optional.of(this.getById(id)).get();
        this.removeById(id);
    }

    @Override
    public void toggleState(String id) {
        MsgType msgType = Optional.of(this.getById(id)).get();
        String state = "ON".equals(msgType.getState()) ? "OFF" : "ON";
        this.update(
                new UpdateWrapper<MsgType>()
                        .set("state", state)
                        .set("updater", WebUtils.getSecurityUsername())
                        .set("update_time", LocalDateTime.now())
                        .eq("id", id)
        );
    }

    /**
     * 校验 角色编码、用户名是否存在
     */
    private void checkAudience(String audience) {
        String[] audiences = audience.split(",");
        if (audiences.length > 0) {
            String[] ary;
            for (String code : audiences) {
                ary = code.split(":");
                if (ary.length != 2 || (!"user".equals(ary[0]) && !"role".equals(ary[0]))) {
                    throw RequestException.fail(ResultEnum.BAD_PARAM);
                }
                if ("role".equals(ary[0]) && roleService.getById(ary[1]) == null) {
                    throw RequestException.fail(ResultEnum.ROLE_NOT_FOUND, new Object[]{code});
                }
                if ("user".equals(ary[0]) && userService.getById(ary[1]) == null) {
                    throw RequestException.fail(ResultEnum.USERNAME_NOT_FOUND, new Object[]{code});
                }
            }
        }
    }
}
