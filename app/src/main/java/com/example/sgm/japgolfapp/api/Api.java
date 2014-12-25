package com.example.sgm.japgolfapp.api;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by CarlAnthony on 12/25/2014.
 */
public class Api {

    public static String WEB_URL = "http://zoogtech.com/golfapp/public/";
    public static final String CHARSET = "UTF-8";
    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";


    public static String getToken(Context context) {
        File cDir = context.getCacheDir();
        File tempFile = new File(cDir.getPath() + "/" + "golfapp_token.txt");
        String strLine = "";
        StringBuilder golfapp_token = new StringBuilder();
        try {
            FileReader fReader = new FileReader(tempFile);
            BufferedReader bReader = new BufferedReader(fReader);
            while ((strLine = bReader.readLine()) != null) {
                golfapp_token.append(strLine);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return golfapp_token.toString();

    }

}
