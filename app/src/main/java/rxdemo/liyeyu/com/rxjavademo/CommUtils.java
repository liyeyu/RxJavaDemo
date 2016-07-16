package rxdemo.liyeyu.com.rxjavademo;

import android.app.Application;

/**
 * Created by Liyeyu on 2016/7/11.
 */
public class CommUtils {

    public static Application mApp;
    public static final String BAIDU_API_KEY = "7fb1aea1948021894cc76bf3c160f6e6";
    public static void init(Application app){
        mApp = app;
    }
}
