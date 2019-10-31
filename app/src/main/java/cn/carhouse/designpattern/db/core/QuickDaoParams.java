package cn.carhouse.designpattern.db.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 参数封装
 */
class QuickDaoParams {
    String whereClause;
    String[] whereArgs;

    QuickDaoParams(Map<String, String> map) {
        // update table where 1=1 and id = 1 set name = 'lfw'
        StringBuffer sb = new StringBuffer();
        sb.append("1=1");
        List<String> list = new ArrayList<>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(" and " + key + "=?");
            list.add(value);
        }
        whereClause = sb.toString();
        whereArgs = list.toArray(new String[list.size()]);
    }
}
