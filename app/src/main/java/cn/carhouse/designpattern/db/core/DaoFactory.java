package cn.carhouse.designpattern.db.core;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;

import cn.carhouse.designpattern.db.utils.TableUtils;

public class DaoFactory {
    // 应用数据库的名称
    public static final String DB_NAME = "acxwb.db";
    private static DaoFactory mInstance;
    private SQLiteDatabase mSqLiteDatabase;

    private DaoFactory() {
    }

    public static DaoFactory getInstance() {
        if (mInstance == null) {
            synchronized (DaoFactory.class) {
                if (mInstance == null) {
                    mInstance = new DaoFactory();
                }
            }
        }
        return mInstance;
    }

    /**
     * 初始化方法:主要是创建SQLiteDatabase
     */
    public void init(Context context) {
        File dbFile = TableUtils.getFile(context, DB_NAME);
        if (!dbFile.exists()) {
            mSqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        } else {
            mSqLiteDatabase = SQLiteDatabase.openDatabase(dbFile.getAbsolutePath(), null, SQLiteDatabase.OPEN_READWRITE);
        }
    }

    /**
     * 获取数据库操作对象
     */
    public <T> IBaseDao<T> getBaseDao(Class<T> clazz) {
        IBaseDao<T> baseDao = new BaseDao<>();
        baseDao.init(mSqLiteDatabase, clazz);
        return baseDao;
    }


}
