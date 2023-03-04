package com.hlm.plugin.host;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hlm.plugin.host.app.PluginEnterCallback;
import com.hlm.plugin.host.app.PluginService;
import com.hlm.plugin.host.exception.PluginException;

public class MainActivity extends AppCompatActivity {

    public static final int FROM_ID_START_ACTIVITY = 1001;
    public static final int FROM_ID_CALL_SERVICE = 1002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        TextView textView = new TextView(this);
        textView.setText("宿主App");

        Button button = new Button(this);
        button.setText("启动插件");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                v.setEnabled(false);//防止点击重入

try {
    PluginService.enter(MainActivity.this, "default", FROM_ID_START_ACTIVITY, new Bundle(), new PluginEnterCallback() {
        @Override
        public void onShowLoadingView(View view) {
            MainActivity.this.setContentView(view); // 显示Manager传来的Loading页面
        }

        @Override
        public void onCloseLoadingView() {
            MainActivity.this.setContentView(linearLayout);
        }

        @Override
        public void onEnterComplete() {
            v.setEnabled(true);
        }
    });
} catch (PluginException e) {
    e.printStackTrace();
}
            }
        });

        linearLayout.addView(textView);
        linearLayout.addView(button);

        Button callServiceButton = new Button(this);
        callServiceButton.setText("调用插件Service，结果打印到Log");
        callServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setEnabled(false);//防止点击重入

                try {
                    PluginService.enter(MainActivity.this, "default", FROM_ID_CALL_SERVICE, null, null);
                } catch (PluginException e) {
                    e.printStackTrace();
                }
            }
        });

        linearLayout.addView(callServiceButton);

        setContentView(linearLayout);
    }


}