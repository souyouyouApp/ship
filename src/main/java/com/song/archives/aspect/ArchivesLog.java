package com.song.archives.aspect;


import java.lang.annotation.*;

@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ArchivesLog {


    /** 要的操作类型比如：add **/
    String operationType() default "";

    /** 要的具体比如：添加用户 **/
    String description() default "";

    /** 是否记录 **/
    boolean writeFlag() default true;

    /** 记录切面描述信息 **/
    boolean descFlag() default false;


}