package com.zwl.netdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.zwl.netdemo.retrofit.ApiService;
import com.zwl.netdemo.retrofit.RetrofitClient;
import com.zwl.netdemo.retrofit.bean.Movie;
import com.zwl.netdemo.retrofit.bean.Subjects;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;


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
        OkHttpUploadTest();
//        OkHttpDownTest();

        retrofitTest();
    }

    private void initLogger() {
        Logger.init("zwl").methodCount(1).hideThreadInfo().logLevel(LogLevel.FULL).methodOffset(0);
    }

    /**
     * 功能描述 : get 基本使用
     *
     * @param :
     */
    private void OkHttpGetTest() {
        Request request = new Request.Builder().url("https://github.com/hongyangAndroid").build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Logger.d(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Logger.d(response.body().string());
            }
        });
    }

    /**
     * 功能描述 : post 基本使用
     *
     * @param :
     */
    private void OkHttpPostTest() {
//        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), "");
        RequestBody requestBody = new FormBody.Builder().add("size", "10").build();
        Request request = new Request.Builder().url("http://api.1-blog.com/biz/bizserver/article/list.do").post
                (requestBody).build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Logger.d(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Logger.d(response.body().string());
            }
        });
    }

    private void OkHttpUploadTest() {
        File file = new File("/sdcard/wangshu.txt");
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/x-markdown; charset=utf-8"), file);
        Request request = new Request.Builder().url("https://api.github.com/markdown/raw").post(requestBody).build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Logger.d(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Logger.d(response.body().string());
            }
        });
    }

    private void OkHttpDownTest() {
        String url = "http://img.my.csdn.net/uploads/201603/26/1458988468_5804.jpg";
        Request request = new Request.Builder().url(url).build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Logger.d(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    saveFile("/sdcard/wangshu.jpg", response.body().byteStream());
                } catch (IOException e) {
                    Logger.e(e.getMessage());
                }
            }
        });
    }

    private ApiService mApiService;

    private void retrofitTest() {
        mApiService = RetrofitClient.getInstance(this).getApiService();
        test1();
        retrofitDownloadTest();
        retrofitUploadFileTest();
        retrofitUploadFilesTest();
    }

    private void test1() {
        mApiService.getTopMovie("https://api.douban.com/v2/movie/top250", 0, 100).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Movie>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Movie movie) {
                        Logger.d(movie.getTitle());
                        List<Subjects> list = movie.getSubjects();
                        for (Subjects sub : list) {
                            Logger.d(sub.getId() + "," + sub.getYear() + "," + sub.getTitle());
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Logger.e(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void retrofitDownloadTest() {
        mApiService.downloadFile("http://p1.so.qhimgs1.com/bdr/300_115_/t01adaa3f75f2947acd.jpg")
                .subscribeOn(Schedulers.io()).observeOn(Schedulers.io())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ResponseBody responseBody) {
                        try {
                            saveFile("/sdcard/1.jpg", responseBody.byteStream());
                        } catch (IOException e) {
                            Logger.e(e.getMessage());
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void retrofitUploadFileTest() {
        File file = new File("");
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);

        // 添加描述
        String descriptionString = "hello, 这是文件描述";
        RequestBody description = RequestBody.create(MediaType.parse("multipart/form-data"), descriptionString);

        mApiService.uploadFile("", description, body)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ResponseBody responseBody) {

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void retrofitUploadFilesTest() {
        File file = new File("");
        MultipartBody.Builder form = new MultipartBody.Builder();
        form.setType(MultipartBody.FORM);
        form.addFormDataPart("keyName", "Zone");
        form.addFormDataPart("file", "gaga.jpg", RequestBody.create(MediaType.parse("image/*"), file));
        form.addFormDataPart("file2", "meinv.jpg", RequestBody.create(MediaType.parse("image/*"), file));
        mApiService.uploadFiles("", form.build())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ResponseBody responseBody) {

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void saveFile(String filePath, InputStream inputStream) throws IOException {
        File file = new File(filePath);
        file.delete();
        file.createNewFile();
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        byte[] buffer = new byte[2048];
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            fileOutputStream.write(buffer, 0, len);
        }
        fileOutputStream.flush();
        inputStream.close();
        fileOutputStream.close();
    }

}
