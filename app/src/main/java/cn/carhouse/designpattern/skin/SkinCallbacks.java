package cn.carhouse.designpattern.skin;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.collection.ArrayMap;
import androidx.core.view.LayoutInflaterCompat;

import java.lang.reflect.Field;
import java.util.Map;

import cn.carhouse.designpattern.skin.utils.LifecycleCallbacks;
import cn.carhouse.designpattern.skin.utils.SkinThemeUtils;

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

            Typeface skinTypeface = SkinThemeUtils.getSkinTypeface(activity);
            SkinFactory skinFactory = new SkinFactory(activity,skinTypeface);
            LayoutInflaterCompat.setFactory2(layoutInflater, skinFactory);
            // 注册皮肤变换监听
            SkinManager.getInstance().addObserver(skinFactory);
            skinFactoryMap.put(activity, skinFactory);
            // 更换状态栏和底部虚拟按键的颜色
            SkinThemeUtils.updateStatusBar(activity);
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
