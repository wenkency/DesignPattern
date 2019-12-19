package cn.carhouse.designpattern.application;

import android.app.Application;

import cn.carhouse.designpattern.db.core.QuickDaoFactory;
import cn.carhouse.designpattern.skin.SkinManager;

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // 数据库初始化
        QuickDaoFactory.getInstance().initPublicSQLite(this);
        // 皮肤
        SkinManager.getInstance().init(this);
    }
}
