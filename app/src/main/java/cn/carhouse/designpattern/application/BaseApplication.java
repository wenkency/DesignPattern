package cn.carhouse.designpattern.application;

import android.app.Application;

import cn.carhouse.designpattern.db.core.DaoFactory;

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // 数据库初始化
        DaoFactory.getInstance().init(this);
    }
}
