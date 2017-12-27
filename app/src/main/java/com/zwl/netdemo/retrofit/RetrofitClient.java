package com.zwl.netdemo.retrofit;

import android.content.Context;
import android.text.TextUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017/12/27.
 */

public class RetrofitClient {
    private static final int DEFAULT_TIMEOUT = 5;

    private ApiService mApiService;

    private OkHttpClient mOkHttpClient;

    public static String baseUrl = ApiService.Base_URL;

    private static Context mContext;

    private static RetrofitClient sInstance;

    public static RetrofitClient getInstance(Context context) {
        if (sInstance == null) {
            synchronized (RetrofitClient.class) {
                if (sInstance == null) {
                    sInstance = new RetrofitClient(context.getApplicationContext(), ApiService.Base_URL);
                }
            }
        }
        return sInstance;
    }

    private RetrofitClient() {

    }

    private RetrofitClient(Context context, String url) {
        if (TextUtils.isEmpty(url)) {
            url = baseUrl;
        }
        mOkHttpClient = new OkHttpClient.Builder().connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS).build();
        Retrofit retrofit = new Retrofit.Builder()
                .client(mOkHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(url)
                .build();
        mApiService = retrofit.create(ApiService.class);
    }

    public ApiService getApiService() {
        return mApiService;
    }
}
