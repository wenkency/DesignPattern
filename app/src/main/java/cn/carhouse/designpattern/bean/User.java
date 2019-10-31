package cn.carhouse.designpattern.bean;

import cn.carhouse.designpattern.db.annotation.DBField;
import cn.carhouse.designpattern.db.annotation.DBTable;

@DBTable("tb_user")
public class User {
    @DBField("_id")
    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
