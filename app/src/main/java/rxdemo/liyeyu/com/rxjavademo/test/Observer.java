package rxdemo.liyeyu.com.rxjavademo.test;

/**
 * 观察者
 * Created by Liyeyu on 2016/7/11.
 */
public interface Observer<T> {
    void update(T t);
}
