package com.qinyou.apiserver.core.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qinyou.apiserver.core.base.*;
import com.qinyou.apiserver.core.component.SpringContext;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 通用工具类
 *
 * @author chuang
 */
@SuppressWarnings("Duplicates")
@Slf4j
public class WebUtils {

    // 对象 转 json 字符串,使用内置 配置好的 objectMapper
    public static String toJSONString(Object o) {
        ObjectMapper objectMapper = SpringContext.getBean(ObjectMapper.class);
        String jsonString;
        try {
            jsonString = objectMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            jsonString = "";
            log.error(e.getMessage(), e);
        }
        return jsonString;
    }

    // 通过 response 直接响应json数据
    public static <T> void outPrintJSON(HttpServletResponse response, Result<T> result) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        String str = toJSONString(result);
        PrintWriter out;
        try {
            out = response.getWriter();
            out.print(str);
            out.flush();
            out.close();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    // 过 freemarker 字符串渲染文本
    public static String processTpl(String template, Map<String, ?> model) throws IOException, TemplateException {
        if (template == null) {
            return null;
        }
        StringWriter out = new StringWriter();
        new Template("template", new StringReader(template),
                new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS))
                .process(model, out);
        return out.toString();
    }

    // 获取客户端IP
    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Real-IP");
        if (ip == null || "".equals(ip.trim()) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknow".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if ("0.0.0.0".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip) || "localhost".equals(ip) || "127.0.0.1".equals(ip)) {
            ip = "127.0.0.1";
        }
        return ip;
    }

    // 获得当前认证的用户名
    public static String getSecurityUsername() {
        String username = "游客";
        if (SecurityContextHolder.getContext() != null && SecurityContextHolder.getContext().getAuthentication() != null) {
            if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof JwtUser) {
                JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                username = jwtUser.getUsername();
            }
            // 否则是  anonymousUser
        }
        return username;
    }

    // 构造分页查询参数
    public static <T> Page<T> buildSearchPage(Query query) {
        return new Page<T>().setCurrent(query.getCurrent()).setSize(query.getPageSize());
    }

    // 构建查询参数
    public static <T> QueryWrapper<T> buildSearchQueryWrapper(Query query) {
        Map<String, String> filter = query.getFilter();
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        String key, field, sqlKey;
        // 查询参数
        if (filter != null) {
            for (Map.Entry<String, String> entry : filter.entrySet()) {
                key = entry.getKey();
                if (!key.startsWith("search_")) {
                    continue;
                }
//              eg: "search_EQ_action_name"
                key = key.replaceAll("search_", "");
                sqlKey = key.substring(0, key.indexOf("_"));
                field = key.substring(key.indexOf("_") + 1);
                switch (sqlKey) {
                    case "EQ":
                        queryWrapper.eq(field, entry.getValue());
                        break;
                    case "LIKE":
                        queryWrapper.like(field, entry.getValue());
                        break;
                    case "GT":
                        queryWrapper.gt(field, entry.getValue());
                        break;
                    case "LT":
                        queryWrapper.lt(field, entry.getValue());
                        break;
                    case "GTE":
                        queryWrapper.ge(field, entry.getValue());
                        break;
                    case "LTE":
                        queryWrapper.le(field, entry.getValue());
                        break;
                }
            }
        }
        // 排序参数
        List<Order> orders = query.getOrders();
        if (orders != null) {
            for (Order order : orders) {
                switch (order.getSort().toUpperCase()) {
                    case "ASC":
                        queryWrapper.orderByAsc(order.getColumn());
                        break;
                    case "DESC":
                        queryWrapper.orderByDesc(order.getColumn());
                        break;
                    default:
                        queryWrapper.orderByAsc(order.getColumn());
                        break;
                }
            }
        }
        return queryWrapper;
    }

    // 查询结果分页封装
    public static <T> PageResult<T> buildPageResult(IPage<T> page) {
        return new PageResult<>(page.getCurrent(), page.getSize(), page.getTotal(), page.getPages(), page.getRecords());
    }

    // 成功消息
    public static Result ok() {
        return Result.builder()
                .status(true)
                .code(ResultEnum.SUCCESS.getCode())
                .msg(ResultEnum.SUCCESS.getMsg())
                .build();
    }

    // 成功消息
    public static Result ok(ResultEnum resultEnum) {
        return Result.builder()
                .status(true)
                .code(resultEnum.getCode())
                .msg(resultEnum.getMsg())
                .build();
    }

    // 成功消息响应
    public static Result ok(ResultEnum resultEnum, Object[] args) {
        return Result.builder()
                .status(true)
                .code(resultEnum.getCode())
                .msg(resultEnum.getMsg(args))
                .build();
    }

    // 成功数据响应
    public static <T> Result<T> ok(T data) {
        return Result.<T>builder()
                .status(true)
                .data(data)
                .build();
    }

    // 成功消息数据 响应
    public static <T> Result<T> ok(ResultEnum resultEnum, T data) {
        return Result.<T>builder()
                .status(true)
                .code(resultEnum.getCode())
                .msg(resultEnum.getMsg())
                .data(data)
                .build();
    }

    // 失败 消息 响应
    public static Result fail(ResultEnum resultEnum) {
        return Result.builder()
                .status(false)
                .code(resultEnum.getCode())
                .msg(resultEnum.getMsg())
                .build();
    }

    // 失败消息响应，消息带参数
    public static Result fail(ResultEnum resultEnum, Object[] args) {
        return Result.builder()
                .status(false)
                .code(resultEnum.getCode())
                .msg(resultEnum.getMsg(args))
                .build();
    }

    // 失败消息数据响应
    public static <T> Result<T> fail(ResultEnum resultEnum, T data) {
        return Result.<T>builder()
                .status(false)
                .code(resultEnum.getCode())
                .msg(resultEnum.getMsg())
                .data(data)
                .build();
    }


    /**
     * 树形数据处理
     *
     * @param allNodes          完整、没有层级的数据列表
     * @param idFieldName       id字段名
     * @param pidFieldName      pid字段名
     * @param childrenFieldName children字段名
     * @param fieldNames        额外的字段名
     * @return 树形结构
     */
    @SuppressWarnings("unchecked")
    public static List<Map<String, Object>> treeData(List<Map<String, Object>> allNodes, String idFieldName, String pidFieldName,
                                                     String childrenFieldName, String[] fieldNames) {
        // 挑出根节点
        List<Map<String, Object>> rootNodes = new LinkedList<>();
        allNodes.stream()
                .filter(map -> !parentNodeExist(map, idFieldName, pidFieldName, allNodes))
                .forEach(map -> rootNodes.add(pickKeyValue(map, idFieldName, pidFieldName, fieldNames)));
        // 使用队列、对象传递构造树形结构
        LinkedList<Map<String, Object>> queue = new LinkedList<>(rootNodes);
        while (queue.size() != 0) {
            Map<String, Object> headItem = queue.pop();
            allNodes.forEach(item -> {
                if (item.get(pidFieldName).equals(headItem.get(idFieldName))) {
                    Map<String, Object> childNode = pickKeyValue(item, idFieldName, pidFieldName, fieldNames);
                    List<Map<String, Object>> children = (List<Map<String, Object>>) headItem.get(childrenFieldName);
                    if (children == null) {
                        children = new LinkedList<>();
                        headItem.put(childrenFieldName, children);
                    }
                    children.add(childNode);
                    queue.push(childNode);
                }
            });
        }
        return rootNodes;
    }


    /**
     * 当前节点是否为 根节点 (通过判断当前节点 父节点是否存在)
     * @param node         当前节点
     * @param idFieldName  id字段名
     * @param pidFieldName pid字段名
     * @param allNodes     完整全部节点
     * @return true 非根节点、false 根节点
     */
    private static boolean parentNodeExist(Map<String, Object> node, String idFieldName, String pidFieldName, List<Map<String, Object>> allNodes) {
        boolean flag = false;
        for (Map<String, Object> item : allNodes) {
            if (node.get(pidFieldName).equals(item.get(idFieldName))) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    private static Map<String, Object> pickKeyValue(Map<String, Object> node, String idFieldName, String pidFieldName, String[] fieldNames) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put(idFieldName, node.get(idFieldName));
        map.put(pidFieldName, node.get(pidFieldName));
        for (String fieldName : fieldNames) {
            map.put(fieldName, node.get(fieldName));
        }
        return map;
    }
}
