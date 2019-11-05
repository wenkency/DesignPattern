package cn.carhouse.designpattern.skin;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.collection.ArrayMap;
import androidx.core.view.LayoutInflaterCompat;

import java.lang.reflect.Field;
import java.util.Map;

import cn.carhouse.designpattern.skin.utils.LifecycleCallbacks;

public class SkinCallbacks extends LifecycleCallbacks {
    private Map<Activity, SkinFactory> skinFactoryMap = new ArrayMap<>();

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        // 参考：AppCompatDelegateImpl --》installViewFactory()
        try {
            LayoutInflater layoutInflater = LayoutInflater.from(activity);
            Field field = LayoutInflater.class.getDeclaredField("mFactorySet");
            field.setAccessible(true);
            field.set(layoutInflater, false);
            SkinFactory skinFactory = new SkinFactory(activity);
            LayoutInflaterCompat.setFactory2(layoutInflater, skinFactory);
            // 注册皮肤变换监听
            SkinManager.getInstance().addObserver(skinFactory);
            skinFactoryMap.put(activity, skinFactory);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        SkinFactory skinFactory = skinFactoryMap.remove(activity);
        if (skinFactory != null) {
            // 取消皮肤变换监听
            SkinManager.getInstance().deleteObserver(skinFactory);
            skinFactory.destroyed();
        }
    }
}
