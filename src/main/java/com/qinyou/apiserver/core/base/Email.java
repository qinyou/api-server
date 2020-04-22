package com.qinyou.apiserver.core.base;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 邮箱检验
 * 如果为null 或 符合邮箱格式 能正常校验通过
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {EmailValidator.class})
public @interface Email {
    boolean required() default true;

    String message() default "Email pattern error";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
