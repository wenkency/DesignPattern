package cn.carhouse.designpattern.db.core;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;

import cn.carhouse.designpattern.db.utils.QuickDaoUtils;

/**
 * 数据库查询
 */
public class QuickDaoFactory {
    // 应用数据库的名称
    public static final String DB_NAME = "acxwb.db";
    private static QuickDaoFactory mInstance;
    private SQLiteDatabase mSqLiteDatabase;

    private QuickDaoFactory() {
    }

    public static QuickDaoFactory getInstance() {
        if (mInstance == null) {
            synchronized (QuickDaoFactory.class) {
                if (mInstance == null) {
                    mInstance = new QuickDaoFactory();
                }
            }
        }
        return mInstance;
    }

    /**
     * 初始化方法:主要是创建SQLiteDatabase
     */
    public void init(Context context) {
        File dbFile = QuickDaoUtils.getFile(context, DB_NAME);
        if (!dbFile.exists()) {
            mSqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        } else {
            mSqLiteDatabase = SQLiteDatabase.openDatabase(dbFile.getAbsolutePath(), null, SQLiteDatabase.OPEN_READWRITE);
        }
    }

    /**
     * 获取数据库操作对象
     */
    public <T extends IQuickDao<M>, M> T getBaseDao(Class<T> daoClazz, Class<M> clazz) {
        if (mSqLiteDatabase == null) {
            throw new RuntimeException("QuickDaoFactory is not init ");
        }
        T dao = null;
        try {
            dao = daoClazz.newInstance();
            dao.init(mSqLiteDatabase, clazz);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return dao;
    }

    public <M> IQuickDao<M> getBaseDao(Class<M> clazz) {
        return getBaseDao(QuickDao.class, clazz);
    }
}
