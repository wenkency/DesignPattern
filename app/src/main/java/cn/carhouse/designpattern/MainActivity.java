package cn.carhouse.designpattern;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import cn.carhouse.designpattern.bean.User;
import cn.carhouse.designpattern.db.core.DaoFactory;
import cn.carhouse.designpattern.db.core.IBaseDao;

public class MainActivity extends AppCompatActivity {

    private IBaseDao<User> mUserDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUserDao = DaoFactory.getInstance().getBaseDao(User.class);

    }

    public void test(View view) {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            User user = new User();
            user.setId(i);
            user.setName("Lven");
            user.setImage("image" + i);
            users.add(user);
        }
        final long begin = System.currentTimeMillis();
        mUserDao.insert(users, new IBaseDao.OnInsertListener() {
            @Override
            public void onInserted() {
                long end = System.currentTimeMillis() - begin;
                Log.e("insert", "insert-->" + end);
            }
        });


    }
}
