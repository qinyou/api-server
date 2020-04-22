package com.qinyou.apiserver.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qinyou.apiserver.sys.entity.DataDict;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 数据字典 Mapper 接口
 * </p>
 *
 * @author chuang
 * @since 2019-10-23
 */
public interface DataDictMapper extends BaseMapper<DataDict> {
    /**
     * 通过pid查询一级子级列表
     *
     * @param pid
     * @return
     */
    @Select("select id from sys_data_dict where pid = #{pid} ")
    Set<String> findListByPid(@Param("pid") String pid);

    // 字典页面增删改查 使用
    @Select("select id as 'key', id as value, name as label from sys_data_dict where pid = #{pid} order by sort")
    List<Map<String, Object>> findPTreeSelectsByPid(@Param("pid") String pid);
}
