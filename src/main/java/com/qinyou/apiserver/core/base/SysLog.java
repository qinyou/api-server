package com.qinyou.apiserver.core.base;

import java.lang.annotation.*;

/**
 * 记录系统日志注解
 *
 * @author chuang
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLog {
    String value() default "";          // 日志内容

    boolean logParams() default true;   // 是否记录参数

    boolean logResult() default true;   // 是否记录返回结果

    String type() default "";           // 日志类型, 自定义扩展。 0 用户可见日志
}
