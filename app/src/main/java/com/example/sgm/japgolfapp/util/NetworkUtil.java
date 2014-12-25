package com.example.sgm.japgolfapp.util;

import android.util.Log;

import com.example.sgm.japgolfapp.GolfApp;
import com.example.sgm.japgolfapp.api.Api;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by CarlAnthony on 12/26/2014.
 */
public class NetworkUtil {

    public static JsonReader getReader(String object) {
        InputStream is = new ByteArrayInputStream(object.getBytes());
        StringBuffer response = new StringBuffer();
        InputStream in = null;
        JsonReader reader = null;
        try {

            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;

            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();

            String reply = response.toString();
            Log.i(GolfApp.TAG, reply);

            // reading
            in = new ByteArrayInputStream(reply.getBytes(Api.CHARSET));
            reader = new JsonReader(new InputStreamReader(in, Api.CHARSET));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return reader;
    }
}
