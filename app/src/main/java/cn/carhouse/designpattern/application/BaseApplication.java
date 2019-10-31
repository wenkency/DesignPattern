package cn.carhouse.designpattern.application;

import android.app.Application;

import cn.carhouse.designpattern.db.core.QuickDaoFactory;

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // 数据库初始化
        QuickDaoFactory.getInstance().init(this);
    }
}
