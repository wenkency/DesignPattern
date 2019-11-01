package cn.carhouse.designpattern;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.carhouse.designpattern.bean.Photo;
import cn.carhouse.designpattern.bean.User;
import cn.carhouse.designpattern.db.core.IQuickDao;
import cn.carhouse.designpattern.db.core.QuerySupport;
import cn.carhouse.designpattern.db.core.QuickDaoFactory;
import cn.carhouse.designpattern.db.subdb.UserDao;

public class MainActivity extends AppCompatActivity {

    private UserDao mUserDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUserDao = QuickDaoFactory.getInstance().getQuickDao(UserDao.class, User.class);
    }

    public void insert(View view) {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            User user = new User();
            user.setId(i);
            user.setName("Lven");
            user.setImage("image" + i);
            user.setState(i);
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
        // update emp set salary=4000,job='ccc' where name='zs';
        User bean = new User();
        bean.setState(0);
        //  将ID =1 的名字改成lfw
        User where = new User();
        where.setId(1);
        int update = mUserDao.update(bean);
        Log.e("MainActivity", "update-->" + update);
    }

    public void defLogin(View view) {
        // 用户登录成功后调用
        User bean = new User();
        bean.setId(100);
        bean.setName("LFW");
        bean.setState(1);
        bean.setImage("image");
        mUserDao.onLogin(bean);
    }

    public void changeLogin(View view) {
        // 用户登录成功后调用
        User bean = new User();
        bean.setId(0);
        bean.setName("LFW");
        bean.setState(1);
        bean.setImage("image");
        mUserDao.onLogin(bean);
    }

    public void subInsert(View view) {
        Photo photo = new Photo();
        photo.setPath("data/data/my.jpg");
        photo.setTime(new Date().toString());
        IQuickDao<Photo> photoDao = QuickDaoFactory.getInstance().getSubQuickDao(Photo.class);
        long insert = photoDao.insert(photo);
        Toast.makeText(this, "执行成功!" + insert, Toast.LENGTH_LONG).show();
    }

    public void subQuery(View view) {
        IQuickDao<Photo> photoDao = QuickDaoFactory.getInstance().getSubQuickDao(Photo.class);
        List<Photo> photos = photoDao.query().queryAll();
        for (Photo u : photos) {
            Log.e("MainActivity", u.toString());
        }
    }

    public void query(View view) {
        QuerySupport<User> query = mUserDao.query();
        query.limit("0 , 10");
        List<User> list = query.query();
        for (User u : list) {
            Log.e("MainActivity", u.toString());
        }
    }

    public void asyncQuery(View view) {
        QuerySupport<User> query = mUserDao.query();
        query.limit("0 , 10");
        query.asyncQuery(new QuerySupport.OnAsyncQueryListener<User>() {
            @Override
            public void onAsyncQueried(List<User> items) {
                for (User u : items) {
                    Log.e("MainActivity", u.toString());
                }
            }
        });
    }

}
