package cn.lven.aspectj;

import android.app.Fragment;
import android.content.Context;
import android.widget.Toast;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * 处理登录切面的类
 */
@Aspect
public class AspectNet {
    private static final String EXECUTION = "execution(@cn.lven.aspectj.CheckNet * *(..))";

    /**
     * 1. 切一个点再来，处理所有注解类CheckNet的代码
     */
    @Pointcut(EXECUTION + " && @annotation(checkNet)")
    public void checkNetBehavior(CheckNet checkNet) {

    }

    /**
     * 2. 处理checkNetBehavior这个方法
     */
    @Around("checkNetBehavior(checkNet)")
    public void checkNet(ProceedingJoinPoint joinPoint, CheckNet checkNet) throws Throwable {

        if (checkNet != null) {
            // 这个就是拦截，不走方法的代码
            Context context = null;
            final Object object = joinPoint.getThis();
            if (object instanceof Context) {
                context = (Context) object;
            } else if (object instanceof Fragment) {
                context = ((Fragment) object).getActivity();
            } else if (object instanceof androidx.fragment.app.Fragment) {
                context = ((androidx.fragment.app.Fragment) object).getActivity();
            }
            Toast.makeText(context, "checkNet", Toast.LENGTH_SHORT).show();

            return;
        }
        // 执行注解上的方法
        joinPoint.proceed();
    }
}
