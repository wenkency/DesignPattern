package cn.carhouse.designpattern.factory.cache;

import android.content.Context;

public class CacheUtils {
    private static Cache cache;

    public static void init(Context context) {
        cache = CacheFactory.getXmlCache();
        cache.init(context);
    }

    public static void putString(String key, String value) {
        cache.putString(key, value);
    }

    public static String getString(String key, String defaultValue) {
        return cache.getString(key, defaultValue);
    }

}
