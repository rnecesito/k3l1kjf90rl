package com.example.sgm.japgolfapp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.sgm.japgolfapp.registration.IntroScreenFragment;
import com.example.sgm.japgolfapp.registration.MainMenuFragment;
import com.example.sgm.japgolfapp.registration.PrimaryScreenFragment;

import java.util.Locale;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Locale myLocale = new Locale("ja");
        Locale myLocale = new Locale("en");
        Locale.setDefault(myLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        if (savedInstanceState == null) {
            SharedPreferences prefs = this.getSharedPreferences(
                    "com.golf.app", Context.MODE_PRIVATE);

            String fromCounting = "com.golf.app.fromcounting";
            prefs.edit().remove(fromCounting).apply();

            String hasLoggedIn = "com.golf.app.hasloggedin";
            String firstTime = "com.golf.app.firstTimeCheck";

            Boolean b = prefs.getBoolean(hasLoggedIn, false);
            Boolean b2 = prefs.getBoolean(firstTime, false);

            if (b2) {
                if(b) {
                    getFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                            .add(R.id.container, new MainMenuFragment())
                            .commit();
                }else{
                    getFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                            .add(R.id.container, new PrimaryScreenFragment())
                            .commit();
                }
            } else {
                prefs.edit().putBoolean(firstTime, true).apply();
                getFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                        .add(R.id.container, new IntroScreenFragment())
                        .commit();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
