package com.qinyou.apiserver.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qinyou.apiserver.sys.entity.DataDict;

import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 数据字典 服务类
 * </p>
 *
 * @author chuang
 * @since 2019-10-23
 */
public interface IDataDictService extends IService<DataDict> {
    /**
     * 递归查询当前当前节点的 所有子节点，不包括当前节点
     *
     * @param root
     */
    void findList(DataDict root);


    void add(DataDict dto);

    void update(DataDict dto);

    void remove(String id);

    void toggleState(String id);

    /**
     * 递归查询当前节点的所有 子节点id，不包括自身
     *
     * @param id
     * @param findedIds
     */
    void findChildrenIds(String id, Set<String> findedIds);

    /**
     * 查询树形结构数据，供给 树形下拉框使用
     *
     * @param root
     * @param disableKey 需要禁用的 key
     */
    void findPForTreeSelect(Map<String, Object> root, Set<String> disableKey);
}
