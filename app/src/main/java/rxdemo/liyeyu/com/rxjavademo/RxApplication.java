package rxdemo.liyeyu.com.rxjavademo;

import android.app.Application;

/**
 * Created by Liyeyu on 2016/7/11.
 */
public class RxApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CommUtils.init(this);
    }
}
