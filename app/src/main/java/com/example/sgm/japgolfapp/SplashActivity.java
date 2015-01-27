package com.example.sgm.japgolfapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;

/**
 * Created by Rey on 1/13/2015.
 */
public class SplashActivity extends Activity{
    private Thread mSplashThread;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash_layout);
        final ImageView splash = (ImageView) findViewById(R.id.splashscreen);
        splash.post(new Runnable() {
            @Override
            public void run() {
                AnimationDrawable anim = (AnimationDrawable) splash.getBackground();
                anim.start();
            }
        });
//        splash.setBackgroundResource(R.drawable.splash_animated);
        final SplashActivity sPlashScreen = this;

        mSplashThread = new Thread() {
            @Override
            public void run() {
                try {
                    synchronized (this) {
                        wait(4000);
                    }
                } catch (InterruptedException ex) {
                }

                finish();
                Intent intent = new Intent();
                intent.setClass(sPlashScreen, MainActivity.class);
                startActivity(intent);
            }
        };

        mSplashThread.start();
    }
}
