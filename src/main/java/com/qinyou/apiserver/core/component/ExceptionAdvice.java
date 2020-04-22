package com.qinyou.apiserver.core.component;

import com.qinyou.apiserver.core.base.RequestException;
import com.qinyou.apiserver.core.base.Result;
import com.qinyou.apiserver.core.base.ResultEnum;
import com.qinyou.apiserver.core.utils.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.List;

/**
 * 全局异常 处理
 *
 * @author chuang
 */
@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {

    // 404 处理
    @ExceptionHandler(NoHandlerFoundException.class)
    public Result handleResourceNotFoundException(NoHandlerFoundException e) {
        return Result.builder()
                .status(false)
                .code(404)
                .msg("404 - 资源不存在")
                .build();
    }

    @ExceptionHandler(RequestException.class)
    @ResponseBody
    public Result requestExceptionHandler(RequestException e) {
        if (e.getE() != null) log.error(e.getMessage(), e.getE());
        return Result.builder()
                .status(false)
                .code(e.getCode())
                .msg(e.getMsg())
                .build();
    }

    // 表单参数校验失败
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public Result methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        String s = ResultEnum.BAD_PARAM.getMsg();
        if (result.hasErrors()) {
            List<ObjectError> errors = result.getAllErrors();
            s = errors.get(0).getDefaultMessage();
        }
        return Result.builder()
                .status(false)
                .code(ResultEnum.BAD_PARAM.getCode())
                .msg(s)
                .build();
    }

    //spring security 账号找不到
    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseBody
    public Result usernameNotFoundException(UsernameNotFoundException exception) {
        return WebUtils.fail(ResultEnum.ACCOUNT_NOT_EXIST);
    }

    // spring security 密码错误
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseBody
    public Result badCredentialsExceptionHandler(BadCredentialsException exception) {
        return WebUtils.fail(ResultEnum.BAD_PASSWORD);
    }

    // spring security 账号被锁定
    @ExceptionHandler(LockedException.class)
    @ResponseBody
    public Result lockedException(LockedException exception) {
        return WebUtils.fail(ResultEnum.ACCOUNT_LOCKED);
    }

    //spring security 无权限
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    public Result accessDeniedException(AccessDeniedException exception) {
        return WebUtils.fail(ResultEnum.ACCESS_DENIED);
    }

    // sql 操作异常
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseBody
    public Result dataIntegrityViolationExceptionHandler(DataIntegrityViolationException e) {
        log.info("--- sql 异常 ---");
        log.error(e.getMessage(), e);
        return WebUtils.fail(ResultEnum.EXCEPTION);
    }

    // sql 异常
    @ExceptionHandler(BadSqlGrammarException.class)
    @ResponseBody
    public Result badSqlGrammarExceptionHandler(BadSqlGrammarException e) {
        log.info("--- sql 异常 ---");
        log.error(e.getMessage(), e);
        return WebUtils.fail(ResultEnum.EXCEPTION);
    }


    // 根异常信息
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result exceptionHandler(Exception e) {
        log.error(e.getMessage(), e);
        return WebUtils.fail(ResultEnum.EXCEPTION, e.getMessage());
    }
}
