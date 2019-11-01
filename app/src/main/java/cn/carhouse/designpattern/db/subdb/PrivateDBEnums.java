package cn.carhouse.designpattern.db.subdb;

import cn.carhouse.designpattern.bean.User;
import cn.carhouse.designpattern.db.core.QuickDaoFactory;

/**
 * 私有数据库的枚举类
 * 实际开发更改这里就好了
 */
public enum PrivateDBEnums {
    path("");

    private String name;

    PrivateDBEnums(String name) {
        this.name = name;
    }

    public String getValue() {
        UserDao userDao = QuickDaoFactory.getInstance().getQuickDao(UserDao.class, User.class);
        if (userDao != null) {
            User user = userDao.getCurrentUser();
            if (user != null) {
                return user.getId() + "_login.db";
            }
        }
        return name;
    }
}