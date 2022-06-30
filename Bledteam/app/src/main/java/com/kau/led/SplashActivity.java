package com.kau.led;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    MediaPlayer mediaPlayer1;
    @Override
    protected void onCreate(Bundle savedInstanceStare) {
        super.onCreate(savedInstanceStare);
        setContentView(R.layout.activity_splash);
        mediaPlayer1 = MediaPlayer.create(this,R.raw.appstart);
        mediaPlayer1.start();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), Firebase.class);
                startActivity(intent);
                finish();


            }
        },3000);
    }
    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}

