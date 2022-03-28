package com.example.lintest2;

import static com.example.lintest2.MainActivity.NATIVE_TAG;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.embedding.engine.FlutterEngineCache;
import io.flutter.embedding.engine.dart.DartExecutor;

public class FirstAty extends Activity {

    public static final String Lin_Engine_ID = "lin_engine";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(NATIVE_TAG, "FirstAty onCreate");
        //initSecondFlutterEngine();
        setContentView(R.layout.aty_play);
        findViewById(R.id.btn_play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(NATIVE_TAG, "去第二页");
                startActivity(SecondAty.withCachedEngine(Lin_Engine_ID).build(FirstAty.this));
            }
        });
    }

    //初始化第二引擎
    private void initSecondFlutterEngine(){
        FlutterEngine flutterEngine = new FlutterEngine(this);
        // Configure an initial route.
        flutterEngine.getNavigationChannel().setInitialRoute("second");
        // Start executing Dart code to pre-warm the FlutterEngine.
        flutterEngine.getDartExecutor().executeDartEntrypoint(
                DartExecutor.DartEntrypoint.createDefault()
        );

        // Cache the FlutterEngine to be used by FlutterActivity or FlutterFragment.
        FlutterEngineCache
                .getInstance()
                .put(Lin_Engine_ID, flutterEngine);
    }
}
