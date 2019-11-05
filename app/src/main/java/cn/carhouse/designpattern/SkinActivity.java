package cn.carhouse.designpattern;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

import cn.carhouse.designpattern.skin.SkinManager;

public class SkinActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skin);
    }

    public void changeSkin(View view) {
        File file = new File(Environment.getExternalStorageDirectory(), "app.skin");
        if (file.exists()) {
            SkinManager.getInstance().loadSkin(file.getAbsolutePath());
        }
    }

    public void resetSkin(View view) {
        SkinManager.getInstance().loadSkin("");
    }

}
