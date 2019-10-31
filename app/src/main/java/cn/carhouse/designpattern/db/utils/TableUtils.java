package cn.carhouse.designpattern.db.utils;

import android.content.Context;
import android.text.TextUtils;

import java.io.File;
import java.lang.reflect.Field;

import cn.carhouse.designpattern.db.annotation.DBField;
import cn.carhouse.designpattern.db.annotation.DBTable;

public class TableUtils {

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
        DBField dbField = field.getAnnotation(DBField.class);
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
        }
        return value;
    }

    /**
     * 获取表名
     */
    public static String getTableName(Class entity) {
        DBTable table = (DBTable) entity.getAnnotation(DBTable.class);
        if (table != null && !TextUtils.isEmpty(table.value())) {
            return table.value();
        }
        return entity.getSimpleName();
    }

    public static File getFile(Context context, String name) {
        File directory = context.getFilesDir();
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
}
