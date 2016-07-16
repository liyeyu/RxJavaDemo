package rxdemo.liyeyu.com.rxjavademo.retrofit;

import com.squareup.okhttp.RequestBody;

import java.util.Map;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.http.QueryMap;
import rx.Observable;
import rxdemo.liyeyu.com.rxjavademo.entry.AliAddrsBean;
import rxdemo.liyeyu.com.rxjavademo.entry.NewReqParams;
import rxdemo.liyeyu.com.rxjavademo.entry.NewsBean;

/**
 * Created by Liyeyu on 2016/7/13.
 */
public interface RetrofitServer {

    @GET("apistore/mobilenumber/mobilenumber")
    Call<AliAddrsBean> getLocation(@Query("a") String a,@Query("aa") String aa,@Query("aaa") String aaa);

    //rxjava
    @GET("apistore/mobilenumber/mobilenumber")
    Observable<AliAddrsBean> getLocationByRx(@Query("a") String a, @Query("aa") String aa, @Query("aaa") String aaa);

    @GET("{parameters}?a=海南省")
    Call<AliAddrsBean> getLocationAddParams(@Path("parameters") String parameters);

    @GET("apistore/mobilenumber/mobilenumber")
    Call<AliAddrsBean> getPhone(@Header("apikey") String key,@Query("phone") String phone);

    //"{}"可以使用@Path替换参数，而且只能是url地址参数
    @GET("txapi/{type}/apple")
    Call<NewsBean> getNews(@Path("type") String type,@Header("apikey") String key, @QueryMap Map<String,Integer> map);

    //@Body 用于 post 请求
    @POST("txapi/{type}/apple")
    Call<NewsBean> getNewsByBody(@Path("type") String type,@Header("apikey") String key, @Body NewReqParams params);

    //@FormUrlEncoded 用于 post 表单提交,@Field 提交项
    @FormUrlEncoded
    @POST("txapi/{type}/apple")
    Call<NewsBean> getNewsByFrom(@Field("type") String type);

    //当函数有@Multipart注解的时候，将会发送multipart数据，
    // Parts都使用@Part注解进行声明
    //Multipart parts要使用Retrofit的众多转换器之一或者实现RequestBody来处理自己的序列化。
    //这个可以用于传文件,可以改变传值的编码，默认utf_8
    @Multipart
    @POST("txapi/{type}/apple")
    Call<NewsBean> getNewsByFrom(@Part("type") RequestBody requestBody);

    //声明静态head
    @Headers({"apikey:"+RetrofitUtils.BAIDU_API_KEY,"web_vsersion:1.01","app_version:1.0.2"})
    @GET("apistore/mobilenumber/mobilenumber")
    Call<AliAddrsBean> getPhoneByHeader(@Header("apikey") String key,@Query("phone") String phone);
}
