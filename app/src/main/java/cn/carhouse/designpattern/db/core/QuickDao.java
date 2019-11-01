package cn.carhouse.designpattern.db.core;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import cn.carhouse.designpattern.db.utils.QuickDaoUtils;
import cn.carhouse.designpattern.db.utils.ThreadManager;

/**
 * 数据库操作类
 */
public class QuickDao<T> implements IQuickDao<T> {
    private boolean isInit = false;
    private SQLiteDatabase mSQLite;
    private Class<T> mEntity;
    // 缓存表结构里面对应的成员字段，目的是怕对象添加新字段，表没有添加
    // key : 列名  value: 字段field对象
    private Map<String, Field> mTableFields;
    private String mTableName;
    private QuerySupport<T> mQuerySupport;

    public void init(SQLiteDatabase sqLite, Class<T> entity) {
        this.mSQLite = sqLite;
        this.mEntity = entity;
        if (!isInit) {
            if (!sqLite.isOpen()) {
                return;
            }
            mTableName = QuickDaoUtils.getTableName(mEntity);
            String sql = QuickDaoUtils.getSql(entity);
            sqLite.execSQL(sql);
            mTableFields = QuickDaoUtils.getTableFields(mEntity, mSQLite);
            isInit = true;
        }
    }


    @Override
    public void insert(List<T> beans) {
        insert(beans, null);
    }

    @Override
    public void insert(final List<T> beans, final OnInsertListener onInsertListener) {
        ThreadManager.getNormalPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    // 开启事务
                    mSQLite.beginTransaction();
                    for (T bean : beans) {
                        insert(bean);
                    }
                    // 告诉数据库事务提交成功
                    mSQLite.setTransactionSuccessful();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    // 结束事务
                    mSQLite.endTransaction();
                }
                // 完成回调
                if (onInsertListener != null) {
                    onInsertListener.onInserted();
                }
            }
        });
    }

    @Override
    public long insert(T bean) {
        ContentValues values = QuickDaoUtils.getContentValues(bean, mTableFields);
        return mSQLite.insert(mTableName, null, values);
    }

    @Override
    public int update(T bean, T where) {
        ContentValues values = QuickDaoUtils.getContentValues(bean, mTableFields);
        QuickDaoParams params = new QuickDaoParams(QuickDaoUtils.getParams(where, mTableFields));
        return mSQLite.update(mTableName, values, params.whereClause, params.whereArgs);
    }

    @Override
    public int update(T bean) {
        ContentValues values = QuickDaoUtils.getContentValues(bean, mTableFields);
        return mSQLite.update(mTableName, values, null, null);
    }

    @Override
    public int delete(T where) {
        QuickDaoParams params = new QuickDaoParams(QuickDaoUtils.getParams(where, mTableFields));
        return mSQLite.delete(mTableName, params.whereClause, params.whereArgs);
    }

    @Override
    public QuerySupport<T> query() {
        if (mQuerySupport == null) {
            mQuerySupport = new QuerySupport<>(mSQLite, mEntity, mTableFields);
        }
        return mQuerySupport;
    }

    @Override
    public void execSQL(String sql) {
        mSQLite.execSQL(sql);
    }

    public String getTableName() {
        return mTableName;
    }
}
