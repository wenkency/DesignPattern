package cn.carhouse.designpattern.db.subdb;

import cn.carhouse.designpattern.bean.User;
import cn.carhouse.designpattern.db.core.QuickDaoFactory;

public class PrivateDBImpl implements PrivateDBSupport {
    private PrivateDBImpl() {
    }

    private static PrivateDBImpl impl = new PrivateDBImpl();

    public static PrivateDBImpl getPrivateDBImpl() {
        return impl;
    }

    private String userId;

    @Override
    public String getId() {
        UserDao userDao = QuickDaoFactory.getInstance().getQuickDao(UserDao.class, User.class);
        if (userDao != null) {
            User user = userDao.getCurrentUser();
            if (user != null) {
                userId = user.getUserId();
            }
        }
        return userId;
    }
}
