package com.tea.server.core.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class CommPointCut {

    /**
     * 请求接口切面
     */
    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)"+
            "|| @annotation(org.springframework.web.bind.annotation.DeleteMapping)"+
            "|| @annotation(org.springframework.web.bind.annotation.GetMapping)"+
            "|| @annotation(org.springframework.web.bind.annotation.PutMapping)"+
            "|| @annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void rest(){}

}
