package com.qinyou.apiserver.core.base;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


/**
 * 自定义 runtime 异常
 */
@Getter
@Setter
@Builder
public class RequestException extends RuntimeException implements Serializable {
    private Integer code; // 编码
    private String msg;   // 信息
    private Exception e;  // 异常

    public static RequestException fail(ResultEnum resultEnum) {
        return RequestException.builder()
                .code(resultEnum.getCode())
                .msg(resultEnum.getMsg())
                .build();
    }

    public static RequestException fail(ResultEnum resultEnum, Object[] msgArgs) {
        return RequestException.builder()
                .code(resultEnum.getCode())
                .msg(resultEnum.getMsg(msgArgs))
                .build();
    }

    public static RequestException fail(ResultEnum resultEnum, Exception e) {
        return RequestException.builder()
                .code(resultEnum.getCode()).msg(resultEnum.getMsg())
                .e(e).build();
    }
}
