package cn.lven.aspectj;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * 处理登录切面的类
 */
@Aspect
public class AspectLogin {
    /**
     * 1. 切一个点再来，处理所有注解类CheckLogin的代码
     */
    @Pointcut("execution(@cn.lven.aspectj.CheckLogin * *(..))")
    public void checkLoginBehavior() {

    }

    /**
     * 2. 处理checkLoginBehavior这个方法
     */
    @Around("checkLoginBehavior()")
    public Object checkLogin(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        CheckLogin checkLogin = signature.getMethod().getAnnotation(CheckLogin.class);
        if (checkLogin != null) {
            // TODO 这里做登录的逻辑判断
            // 这个就是拦截，不走方法的代码
            return null;
        }

        return joinPoint.proceed();
    }
}
