package cn.carhouse.designpattern.db.core;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import cn.carhouse.designpattern.db.subdb.PrivateDBEnums;
import cn.carhouse.designpattern.db.utils.QuickDaoUtils;

/**
 * 数据库查询
 */
public class QuickDaoFactory {
    // 应用数据库的名称
    public static final String DB_NAME = "acxwb.db";
    private static QuickDaoFactory mInstance;
    // 公共库操作
    private SQLiteDatabase mSqLiteDatabase;
    protected Context mContext;

    // 定义一个用于实现分库的数据库操作对象
    protected SQLiteDatabase mSubSQLiteDatabase;

    // 设计一个数据库连接池
    protected Map<String, IQuickDao> mCacheMap = Collections.synchronizedMap(new HashMap<String, IQuickDao>());

    protected QuickDaoFactory() {
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
        this.mContext = context;
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
    public <T extends IQuickDao<M>, M> T getQuickDao(String key, Class<T> daoClazz, Class<M> clazz, SQLiteDatabase sqLiteDatabase) {
        if (sqLiteDatabase == null) {
            throw new RuntimeException("SQLiteDatabase is null ");
        }
        // 从缓存取
        IQuickDao quickDao = mCacheMap.get(key);
        if (quickDao != null) {
            return (T) quickDao;
        }
        T dao = null;
        try {
            dao = daoClazz.newInstance();
            dao.init(sqLiteDatabase, clazz);
            // 缓存
            mCacheMap.put(key, dao);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return dao;
    }

    /**
     * 获取数据库操作对象
     */
    public <T extends IQuickDao<M>, M> T getQuickDao(Class<T> daoClazz, Class<M> clazz) {
        return getQuickDao(daoClazz.getSimpleName(), daoClazz, clazz, mSqLiteDatabase);
    }

    /**
     * 获取数据库操作对象
     */
    public <M> IQuickDao<M> getQuickDao(Class<M> clazz) {
        return getQuickDao(QuickDao.class, clazz);
    }

    /**
     * 获取分库操作对象
     */
    public <T extends IQuickDao<M>, M> T getSubQuickDao(Class<T> daoClazz, Class<M> clazz) {
        // 获取登录用户的信息
        String name = PrivateDBEnums.path.getValue();
        if (mCacheMap.get(name) != null) {
            return (T) mCacheMap.get(name);
        }
        initSQLite(name);
        return getQuickDao(name, daoClazz, clazz, mSubSQLiteDatabase);
    }

    /**
     * 获取分库操作对象
     */
    public <M> IQuickDao<M> getSubQuickDao(Class<M> clazz) {
        return getSubQuickDao(QuickDao.class, clazz);
    }

    private void initSQLite(String name) {
        File dbFile = QuickDaoUtils.getFile(mContext, name);
        if (!dbFile.exists()) {
            mSubSQLiteDatabase = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        } else {
            mSubSQLiteDatabase = SQLiteDatabase.openDatabase(dbFile.getAbsolutePath(), null, SQLiteDatabase.OPEN_READWRITE);
        }
    }
}
