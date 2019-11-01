package cn.carhouse.designpattern.db.update;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: UpdateDb
 * @Description: 更新数据库脚本
 */
public class UpdateDb {
    /**
     * 数据库名称
     */
    private String dbName;
    /**
     *
     */
    private List<String> sqlBefores;
    /**
     *
     */
    private List<String> sqlAfters;

    public UpdateDb(Element ele) {
        dbName = ele.getAttribute("name");
        sqlBefores = new ArrayList<>();
        sqlAfters = new ArrayList<>();

        {
            NodeList sqls = ele.getElementsByTagName("SqlBefore");
            for (int i = 0; i < sqls.getLength(); i++) {
                String sql_before = sqls.item(i).getTextContent();
                this.sqlBefores.add(sql_before);
            }
        }

        {
            NodeList sqls = ele.getElementsByTagName("SqlAfter");
            for (int i = 0; i < sqls.getLength(); i++) {
                String sql_after = sqls.item(i).getTextContent();
                this.sqlAfters.add(sql_after);
            }
        }

    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public List<String> getSqlBefores() {
        return sqlBefores;
    }

    public void setSqlBefores(List<String> sqlBefores) {
        this.sqlBefores = sqlBefores;
    }

    public List<String> getSqlAfters() {
        return sqlAfters;
    }

    public void setSqlAfters(List<String> sqlAfters) {
        this.sqlAfters = sqlAfters;
    }
}
