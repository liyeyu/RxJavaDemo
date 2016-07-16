package rxdemo.liyeyu.com.rxjavademo.retrofit;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;

import java.io.IOException;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rxdemo.liyeyu.com.rxjavademo.CommUtils;

/**
 * Created by Liyeyu on 2016/7/12.
 */
public class RetrofitUtils extends CommUtils {
    private static RetrofitUtils mManager;
    //使用“/”结束。否则sever中声明的将会覆盖导致url拼接不正确
    //Base URL: 总是以 /结尾
    //@Url : 不要以 / 开头
    public static final String BaseUrl = "http://apis.baidu.com/";

    private RetrofitUtils() {
    }

    public static RetrofitUtils get() {
        if (mManager == null) {
            synchronized (RetrofitUtils.class) {
                if (mManager == null) {
                    mManager = new RetrofitUtils();
                }
            }
        }
        return mManager;
    }

    //每一个Call实例可以同步(call.excute())或者异步(call.enquene(CallBack<?> callBack))的被执行，
    //每一个实例仅仅能够被使用一次，但是可以通过clone()函数创建一个新的可用的实例。
    //默认情况下，Retrofit只能够反序列化Http体为OkHttp的ResponseBody类型
    //并且只能够接受ResponseBody类型的参数作为@body
    public static Retrofit sendRequest(String url) {


        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.networkInterceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                //在调用链上执行拦截器，为正在执行的请求做额外操作
                Response proceed = chain.proceed(chain.request());
                return proceed;
            }
        });

        return new Retrofit.Builder()
                .baseUrl(url)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())// 使用RxJava作为回调适配器
                .addConverterFactory(GsonConverterFactory.create()) // 使用Gson作为数据转换器
                .client(okHttpClient)//委托OkHttp拦截器
                .build();
    }

}
