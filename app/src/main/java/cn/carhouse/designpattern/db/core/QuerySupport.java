package cn.carhouse.designpattern.db.core;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.carhouse.designpattern.db.utils.QuickDaoUtils;
import cn.carhouse.designpattern.db.utils.ThreadUtils;

/**
 * 查询封装对象
 */
public class QuerySupport<T> {
    private static Handler mHandler = new Handler(Looper.getMainLooper());
    // 查询的列
    private String[] mQueryColumns;
    // 查询的条件
    private String mQuerySelection;
    // 查询的参数
    private String[] mQuerySelectionArgs;
    // 查询分组
    private String mQueryGroupBy;
    // 查询对结果集进行过滤
    private String mQueryHaving;
    // 查询排序
    private String mQueryOrderBy;
    // 查询可用于分页
    private String mQueryLimit;

    private Class<T> mClass;
    private SQLiteDatabase mSQLiteDatabase;
    private Map<String, Field> mTableFields;
    private final String mTableName;
    private OnAsyncQueryListener<T> onAsynQueryListener;


    public QuerySupport(SQLiteDatabase sqLiteDatabase, Class<T> clazz, Map<String, Field> tableFields) {
        this.mClass = clazz;
        this.mSQLiteDatabase = sqLiteDatabase;
        this.mTableFields = tableFields;
        mTableName = QuickDaoUtils.getTableName(mClass);
    }

    public QuerySupport columns(String... columns) {
        this.mQueryColumns = columns;
        return this;
    }

    public QuerySupport selectionArgs(String... selectionArgs) {
        this.mQuerySelectionArgs = selectionArgs;
        return this;
    }

    public QuerySupport having(String having) {
        this.mQueryHaving = having;
        return this;
    }

    public QuerySupport orderBy(String orderBy) {
        this.mQueryOrderBy = orderBy;
        return this;
    }

    public QuerySupport limit(String limit) {
        this.mQueryLimit = limit;
        return this;
    }

    public QuerySupport groupBy(String groupBy) {
        this.mQueryGroupBy = groupBy;
        return this;
    }

    public QuerySupport selection(String selection) {
        this.mQuerySelection = selection;
        return this;
    }

    /**
     * 所有查询最终执行的方法
     */
    public List<T> query() {
        Cursor cursor = mSQLiteDatabase.query(mTableName, mQueryColumns, mQuerySelection,
                mQuerySelectionArgs, mQueryGroupBy, mQueryHaving, mQueryOrderBy, mQueryLimit);
        clearQueryParams();
        return cursorToList(cursor);
    }

    /**
     * 异步查询
     */
    public void asyncQuery() {
        if (onAsynQueryListener == null) {
            return;
        }
        ThreadUtils.getNormalPool().execute(new Runnable() {
            @Override
            public void run() {
                // 调的还是查询方法
                asyncQueryCallback(query());
            }
        });
    }

    public void asyncQuery(OnAsyncQueryListener<T> listener) {
        this.onAsynQueryListener = listener;
        asyncQuery();
    }

    private void asyncQueryCallback(final List<T> items) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (onAsynQueryListener != null) {
                    onAsynQueryListener.onAsyncQueried(items);
                }
            }
        });
    }


    public List<T> query(T where) {
        return query(where, null, null, null);
    }

    public List<T> query(T where, String orderBy, String startIndex, String limit) {
        QuickDaoParams params = new QuickDaoParams(QuickDaoUtils.getParams(where, mTableFields));
        String queryLimit = null;
        if (!TextUtils.isEmpty(startIndex) && !TextUtils.isEmpty(limit)) {
            // '1 , 5'
            queryLimit = startIndex + " , " + limit;
        }
        mQuerySelection = params.whereClause;
        mQuerySelectionArgs = params.whereArgs;
        mQueryLimit = queryLimit;
        mQueryOrderBy = orderBy;
        return query();
    }


    public List<T> queryAll() {
        clearQueryParams();
        return query();
    }

    /**
     * 清空参数
     */
    private void clearQueryParams() {
        mQueryColumns = null;
        mQuerySelection = null;
        mQuerySelectionArgs = null;
        mQueryGroupBy = null;
        mQueryHaving = null;
        mQueryOrderBy = null;
        mQueryLimit = null;
    }

    /**
     * 通过Cursor封装成查找对象
     *
     * @return 对象集合列表
     */
    private List<T> cursorToList(Cursor cursor) {
        List<T> items = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                try {
                    // 1. 创建一个对象
                    T item = mClass.newInstance();
                    // 2. 给每个字段赋值
                    for (Map.Entry<String, Field> entry : mTableFields.entrySet()) {
                        Field field = entry.getValue();
                        Class<?> type = field.getType();
                        // 根据列名去获取位置
                        int columnIndex = cursor.getColumnIndex(entry.getKey());
                        if (columnIndex == -1) {
                            continue;
                        }
                        // 字段类型
                        if (type == String.class) {
                            field.set(item, cursor.getString(columnIndex));
                        } else if (type == Integer.class || type == int.class) {
                            field.set(item, cursor.getInt(columnIndex));
                        } else if (type == Long.class || type == long.class) {
                            field.set(item, cursor.getLong(columnIndex));
                        } else if (type == Double.class || type == double.class) {
                            field.set(item, cursor.getDouble(columnIndex));
                        } else if (type == byte[].class) {
                            field.set(item, cursor.getBlob(columnIndex));
                        } else if (type == Boolean.class || type == boolean.class) {
                            int value = cursor.getInt(columnIndex);
                            if (0 == value) {
                                field.set(item, false);
                            } else {
                                field.set(item, true);
                            }
                        }
                    }
                    items.add(item);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            cursor.close();
        }
        return items;
    }

    /**
     * 异步查询回调监听
     */
    public interface OnAsyncQueryListener<T> {
        void onAsyncQueried(List<T> items);
    }

    public void setOnAsynQueryListener(OnAsyncQueryListener<T> onAsynQueryListener) {
        this.onAsynQueryListener = onAsynQueryListener;
    }
}
