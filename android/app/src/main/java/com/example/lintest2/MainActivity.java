package com.example.lintest2;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;


public class MainActivity extends MyFlutterActivity {

    public static final String NATIVE_TAG = "原生";

    private static final String CHANNEL_NAME = "app.lin.test/main";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FlutterEngine engine = getFlutterEngine();
        if (engine != null) {
            MethodChannel channel = new MethodChannel(getFlutterEngine().getDartExecutor().getBinaryMessenger(), CHANNEL_NAME);
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

    }


}
