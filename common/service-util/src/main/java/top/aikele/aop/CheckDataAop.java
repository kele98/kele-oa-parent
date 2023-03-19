package top.aikele.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;

@Component
@Aspect
public class CheckDataAop {
    @Pointcut("@annotation(top.aikele.annotation.CheckData)")
    public void pointCut(){

    }
    @Around("pointCut()")
    public void check(ProceedingJoinPoint jp){
        //获取请求参数

    }
}
