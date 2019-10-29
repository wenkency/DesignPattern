package cn.carhouse.designpattern;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import cn.carhouse.designpattern.builder.title.DefTitleBar;
import cn.carhouse.designpattern.builder.title.DefTitleBuilder;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DefTitleBar titleBar = DefTitleBuilder.create(this).build();
        titleBar.seTitle("主页面");
        titleBar.colorStyle(Color.parseColor("#008577"), false);

        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {

            }
        });
        dialog.show();
    }
}
