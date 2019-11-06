package cn.carhouse.designpattern;

import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.File;

import cn.carhouse.designpattern.skin.SkinManager;

public class AppFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_skin,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        view.findViewById(R.id.changeSkin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSkin(v);
            }
        });
        view.findViewById(R.id.resetSkin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetSkin(v);
            }
        });
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
