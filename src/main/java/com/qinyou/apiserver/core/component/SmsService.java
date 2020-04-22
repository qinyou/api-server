package com.qinyou.apiserver.core.component;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.digest.MD5;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 短信验证码工具
 *
 * @author chuang
 */
@SuppressWarnings("FieldCanBeLocal")
@Slf4j
@Component
public class SmsService {

    // 容联云 配置
    private final String REST_URL = "https://app.cloopen.com:8883";
    private final String ACCOUNT_SID = "8aaf070858862df301588b17dd150386";
    private final String AUTH_TOKEN = "6afe10acbaf74221be528721b879494d";
    private final String APP_ID = "8aaf070858862df301588b17de7a038d";
    // 验证码过期时间，单位分钟
    @Value("${app.safe-code.max-duration}")
    private String MAX_DURATION = "10";

    public boolean send(String phones, String code) {
        boolean flag = false;
        try {
            String content = HttpUtil.createPost(uri()).addHeaders(headers()).body(body(phones, code)).execute().body();
            JSON json = JSONUtil.parse(content);
            if ("000000".equals(json.getByPath("statusCode"))) {
                flag = true;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return flag;
    }


    private Map<String, String> headers() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json;charset=utf-8");
        headers.put("Content-Length", "256");
        String authorization = ACCOUNT_SID + ":" + DateUtil.format(new Date(), "yyyyMMddHHmmss");
        authorization = Base64.encode(authorization, "UTF-8");
        headers.put("Authorization", authorization);
        System.out.println(headers);
        return headers;
    }

    private String uri() {
        StringBuilder builder = new StringBuilder();
        String sig = builder.append(ACCOUNT_SID).append(AUTH_TOKEN).append(DateUtil.format(new Date(), "yyyyMMddHHmmss")).toString();
        sig = MD5.create().digestHex(sig, "UTF-8").toUpperCase();
        builder = new StringBuilder();
        return builder.append(REST_URL).append("/2013-12-26/Accounts/").append(ACCOUNT_SID)
                .append("/SMS/TemplateSMS?sig=").append(sig).toString();
    }

    private String body(String phones, String code) {
        Map<String, Object> headers = new LinkedHashMap<>();
        headers.put("to", phones);
        headers.put("appId", APP_ID);
        headers.put("templateId", "1");
        headers.put("datas", new String[]{code, MAX_DURATION});
        return JSONUtil.toJsonStr(headers);
    }
}
