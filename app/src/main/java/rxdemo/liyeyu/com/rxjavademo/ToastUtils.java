package rxdemo.liyeyu.com.rxjavademo;

import android.widget.Toast;

/**
 * Created by Liyeyu on 2016/7/11.
 */
public class ToastUtils extends CommUtils{

    public static void show(String msg){
        Toast.makeText(mApp,msg,Toast.LENGTH_SHORT).show();
    }
}
