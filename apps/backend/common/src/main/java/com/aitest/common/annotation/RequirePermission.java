package com.aitest.common.annotation;

import java.lang.annotation.*;

/**
 * 权限校验注解
 * 用于标注需要特定权限才能访问的方法
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequirePermission {

    /**
     * 权限码，支持多个权限（满足其一即可）
     * 格式示例：system:user:view, system:user:edit
     */
    String[] value() default {};

    /**
     * 权限码（别名，与value相同）
     */
    String[] code() default {};

    /**
     * 是否需要全部权限（默认false，满足其一即可）
     * true: 需要满足所有权限
     * false: 满足任意一个权限即可
     */
    boolean requireAll() default false;
}
