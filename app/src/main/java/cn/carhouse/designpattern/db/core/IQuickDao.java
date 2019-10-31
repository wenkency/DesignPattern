package cn.carhouse.designpattern.db.core;

import android.database.sqlite.SQLiteDatabase;

import java.util.List;

/**
 * 数据库Dao
 */
public interface IQuickDao<T> {
    /**
     * 初始化
     */
    void init(SQLiteDatabase sqLiteDatabase, Class<T> clazz);

    /**
     * 插入对象
     */
    long insert(T bean);

    /**
     * 批量插入,带完成回调
     */
    void insert(List<T> beans, OnInsertListener onInsertListener);

    /**
     * 批量插入
     */
    void insert(List<T> beans);

    /**
     * 更新
     *
     * @param bean  改成最终结果
     * @param where 限制条件例如: where id=6
     */
    int update(T bean, T where);

    /**
     * 删除
     *
     * @param where 限制条件
     * @return
     */
    int delete(T where);

    /**
     * 查询封装
     */
    QuerySupport<T> query();

    interface OnInsertListener {
        void onInserted();
    }
}
