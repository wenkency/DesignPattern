package cn.carhouse.designpattern.db.subdb;

import java.util.List;

import cn.carhouse.designpattern.bean.User;
import cn.carhouse.designpattern.db.core.QuerySupport;
import cn.carhouse.designpattern.db.core.QuickDao;

public class UserDao extends QuickDao<User> {

    public long onLogin(User bean) {
        // 1.登录状态全部改为0
        User update = new User();
        update.setState(0);
        update(update);
        // 更改状态
        bean.setState(1);
        // 2. 查看数据库里面有没有这个用户
        // 有就更新用户信息
        // 没有就插入一条信息
        User user = getUserById(bean.getUserId());
        if (user != null) {
            // 更新数据
            update(bean, user);
            return -1;
        }
        // 插入一条用户信息
        return super.insert(bean);
    }

    /**
     * 根据ID查询用户
     */
    public User getUserById(String id) {
        User where = new User();
        where.setUserId(id);
        return getUser(where);
    }

    /**
     * 查询当前登录用户
     */
    public User getCurrentUser() {
        User where = new User();
        where.setState(1);
        return getUser(where);
    }

    private User getUser(User where) {
        QuerySupport<User> query = query();
        List<User> users = query.query(where);
        if (users.size() > 0) {
            return users.get(0);
        }
        return null;
    }

}
