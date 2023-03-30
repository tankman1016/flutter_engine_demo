package com.example.lintest2;

import static com.example.lintest2.MainActivity.NATIVE_TAG;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;


public class FirstAty extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(NATIVE_TAG, "FirstAty onCreate");
        setContentView(R.layout.aty_play);
        findViewById(R.id.btn_play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(NATIVE_TAG, "去第二页");
                startActivity(SecondAty.withCachedEngine().build(FirstAty.this));
            }
        });
    }
}
