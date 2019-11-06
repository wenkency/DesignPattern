package cn.carhouse.designpattern.skin;

import android.app.Application;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import java.lang.reflect.Method;
import java.util.Observable;

import cn.carhouse.designpattern.db.utils.ThreadUtils;
import cn.carhouse.designpattern.skin.utils.SkinPreference;
import cn.carhouse.designpattern.skin.utils.SkinResources;

/**
 * 皮肤管理类
 */
public class SkinManager extends Observable {
    private static Handler mHandler = new Handler(Looper.getMainLooper());
    private static SkinManager mInstance;
    private Application mApplication;

    public static SkinManager getInstance() {
        if (mInstance == null) {
            synchronized (SkinManager.class) {
                if (mInstance == null) {
                    mInstance = new SkinManager();
                }
            }
        }
        return mInstance;
    }

    public void init(Application application) {
        mApplication = application;
        // 缓存皮肤路径
        SkinPreference.init(application);
        // 初始化皮肤资源管理器
        SkinResources.init(application);
        // 注册Activity监听
        application.registerActivityLifecycleCallbacks(new SkinCallbacks());
        // 加载资源包
        loadSkin(SkinPreference.getInstance().getSkin());
    }

    public void loadSkin(final String skinPath) {
        if (TextUtils.isEmpty(skinPath)) {
            // 皮肤路径置空
            SkinPreference.getInstance().setSkin("");
            // 还原默认
            SkinResources.getInstance().reset();
            // 应用皮肤包
            setChanged();
            // 通知观察者
            notifyObservers();
        } else {
            // 线程去加载皮肤包
            ThreadUtils.getNormalPool().execute(new Runnable() {
                @Override
                public void run() {
                    loadSkinResources(skinPath);
                }
            });
        }
    }

    private void loadSkinResources(String skinPath) {
        try {
            Resources rs = mApplication.getResources();
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPath = AssetManager.class.getDeclaredMethod("addAssetPath", String.class);
            // 加载皮肤的路径
            addAssetPath.invoke(assetManager, skinPath);
            // 皮肤的资源
            Resources resources = new Resources(assetManager, rs.getDisplayMetrics(), rs.getConfiguration());
            // 皮肤资源的包名
            String packName = mApplication.getPackageManager()
                    .getPackageArchiveInfo(skinPath, PackageManager.GET_ACTIVITIES)
                    .packageName;
            SkinResources.getInstance().applySkin(resources, packName);
            // 保存当前使用的皮肤包
            SkinPreference.getInstance().setSkin(skinPath);
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    // 应用皮肤包
                    setChanged();
                    // 通知观察者
                    notifyObservers();
                }
            });
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
