package com.qinyou.apiserver.core.utils;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 日期工具
 *
 * @author chuang
 */
@Slf4j
public class DateUtils {
    private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // 格式化日期为字符串
    public static String formatDateTime(LocalDateTime localDateTime) {
        String date = localDateTime.format(DEFAULT_FORMATTER);
        log.debug("date: {}", date);
        return date;
    }

    // 将字符串解析为日期
    public static LocalDateTime parseLocalDateTime(String str) {
        return LocalDateTime.parse(str, DEFAULT_FORMATTER);
    }
}
