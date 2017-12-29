package com.zwl.netdemo.retrofit;

import android.content.Context;

import com.orhanobut.logger.Logger;
import com.zwl.retrofitmodule.RetrofitClient;
import com.zwl.retrofitmodule.common.ProgressObserver;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;

/**
 * Created by Administrator on 2017/12/29.
 */

public class Test {
    public void start(Context context) {
        retrofitClientTest(context);
    }

    private void retrofitClientTest(Context context) {
        RetrofitClient.init(context, "");
        RetrofitClient.getInstance().download("http://p1.so.qhimgs1.com/bdr/300_115_/t01adaa3f75f2947acd.jpg", new
                ProgressObserver<ResponseBody>() {

                    @Override
                    public void onProgress(long progress, long total, boolean done) {
                        Logger.d(done + " " + (progress / total) + "%");
                    }

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ResponseBody responseBody) {
                        Logger.d("#############");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


}
