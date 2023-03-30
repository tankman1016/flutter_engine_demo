package com.example.lintest2;



import android.util.Log;


import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.android.FlutterActivityLaunchConfigs;


public class SecondAty extends MyFlutterActivity {

    public static FlutterActivity.CachedEngineIntentBuilder withCachedEngine() {
        return new FlutterActivity.CachedEngineIntentBuilder(SecondAty.class, MyFlutterActivity.Engine_ID)
                .backgroundMode(FlutterActivityLaunchConfigs.BackgroundMode.transparent)
                .destroyEngineWithActivity(false);
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.d("AAA",getActivityId());
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
    }
}
