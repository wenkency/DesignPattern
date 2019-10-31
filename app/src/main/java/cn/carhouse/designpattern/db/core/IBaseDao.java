package cn.carhouse.designpattern.db.core;

import android.database.sqlite.SQLiteDatabase;

import java.util.List;

/**
 * 数据库Dao
 */
public interface IBaseDao<T> {
    /**
     * 初始化
     */
    void init(SQLiteDatabase sqLiteDatabase, Class<T> clazz);

    /**
     * 插入对象
     */
    long insert(T bean);

    /**
     * 批量插入
     */
    void insert(List<T> beans);
}
