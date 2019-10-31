package cn.carhouse.designpattern;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import cn.carhouse.designpattern.bean.User;
import cn.carhouse.designpattern.db.core.QuickDaoFactory;
import cn.carhouse.designpattern.db.core.IQuickDao;
import cn.carhouse.designpattern.db.core.QuerySupport;

public class MainActivity extends AppCompatActivity {

    private IQuickDao<User> mUserDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUserDao = QuickDaoFactory.getInstance().getBaseDao(User.class);
    }

    public void insert(View view) {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            User user = new User();
            user.setId(i);
            user.setName("Lven");
            user.setImage("image" + i);
            users.add(user);
        }
        final long begin = System.currentTimeMillis();
        mUserDao.insert(users, new IQuickDao.OnInsertListener() {
            @Override
            public void onInserted() {
                long end = System.currentTimeMillis() - begin;
                Log.e("MainActivity", "insert-->" + end);
            }
        });
    }

    public void delete(View view) {
        // 删除 id=0 并且 name=Lven的
        User where = new User();
        where.setId(0);
        where.setName("Lven");
        int delete = mUserDao.delete(where);
        Log.e("MainActivity", "delete-->" + delete);
    }

    public void update(View view) {
        // update table where _id = 1 set name = 'lfw'

        User bean = new User();
        bean.setName("lfw");

        //  将ID =1 的名字改成lfw
        User where = new User();
        where.setId(1);
        int update = mUserDao.update(bean, where);
        Log.e("MainActivity", "update-->" + update);
    }


    public void query(View view) {
        QuerySupport<User> query = mUserDao.query();
        User user = new User();
        user.setName("lfw");
        query.query(user, new QuerySupport.OnQueryListener<User>() {
            @Override
            public void onQueried(List<User> list) {
                for (User u : list) {
                    Log.e("MainActivity", u.toString());
                }
            }
        });

    }
}
