package com.example.multi.app.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//@Retention是一个元注解，用于指定注解的保留策略。
//RetentionPolicy.RUNTIME:
//表示这个注解在运行时仍然会被保留。这意味着在程序运行时，可以通过反射机制来获取这个注解的信息。
//例如，框架可以通过反射检查方法是否被
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
//ElementType.METHOD：表示这个注解可以被应用到方法上。
//ElementType.PARAMETER：表示这个注解可以被应用到方法的参数上。
public @interface RequireLogin {

}