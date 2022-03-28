package com.example.lintest2;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.embedding.engine.FlutterEngineCache;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugins.GeneratedPluginRegistrant;

public class MainActivity extends FlutterActivity {

    public static final String NATIVE_TAG = "原生";


    private static final String CHANNEL_NAME = "app.lin.test/main";
    private static MethodChannel channel;
    Bundle mSavedInstanceState;

    public void configureFlutterEngine(@NonNull FlutterEngine flutterEngine) {
        Log.v(NATIVE_TAG, "MainAty configureFlutterEngine");
        GeneratedPluginRegistrant.registerWith(flutterEngine);
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

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.v(NATIVE_TAG, "MainAty onCreate");
        getIntent().putExtra("cached_engine_id", FirstAty.Lin_Engine_ID);
        mSavedInstanceState = savedInstanceState;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();

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
            FlutterEngine engineCache = FlutterEngineCache.getInstance().get(FirstAty.Lin_Engine_ID);
            if (engineCache != null) {
                Log.v(MainActivity.NATIVE_TAG, "缓存引擎不为空");
                engineCache.getLifecycleChannel().appIsResumed();
            } else {
                Log.v(MainActivity.NATIVE_TAG, "缓存引擎为空");
            }
        }


    }
}
