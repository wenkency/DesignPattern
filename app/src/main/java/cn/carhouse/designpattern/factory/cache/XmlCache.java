package cn.carhouse.designpattern.factory.cache;

import android.content.Context;
import android.content.SharedPreferences;


public class XmlCache implements Cache {
    public static final String CACHE = "cache";
    private SharedPreferences mPreferences;

    @Override
    public void init(Context context) {
        if (context != null) {
            new RuntimeException("XmlCache Context is null");
        }
        mPreferences = context.getSharedPreferences(CACHE, Context.MODE_PRIVATE);
    }

    @Override
    public void putString(String key, String value) {
        if (mPreferences == null) {
            return;
        }
        mPreferences.edit()
                .putString(key, value)
                .commit();
    }

    @Override
    public String getString(String key, String defaultValue) {
        return mPreferences.getString(key, defaultValue);
    }
}
