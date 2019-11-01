package cn.carhouse.designpattern.db.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import cn.carhouse.designpattern.db.annotation.DbField;
import cn.carhouse.designpattern.db.annotation.DbTable;

public class QuickDaoUtils {
    /**
     * 建表的Sql语句
     */
    public static String getSql(Class entity) {
        // 创建表
        String tableName = getTableName(entity);
        StringBuffer sb = new StringBuffer("create table if not exists " + tableName + "( id integer primary key autoincrement, ");
        Field[] fields = entity.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            // 字段对应的表类型
            String type = getType(field.getType());
            // 字段名称
            String name = getName(field);
            if (TextUtils.isEmpty(type) || TextUtils.isEmpty(name)) {
                continue;
            }
            sb.append(name + type);
        }
        // 删除一个,号
        if (sb.charAt(sb.length() - 1) == ',') {
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append(")");
        return sb.toString();
    }

    /**
     * 字段名称
     */
    public static String getName(Field field) {
        String name = field.getName();
        DbField dbField = field.getAnnotation(DbField.class);
        if (dbField != null && !TextUtils.isEmpty(dbField.value())) {
            name = dbField.value();
        }
        return name;
    }

    /**
     * 字段对应的表类型
     */
    private static String getType(Class<?> type) {
        String value = null;
        // 字段类型
        if (type == String.class) {
            value = " TEXT,";
        } else if (type == Integer.class || type == int.class) {
            value = " INTEGER,";
        } else if (type == Long.class || type == long.class) {
            value = " BIGINT,";
        } else if (type == Double.class || type == double.class) {
            value = " DOUBLE,";
        } else if (type == byte[].class) {
            value = " BLOB,";
        } else if (type == Boolean.class || type == boolean.class) {
            value = " BOOLEAN,";
        }
        return value;
    }

    /**
     * 获取表名
     */
    public static String getTableName(Class entity) {
        DbTable table = (DbTable) entity.getAnnotation(DbTable.class);
        if (table != null && !TextUtils.isEmpty(table.value())) {
            return table.value();
        }
        return entity.getSimpleName();
    }

    public static File getFile(Context context, String name) {
        File directory = new File(context.getFilesDir(), "database");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        File file = new File(directory, name);
        if (file != null && !file.exists()) {
            try {
                file.createNewFile();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * 缓存表结构里面对应的成员字段，目的是怕对象添加新字段，表没有添加
     * key:表格列名   value:成员字段
     */
    public static Map<String, Field> getTableFields(Class entity, SQLiteDatabase sqLite) {
        Map<String, Field> tableFields = new HashMap<>();
        // 1.取到所有的列名
        String sql = "select * from " + getTableName(entity) + " limit 1,0";// 空表
        Cursor cursor = sqLite.rawQuery(sql, null);
        String[] columnNames = cursor.getColumnNames();
        // 2.取所有的成员变量
        Field[] fields = entity.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            String name = QuickDaoUtils.getName(field);
            if (TextUtils.isEmpty(name)) {
                continue;
            }
            for (String columnName : columnNames) {
                if (name.equals(columnName)) {
                    tableFields.put(name, field);
                    break;
                }
            }
        }
        return tableFields;
    }


    public static ContentValues getContentValues(Object bean, Map<String, Field> tableFields) {
        ContentValues values = new ContentValues();
        // 循环数据库里面表结构对应的字段
        for (Map.Entry<String, Field> entry : tableFields.entrySet()) {
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

    public static Map<String, String> getParams(Object bean, Map<String, Field> tableFields) {
        Map<String, String> map = new HashMap<>();
        // 循环数据库里面表结构对应的字段
        for (Map.Entry<String, Field> entry : tableFields.entrySet()) {
            try {
                String key = entry.getKey();
                Field field = entry.getValue();
                Object obj = field.get(bean);
                if (obj != null) {
                    String value = obj.toString();
                    if (TextUtils.isEmpty(key) || TextUtils.isEmpty(value)) {
                        continue;
                    }
                    map.put(key, value);
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return map;
    }
}
