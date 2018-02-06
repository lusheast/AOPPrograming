package mjoys.com.aopprograming;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.Toast;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * Created by zsd on 2018/2/5 11:02
 * desc:处理切点的一个类
 */

@Aspect
public class AspectUtils {

    /**
     * 找到处理的切点
     * * *(..)  表示可以处理所有的方法
     */
    @Pointcut("execution(@mjoys.com.aopprograming.CheckNet * *(..))")
    public void getCheckNetAspect() {

    }


    /**
     * 处理切面
     *
     * @return
     */
    @Around("getCheckNetAspect()")
    public Object checkNet(ProceedingJoinPoint joinPoint) throws Throwable {
        // 1.获取 CheckNet 注解
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        CheckNet checkNet = signature.getMethod().getAnnotation(CheckNet.class);
        if (checkNet != null) {
            //2.获取到注解后，再去检查有没有网络
            //joinPoint.getThis()是当前切点方法所在的类
            Object object = joinPoint.getThis();
            Context context = getContext(object);
            if (context != null) {
                if (!isNetworkAvailable(context)) {
                    //没有网不执行后面的任务
                    Toast.makeText(context, "请检查您的网络", Toast.LENGTH_LONG).show();
                    return null;
                }
            }
        }
        return joinPoint.proceed();
    }

    /**
     * 通过对象获取上下文
     *
     * @param object
     * @return
     */
    private Context getContext(Object object) {
        if (object instanceof Activity) {
            return (Activity) object;
        } else if (object instanceof Fragment) {
            Fragment fragment = (Fragment) object;
            return fragment.getActivity();
        } else if (object instanceof View) {
            View view = (View) object;
            return view.getContext();
        }
        return null;
    }


    /**
     * 检查当前网络是否可用
     *
     * @return
     */
    public boolean isNetworkAvailable(Context context) {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
