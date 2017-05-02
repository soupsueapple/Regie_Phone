package com.keertech.regie_phone.Network;

import com.keertech.regie_phone.Constant.Constant;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by soup on 2017/5/2.
 */

public class HttpClient {

    private static final String BASE_URL = Constant.Base_URL;

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        if(params == null) params = new RequestParams();

        if(!url.equals(Constant.Login_URL) || !url.equals(Constant.EXEC)){
            params.put("userId",Constant.userId);
            params.put("deviceId",Constant.deviceId);
        }
        client.setTimeout(240*1000);
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        if(params == null) params = new RequestParams();

        if(!url.equals(Constant.Login_URL) || !url.equals(Constant.EXEC)){
            params.put("userId",Constant.userId);
            params.put("deviceId",Constant.deviceId);
        }
        client.setTimeout(240*1000);
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        if(relativeUrl.contains("http://api.zdoz.net/")) return relativeUrl;
        else return BASE_URL + relativeUrl;
    }
}
