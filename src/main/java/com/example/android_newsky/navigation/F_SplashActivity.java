package com.example.android_newsky.navigation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.android_newsky.R;

public class F_SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dsa_splash);

        try {

            Thread.sleep(3000); //대기 초 설정

            startActivity(new Intent(F_SplashActivity.this, Friend_View.class));

            finish();

        } catch (Exception e) { e.printStackTrace(); }
    }
}
