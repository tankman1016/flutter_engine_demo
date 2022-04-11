package com.example.lintest2;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.embedding.engine.FlutterEngineCache;
import io.flutter.embedding.engine.dart.DartExecutor;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

public class MainActivity extends FlutterActivity {

    public static final String NATIVE_TAG = "原生";


    private static final String CHANNEL_NAME = "app.lin.test/main";
    private static MethodChannel channel;
    private Bundle mSavedInstanceState;

    public static final String Engine_ID = "lin_engine";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.v(NATIVE_TAG, "MainAty onCreate");
        //需要在页面中初始化，这样flutter层调用比方一些和页面相关的东西不会报错
        initFlutterEngine();
        mSavedInstanceState = savedInstanceState;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //由于一个flutter引擎，只能和一个页面aty绑定
        //当引擎与其他页面绑定时，当前页面会卡住不动
        //这块代码处理，引擎与当前aty的重新绑定
        try {
            FlutterEngine engine = getFlutterEngine();
            if (engine != null) {
                Log.v(MainActivity.NATIVE_TAG, "当前引擎不为空");
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            Log.v(MainActivity.NATIVE_TAG, "当前引擎为空");
            super.onCreate(mSavedInstanceState);
            FlutterEngine engineCache = FlutterEngineCache.getInstance().get(Engine_ID);
            if (engineCache != null) {
                Log.v(MainActivity.NATIVE_TAG, "缓存引擎不为空");
                engineCache.getLifecycleChannel().appIsResumed();
            } else {
                Log.v(MainActivity.NATIVE_TAG, "缓存引擎为空");
            }
        }


    }

    private void initFlutterEngine() {
        //flutter 引擎 new的时候会自动注册插件
        FlutterEngine flutterEngine = new FlutterEngine(this);
        // 初始化路由名字，abc。可在 flutter 端用 window.defaultRouteName
        flutterEngine.getNavigationChannel().setInitialRoute("abc");
        // Start executing Dart code to pre-warm the FlutterEngine.
        flutterEngine.getDartExecutor().executeDartEntrypoint(
                DartExecutor.DartEntrypoint.createDefault()
        );

        channel = new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), CHANNEL_NAME);
        channel.setMethodCallHandler(new MethodChannel.MethodCallHandler() {
            @Override
            public void onMethodCall(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
                switch (call.method) {
                    case "goToFirstAty":
                        Intent intent = new Intent(MainActivity.this, FirstAty.class);
                        startActivity(intent);
                        break;
                }
            }
        });

        // Cache the FlutterEngine to be used by FlutterActivity or FlutterFragment.
        // 将当前引擎缓存下来
        FlutterEngineCache
                .getInstance()
                .put(Engine_ID, flutterEngine);

        //将cached_engine_id 设置成Engine_ID ，以便让当前activity 使用缓存引擎
        getIntent().putExtra("cached_engine_id", Engine_ID);
    }
}
