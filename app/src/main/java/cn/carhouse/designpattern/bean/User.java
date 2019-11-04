package cn.carhouse.designpattern.bean;

import cn.carhouse.designpattern.db.annotation.DbTable;

@DbTable("tb_user")
public class User {
    private String userId;
    private String name;
    // 登录状态
    private int state;
    private String password;
    private String loginName;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", state=" + state +
                ", password='" + password + '\'' +
                ", loginName='" + loginName + '\'' +
                '}';
    }
}

