package com.splash;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.LoginActivity;
import com.adapter.dprdbottom.R;

public class splashscreen extends AppCompatActivity {

    private int waktu_loading=1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent home=new Intent(splashscreen.this, LoginActivity.class);
                startActivity(home);
                finish();

            }
        },waktu_loading);

    }
}
