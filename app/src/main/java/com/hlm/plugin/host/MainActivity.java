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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        TextView textView = new TextView(this);
        textView.setText("宿主App");
        linearLayout.addView(textView);

        Button button = new Button(this);
        button.setText("启动插件0");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                v.setEnabled(false);//防止点击重入

                try {
                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.KEY_PLUGIN_PART_KEY, "sample-plugin");
                    bundle.putString(Constant.KEY_ACTIVITY_CLASSNAME, "com.example.shadowapp.MainActivity");

                    PluginService.enter(MainActivity.this, "default", 100, bundle, new PluginEnterCallback() {
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
        linearLayout.addView(button);


        Button button1 = new Button(this);
        button1.setText("启动插件-Demo1");
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                v.setEnabled(false);//防止点击重入

                try {
                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.KEY_PLUGIN_PART_KEY, "plugin-demo1");
                    bundle.putString(Constant.KEY_ACTIVITY_CLASSNAME, "com.hlm.plugin.app.demo1.MainActivity");

                    PluginService.enter(MainActivity.this, "default", 200, bundle, new PluginEnterCallback() {
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
        linearLayout.addView(button1);


        Button button2 = new Button(this);
        button2.setText("启动插件-Demo2");
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                v.setEnabled(false);//防止点击重入

                try {
                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.KEY_PLUGIN_PART_KEY, "plugin-demo2");
                    bundle.putString(Constant.KEY_ACTIVITY_CLASSNAME, "com.hlm.plugin.app.demo2.MainActivity");

                    PluginService.enter(MainActivity.this, "default", 300, bundle, new PluginEnterCallback() {
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
        linearLayout.addView(button2);


        Button callServiceButton = new Button(this);
        callServiceButton.setText("调用插件Service，结果打印到Log");
        callServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setEnabled(false);//防止点击重入

                try {
                    PluginService.enter(MainActivity.this, "default", 100, null, null);
                } catch (PluginException e) {
                    e.printStackTrace();
                }
            }
        });

        linearLayout.addView(callServiceButton);

        setContentView(linearLayout);
    }


}