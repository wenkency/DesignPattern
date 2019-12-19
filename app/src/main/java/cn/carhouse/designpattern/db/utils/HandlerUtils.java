package cn.carhouse.designpattern.db.utils;

import android.os.Handler;
import android.os.Looper;

public class HandlerUtils {
    private static Handler mHandler = new Handler(Looper.getMainLooper());

    public static Handler getHandler() {
        return mHandler;
    }
}
