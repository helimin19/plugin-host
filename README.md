# 插件宿主jar包使用说明

> 在宿主APP的build.gradle文件中引入
  1. implementation project(':plugin_host')
  2. implementation 'com.squareup.retrofit2:retrofit:2.9.0'
  3. implementation 'com.squareup.retrofit2:converter-gson:2.9.0
  4. implementation project(':plugin_lib')
>> 三方件说明
  1. plugin_host: 主要的宿主插件APK
  2. retrofit: 宿主插件中更新插件管理APK使用
  3. plugin_lib: 插件管理APK或插件应用APK中可能使用到的方法(插件管理APK或插件应用APK中需要编译依赖这个包了)
  
> 在宿主APP的AndroidManifest.xml文件中引入(主要保证网络可用)
  1. <uses-permission android:name="android.permission.INTERNET" />
  2. <application android:usesCleartextTraffic="true"
  
> Retrofit代理类的实现
  1. 创建一个类实现RetrofitProvider接口
    public class RetrofitClient implements RetrofitProvider {
        @Override
        public Retrofit getRetrofit(Context context) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://192.168.31.66:8080/examples/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            return retrofit;
        }
    }

> Application的onCreate中实现
  1. 设置Retrofit代理，检测插件管理的地址，更新插件管理的地址
    RetrofitProxy.instance().setRetrofitProvider(new RetrofitClient());
    PluginManagerConfig.instance().setUrlCheck("plugin/manager/check.txt").setUrlUpdate("MainPluginManager-debug.apk");
    PluginAppConfig.instance().setBaseUrl("http://192.168.31.66:8080/examples/").setUrlCheck("plugin/app/check.txt").setUrlUpdate("plugin-debug.zip");
    注: 插件管理中不能将Retrofit传过去了，会导致找不到接口的@POST,@GET等
  2. 初使化插件宿主
    HostApplication.onApplicationCreate(this);

> 在启动页面实现插件宿主的更新
    HostApplication.updatePluginManagers(this, () -> {
        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
        startActivity(intent);
    });

> 调用插件
  PluginService.enter(Context context, String managerCode, long fromId, Bundle bundle, PluginEnterCallback callback) throws PluginException
  1. 参数说明
  第一个参数: 上下文
  第二个参数: 插件管理的编码
  第三个参数: 标识符，插件管理中使用这个标识是什么
  第四个参数: 传入给插件的参数
  第五个参数: 回调函数
  2. 回调函数说明
  onShowLoadingView: 显示插件加载中的视图
  onCloseLoadingView: 关闭插件加载中的视图
  onEnterComplete: 完成插件加载
  3. 示例
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
