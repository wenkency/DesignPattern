package cn.carhouse.designpattern.factory.cache;

import android.content.Context;

/**
 * 缓存抽象类
 */
public interface Cache {
    void init(Context context);

    void putString(String key, String value);

    String getString(String key, String defaultValue);
}
