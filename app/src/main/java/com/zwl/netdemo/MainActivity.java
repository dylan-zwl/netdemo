package com.zwl.netdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private OkHttpClient mOkHttpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initLogger();
        mOkHttpClient = new OkHttpClient();
        OkHttpGetTest();
        OkHttpPostTest();
    }

    private void initLogger() {
        Logger.init("zwl").methodCount(2).hideThreadInfo().logLevel(LogLevel.FULL).methodOffset(0);
    }

    private void OkHttpGetTest() {
        Request request = new Request.Builder().url("https://github.com/hongyangAndroid").build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Logger.d(e.getMessage());
            }

            @Override
            public void onResponse(Response response) throws IOException {
                Logger.d(response.body().string());
            }
        });
    }


    private void OkHttpPostTest() {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), "");
        Request request = new Request.Builder().url("").post(requestBody).build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Logger.d(e.getMessage());
            }

            @Override
            public void onResponse(Response response) throws IOException {
                Logger.d(response.body().string());
            }
        });

    }

}
