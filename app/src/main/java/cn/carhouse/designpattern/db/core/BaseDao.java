package cn.carhouse.designpattern.db.core;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.carhouse.designpattern.db.utils.TableUtils;

public class BaseDao<T> implements IBaseDao<T> {
    private boolean isInit = false;
    private SQLiteDatabase mSQLite;
    private Class<T> mEntity;
    // 缓存表结构里面对应的成员字段，目的是怕对象添加新字段，表没有添加
    // key : 列名  value: 字段field对象
    private Map<String, Field> mTableFields;
    private String mTableName;

    public void init(SQLiteDatabase sqLite, Class<T> entity) {
        this.mSQLite = sqLite;
        this.mEntity = entity;
        if (!isInit) {
            if (!sqLite.isOpen()) {
                return;
            }
            mTableName = TableUtils.getTableName(mEntity);
            String sql = TableUtils.getSql(entity);
            sqLite.execSQL(sql);
            mTableFields = new HashMap<>();
            cacheTableField();
            isInit = true;
        }
    }

    /**
     * 缓存表结构里面对应的成员字段，目的是怕对象添加新字段，表没有添加
     */
    private void cacheTableField() {
        // 1.取到所有的列名
        String sql = "select * from " + mTableName + " limit 1,0";// 空表
        Cursor cursor = mSQLite.rawQuery(sql, null);
        String[] columnNames = cursor.getColumnNames();
        // 2.取所有的成员变量
        Field[] fields = mEntity.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            String name = TableUtils.getName(field);
            if (TextUtils.isEmpty(name)) {
                continue;
            }
            for (String columnName : columnNames) {
                if (name.equals(columnName)) {
                    mTableFields.put(name, field);
                    break;
                }
            }
        }

    }

    @Override
    public void insert(List<T> beans) {
        try {
            //开启事务
            mSQLite.beginTransaction();
            for (T bean : beans) {
                insert(bean);
            }
            //告诉数据库事务提交成功
            mSQLite.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //结束事务
            mSQLite.endTransaction();
        }
    }

    @Override
    public long insert(T bean) {
        return mSQLite.insert(mTableName, null, getContentValues(bean));
    }


    private ContentValues getContentValues(T bean) {
        ContentValues values = new ContentValues();
        // 循环数据库里面表结构对应的字段
        for (Map.Entry<String, Field> entry : mTableFields.entrySet()) {
            try {
                String key = entry.getKey();
                Field field = entry.getValue();
                Object obj = field.get(bean);
                if (obj != null) {
                    String value = obj.toString();
                    if (TextUtils.isEmpty(key) || TextUtils.isEmpty(value)) {
                        continue;
                    }
                    values.put(key, value);
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return values;
    }
}
