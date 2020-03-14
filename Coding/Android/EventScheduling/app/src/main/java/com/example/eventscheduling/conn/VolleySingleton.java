package com.example.eventscheduling.conn;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class VolleySingleton extends Application {
    private Context context;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    private static VolleySingleton mInstance;
    private static final String TAG = "VolleySingleton";

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }


    public static synchronized VolleySingleton getInstance() { return mInstance; }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addReqToQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addReqToQueue(Request<T> request) {
        request.setTag(TAG);
        getRequestQueue().add(request);
        Log.d(TAG, "addReqToQueue: called");
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (imageLoader == null) {
            imageLoader = new ImageLoader(this.requestQueue,
                    new LruBitmapCache());
        }
        return this.imageLoader;
    }

    public void cancelPendingRequests(Object tag) {
        if (requestQueue != null) {
            requestQueue.cancelAll(tag);
        }
    }
}
