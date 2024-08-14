package com.givemetreat.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD}) //자바의 Method타입에만 쓰겠다는 뜻
@Retention(RetentionPolicy.RUNTIME)
public @interface TimeTraceStop {

}
