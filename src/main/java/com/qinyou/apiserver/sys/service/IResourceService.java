package com.qinyou.apiserver.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qinyou.apiserver.sys.entity.Resource;

/**
 * <p>
 * 系统资源表 服务类
 * </p>
 *
 * @author chuang
 * @since 2019-10-19
 */
public interface IResourceService extends IService<Resource> {
    void add(Resource resource);

    void update(Resource resource);

    void remove(String id);

    void toggleState(String id);
}
