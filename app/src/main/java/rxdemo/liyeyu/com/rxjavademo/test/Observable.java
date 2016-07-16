package rxdemo.liyeyu.com.rxjavademo.test;

/**
 * 被观察者
 * Created by Liyeyu on 2016/7/11.
 */
public class Observable<T> {
    protected OnAttach onAttach;

    public Observable(OnAttach onAttach) {
        this.onAttach = onAttach;
    }

    public static <T> Observable<T> create(OnAttach<T> onAttach){
        return new Observable<>(onAttach);
    }

    public void attach(Observer<T> observer){
        onAttach.call(observer);
    }

    public <R> Observable<R> map(IFun<? super T,? extends R> iFun){
        Operator operator = new OperatorMap(iFun);
        Observable observable = lift(operator);
        return observable;
    }

    public <R> Observable<R> lift(final Operator<? super R,? extends T> operator){
        return new Observable<>(new OnAttach() {
            @Override
            public void call(Observer observer) {
                Observer call = operator.call(observer);
                Observable.this.onAttach.call(call);
            }
        });
    }

    public interface OnAttach<T> {
        void call(Observer<? super T> observer);
    }

    public interface Operator<R, T> extends IFun<Observer<? super R>, Observer<? super T>> {}
}
