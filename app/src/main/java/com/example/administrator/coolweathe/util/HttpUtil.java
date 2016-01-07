package com.example.administrator.coolweathe.util;

import android.os.Build;
import android.os.Handler;
import android.os.Message;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;


/**
 * Created by Administrator on 2016/1/5.
 */
public class HttpUtil {
    public static final int OK_ERROR = -1;
    public static final int OK_SUNCCESS = 1;
    public static void OKdoGet(String url,final Handler handler)
    {
        // GET
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                handler.sendEmptyMessage(OK_ERROR);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String rusultStr = response.body().string();
                Message message = new Message();
                message.obj = rusultStr;
                message.what= OK_SUNCCESS;
                handler.sendMessage(message);
            }
        });

    }
    public static void OKdoPost(String url,final Handler handler,FormEncodingBuilder builder)
    {
        //post
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).post(builder.build()).build();
        okHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Request arg0, IOException arg1) {
                handler.sendEmptyMessage(OK_ERROR);
            }

            @Override
            public void onResponse(Response arg0) throws IOException {
                String resultStr = arg0.body().string();
                Message msg = new Message();
                msg.what = OK_SUNCCESS;
                msg.obj = resultStr;
                handler.sendMessage(msg);
            }
        });
    }
    public static void OKdoPostWithCode(String url, final Handler handler,
                                        FormEncodingBuilder builder, final int success, final int error) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).post(builder.build())
                .build();
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Request arg0, IOException arg1) {
                handler.sendEmptyMessage(error);
            }

            @Override
            public void onResponse(Response arg0) throws IOException {
                String resultStr = arg0.body().string();
                Message msg = new Message();
                msg.what = success;
                msg.obj = resultStr;
                handler.sendMessage(msg);
            }
        });
    }


}
