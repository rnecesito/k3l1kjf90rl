package com.example.sgm.japgolfapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;

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
        WebView wv = (WebView) findViewById(R.id.splashscreen);
        WebSettings settings = wv.getSettings();
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        wv.loadUrl("file:///android_asset/splash_movie.gif");

        final SplashActivity sPlashScreen = this;

        mSplashThread = new Thread() {
            @Override
            public void run() {
                try {
                    synchronized (this) {
                        wait(4100);
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
