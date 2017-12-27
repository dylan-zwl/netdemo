package com.zwl.netdemo.retrofit;


import com.zwl.netdemo.retrofit.bean.Movie;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by Administrator on 2017/12/27.
 */

public interface ApiService {
    public static final String Base_URL = "http://ip.taobao.com/";

    @GET("service/getIpInfo.php/")
    Observable<ResponseBody> getData(@Query("ip") String ip);

    @GET("{url}")
    Observable<ResponseBody> executeGet(@Path("url") String url, @QueryMap Map<String, String> maps);

    @GET()
    Observable<Movie> getTopMovie(@Url String url, @Query("start") int start, @Query("count") int count);

    @POST("{url}")
    Observable<ResponseBody> executePost(
            @Path("url") String url,
            @Header("key") String key,
            @FieldMap Map<String, String> maps);

    /**
     * 功能描述 : 上传单个文件
     *
     * @param :
     */
    @Multipart //注解上传文件
    @POST("{url}")
    Observable<ResponseBody> uploadFile(
            @Path("url") String url,
            @Part("description") RequestBody description,
            @Part MultipartBody.Part file);

    /**
     * 功能描述 : 上传多个文件
     *
     * @param :
     */
    @Multipart
    @POST()
    Observable<ResponseBody> uploadFiles(
            @Url String url,
            @Body MultipartBody body);

    /**
     * 功能描述 : 下载文件
     *
     * @param :
     */
    //Streaming意味着立刻传递字节码，而不需要把整个文件读进内存。值得注意的是，
    // 如果你使用了@Streaming，并且会抛出android.os.NetworkOnMainThreadException异常，那么请将你的请求放到子线程中去
    @Streaming
    @GET()
    Observable<ResponseBody> downloadFile(@Url String url);
}
