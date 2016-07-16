package rxdemo.liyeyu.com.rxjavademo.test;

/**
 * Created by Liyeyu on 2016/7/11.
 */
public class OperatorMap<R, T>  implements Observable.Operator<R, T>  {
    private IFun<? super T,? extends R> convert;

    public OperatorMap(IFun<? super T, ? extends R> convert) {
        this.convert = convert;
    }

    @Override
    public Observer<? super T> call( final Observer<? super R> observer) {
        return new Observer<T>() {
            @Override
            public void update(T t) {
                observer.update(convert.call(t));
            }
        };
    }
}
