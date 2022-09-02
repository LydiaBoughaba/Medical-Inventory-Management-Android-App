package com.boughaba.medicinestockmanagement.volley;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class FileRequest {
    private static FileRequest instance;
    private RequestQueue requestQueue;
    private static Context context;

    private FileRequest(Context context) {
        this.context = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized FileRequest getInstance(Context context) {
        if (instance == null) {
            instance = new FileRequest(context);
        }
        return instance;
    }

    private RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(this.context);
        }
        return requestQueue;
    }

    public <T> void addToRequest(Request<T> request) {
        getRequestQueue().add(request);
    }
}
