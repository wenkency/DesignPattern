package cn.carhouse.designpattern.bean;

import cn.carhouse.designpattern.db.annotation.DbField;
import cn.carhouse.designpattern.db.annotation.DbTable;

@DbTable("tb_user")
public class User {
    @DbField("_id")
    private Integer id;
    private String name;

    // 登录状态
    private int state;

    private String image;

    public Integer getId() {
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", state=" + state +
                ", image='" + image + '\'' +
                '}';
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}

