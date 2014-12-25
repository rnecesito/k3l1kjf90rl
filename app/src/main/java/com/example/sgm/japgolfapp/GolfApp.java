package com.example.sgm.japgolfapp;

import android.app.Application;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by CarlAnthony on 12/26/2014.
 */
public class GolfApp extends Application {

    public static String TAG = "Golf Application";
    public static RequestQueue mRequestQueue;
    public static com.android.volley.toolbox.ImageLoader mImageLoader;
    private static final String appQueueTag = "app_queue_tag";
    public static boolean has_requested = false;

    private static Context CONTEXT;

    @Override
    public void onCreate() {
        super.onCreate();

        CONTEXT = getApplicationContext();
    }

    public static RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(CONTEXT);
        }

        return mRequestQueue;
    }

    public static <T> void addToRequestQueue(Request<T> req, String tag) {
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
