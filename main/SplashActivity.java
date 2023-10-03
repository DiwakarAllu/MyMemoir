package com.example.mystickynotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView imageView=findViewById(R.id.imageView);
        TextView textView=findViewById(R.id.textView);

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale);
        imageView.startAnimation(animation);
        //textView.startAnimation(animation);

        Intent iHome=new Intent(SplashActivity.this,MainActivity.class);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(iHome);
                finish();
            }
        },2000);



    }
}