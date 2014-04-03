package com.yi4all.synccloud.service;


import android.os.Handler;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.yi4all.synccloud.db.ServerModel;

/**
 * Created by chenyu on 13-8-8.
 */
public class HttpUtils {

    private static final String BASE_URL = "http://10.52.2.4:9000";

    private static AsyncHttpClient client = new AsyncHttpClient();
    
    public static void get(ServerModel sm, String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(sm, url), params, responseHandler);
    }

    public static void post(ServerModel sm, String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(sm, url), params, responseHandler);
    }

    public static String getAbsoluteUrl(ServerModel sm, String relativeUrl) {
        if(sm != null){
            return "http://" + sm.getIp() + ":" + sm.getPort() + relativeUrl;
        }
        return BASE_URL + relativeUrl;
    }

}
