package rxdemo.liyeyu.com.rxjavademo;

import android.util.Log;

/**
 * Created by Liyeyu on 2016/7/9.
 */
public class Logg {
    public static final boolean DEBUG = true;
    public static final String TAG = "liyeyu";
    public static void i(String msg){
        if(DEBUG)
            Log.i(TAG, msg);
    }
}
