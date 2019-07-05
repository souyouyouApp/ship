package com.song.archives.aspect;


import java.lang.annotation.*;

@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ArchivesLog {


    /** 要的操作类型比如：add **/
    public String operationType() default "";  

    /** 要的具体比如：添加用户 **/
    public String operationName() default "";

}