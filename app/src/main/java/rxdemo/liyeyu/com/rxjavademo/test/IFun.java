package rxdemo.liyeyu.com.rxjavademo.test;

/**
 * 操作符接口
 * Created by Liyeyu on 2016/7/11.
 */
public interface IFun<T,R> {
    R call(T t);
}
