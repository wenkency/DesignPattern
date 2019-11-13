package cn.lven.module;

import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ModuleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module);

        TextView tv = findViewById(R.id.tv);
        String url = "carhouse://order.b/detail/delivery?id=123";
        Uri uri = Uri.parse(url);
        StringBuffer sb = new StringBuffer();
        sb.append(uri.getScheme() + "\n");
        sb.append(uri.getHost() + "\n");
        sb.append(uri.getPath() + "\n");
        sb.append(uri.getQueryParameterNames() + "\n");
        tv.setText(sb.toString());

    }
}
