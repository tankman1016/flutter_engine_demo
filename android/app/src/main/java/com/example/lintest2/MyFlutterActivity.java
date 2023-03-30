package com.example.lintest2;

import android.os.Bundle;

import androidx.annotation.Nullable;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.embedding.engine.FlutterEngineCache;
import io.flutter.embedding.engine.dart.DartExecutor;

public class MyFlutterActivity extends FlutterActivity {

    private Bundle mSavedInstanceState;
    public static final String Engine_ID = "lin_engine";
    //FlutterEngine 内部没有提供是否绑定了activity这里用逻辑判断
    public static String AttachAtyName = "";

    private String activityId="";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        activityId=this.getClass().getSimpleName()+System.currentTimeMillis();
        initFlutterEngine();
        mSavedInstanceState = savedInstanceState;
        super.onCreate(savedInstanceState);
        AttachAtyName=activityId;
    }

    @Override
    protected void onStart() {
        if(!AttachAtyName.equals(activityId)){
            super.onCreate(mSavedInstanceState);
            AttachAtyName=activityId;
        }
        super.onStart();
    }

    public String getActivityId() {
        return activityId;
    }

    private void initFlutterEngine() {

        FlutterEngine engineCache = FlutterEngineCache.getInstance().get(Engine_ID);
        if (engineCache != null) {
            return;
        }

        //flutter 引擎 new的时候会自动注册插件
        FlutterEngine flutterEngine = new FlutterEngine(this);
        // 初始化路由名字，abc。可在 flutter 端用 window.defaultRouteName
        // flutterEngine.getNavigationChannel().setInitialRoute("abc");
        // Start executing Dart code to pre-warm the FlutterEngine.
        flutterEngine.getDartExecutor().executeDartEntrypoint(
                DartExecutor.DartEntrypoint.createDefault()
        );

//

        // Cache the FlutterEngine to be used by FlutterActivity or FlutterFragment.
        // 将当前引擎缓存下来
        FlutterEngineCache
                .getInstance()
                .put(Engine_ID, flutterEngine);

        //将cached_engine_id 设置成Engine_ID ，以便让当前activity 使用缓存引擎
        getIntent().putExtra("cached_engine_id", Engine_ID);
    }
}

