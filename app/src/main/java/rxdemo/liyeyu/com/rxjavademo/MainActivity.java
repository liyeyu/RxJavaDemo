package rxdemo.liyeyu.com.rxjavademo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxCompoundButton;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import com.trello.rxlifecycle.ActivityEvent;
import com.trello.rxlifecycle.components.RxActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rxdemo.liyeyu.com.rxjavademo.entry.A;
import rxdemo.liyeyu.com.rxjavademo.entry.AB;
import rxdemo.liyeyu.com.rxjavademo.entry.B;
import rxdemo.liyeyu.com.rxjavademo.entry.NewsBean;
import rxdemo.liyeyu.com.rxjavademo.retrofit.RetrofitServer;
import rxdemo.liyeyu.com.rxjavademo.retrofit.RetrofitUtils;

/**
 * 继承自RxActivity，管理订阅的生命周期
 */
public class MainActivity extends RxActivity {

    private TextView mTv;
    private String str = "";
    private CheckBox cbText;
    private Button btnSearch;
    private Button btnRetrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTv = (TextView) findViewById(R.id.tv_rx);
        cbText = (CheckBox) findViewById(R.id.cb_text1);
        btnSearch = (Button) findViewById(R.id.btn_search);
        btnRetrofit = (Button) findViewById(R.id.btn_retrofit);
        //RxJava 被观察者发送一个事件给订阅者
        Observable.just("hello world!").subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
//                mTv.setText(s);
            }
        });
//-------------
        //RxJava 被观察者发送一个事件给订阅者,带map操作符，针对某个订阅者的事件进行修改,返回对象为string
        Observable.just("hello world!").map(new Func1<String, String>() {
            @Override
            public String call(String s) {
                return s + " I am boy.";
            }
        }).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
//                mTv.setText(s);
            }
        });
//-------------
        //RxJava 被观察者发送一个事件给订阅者,带map操作符，针对某个订阅者的事件进行修改,返回对象为自定义对象
        Observable.just("hello world!").map(new Func1<String, RxJavaInfo>() {
            @Override
            public RxJavaInfo call(String s) {
                return new RxJavaInfo(s + " I am boy 123456.");
            }
        }).subscribe(new Action1<RxJavaInfo>() {
            @Override
            public void call(RxJavaInfo s) {
//                mTv.setText(s.mapStr);
            }
        });
//-------------
        // RxJava 构建集合查询
        query("haha").subscribe(new Action1<List<String>>() {
            @Override
            public void call(List<String> strings) {
                //轮训集合
                Observable.from(strings).subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
//                        str+=s;
//                        mTv.setText(str);
                    }
                });
            }
        });
//-------------
        //RxJava 构建集合查询 flatMap关键字将集合拆分
        query("haha").flatMap(new Func1<List<String>, Observable<String>>() {
            @Override
            public Observable<String> call(List<String> strings) {
                return Observable.from(strings);
            }
            //指定输出数量
        }).take(1).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
//                Logg.i(s);
            }
        });
//-------------
        //RxJava 构建集合查询 flatMap关键字将集合拆分
        query("haha").flatMap(new Func1<List<String>, Observable<String>>() {
            @Override
            public Observable<String> call(List<String> strings) {
                return Observable.from(strings);
            }
            //拆分子项
        }).flatMap(new Func1<String, Observable<String>>() {
            @Override
            public Observable<String> call(String s) {
                return getTitle(s);
            }
        }).filter(new Func1<String, Boolean>() {
            @Override
            public Boolean call(String s) {
                //输出满足条件的项
                return s.contains("s1");
            }
            //在subscribe订阅者的 onNext()方法执行前进行额外操作
        }).doOnNext(new Action1<String>() {
            @Override
            public void call(String s) {
//                Logg.i(s + "--doOnNext");
            }
        }).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
//                Logg.i(s);
            }
        });
//-------------
        //线程调度
        Subscription subscription = query("haha")
                //指定数据发射在I/O线程中执行，只能执行一次
                .subscribeOn(Schedulers.io())
                //observeOn用于切换线程，指定调用链后面的节点在哪个线程中执行
                .observeOn(AndroidSchedulers.mainThread())
                //将订阅事件的生命周期和activity的声明周期绑定
                // .compose(this.<List<String>>bindToLifecycle())
                //或者指定activity的某个生命周期时取消订阅
                .compose(this.<List<String>>bindUntilEvent(ActivityEvent.STOP))
                .subscribe(new Action1<List<String>>() {
                    @Override
                    public void call(List<String> list) {
//                        Logg.i("list:" + list.size());
                    }
                });
        //取消订阅，停止整个调用链
        subscription.unsubscribe();
//-------------
        //RxBinding 操作view 点击事件
        //防抖处理 2s内响应一次
        mTv.setText("throttleFirst");
        RxView.clicks(btnSearch).throttleFirst(2, TimeUnit.SECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startActivity(new Intent(MainActivity.this,SearchActivity.class));
            }
        });
        RxCompoundButton.checkedChanges(cbText).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                Logg.i(aBoolean.toString());
//                merge();
//                mergeList();
                mergeListZip();
            }
        });
//-------------
        RxView.clicks(btnRetrofit).throttleFirst(2, TimeUnit.SECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Retrofit retrofit = RetrofitUtils.sendRequest(RetrofitUtils.BaseUrl);
                RetrofitServer retrofitServer = retrofit.create(RetrofitServer.class);
                RequestBody requestBody = RequestBody.create(MediaType.parse("utf-8"), "海南省");
//                Call<AliAddrsBean> call = retrofitServer.getLocation("海南省", "澄迈县", "老城镇");
//                Call<AliAddrsBean> call = retrofitServer.getLocationAddParams("geocoding");
//                Call<AliAddrsBean> call = retrofitServer.getPhone(CommUtils.BAIDU_API_KEY,"18689484512");
                Map<String,Integer> map = new HashMap<>();
                map.put("num",5);
                map.put("page",2);
                Call<NewsBean> call = retrofitServer.getNews("apple",CommUtils.BAIDU_API_KEY, map);
//                NewReqParams reqParams = new NewReqParams();
//                reqParams.setNum(5);
//                reqParams.setPage(2);
//                Call<NewsBean> call = retrofitServer.getNewsByBody("apple",CommUtils.BAIDU_API_KEY, reqParams);
//                try {
//                    //同步请求
//                    call.execute();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                //异步队列请求
                call.enqueue(new Callback<NewsBean>() {
                    @Override
                    public void onResponse(Response<NewsBean> response, Retrofit retrofit) {
                        String text = "";
                        NewsBean body = response.body();
                        //retrofit2.0中不管对象是否能被解析，都会调用onResponse，所以要进行判空
                        if(body!=null && body.getCode()==200){
                            List<NewsBean.NewslistBean> newslist = body.getNewslist();
                            for (NewsBean.NewslistBean item:newslist) {
                                text += item.getTitle() + "\n";
                            }
                        }
                        mTv.setText(text);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        mTv.setText(t.getMessage().toString());
                    }
                });
            }
        });
    }

    /**
     * 向订阅者发布数组
     * @param text
     * @return
     */
    private Observable<List<String>> query(String text) {
        return Observable.just(Arrays.asList(new String[]{"s1"+text,"s2"+text}));
    }
    /**
     * 向订阅者发布Observable
     * @param
     * @return
     */
    private Observable<String> getTitle(String URL) {
        return Observable.just(URL+"_java");
    }

    /**
     * 将2个对象合并成一个对象，
     */
    private void merge() {
        Observable<A> a = Observable.just(new A(1));
        Observable<B> b = Observable.just(new B(2));
        Observable.zip(a, b, new Func2<A, B, AB>() {
            @Override
            public AB call(A a, B b) {
                return new AB(a.a,b.b);
            }
        })
         .subscribe(new Action1<AB>() {
                    @Override
                    public void call(AB ab) {
                        ToastUtils.show(ab.toString());
                    }
         });
    }
    /**
     * 将2个集合合并成一个集合，等长
     */
    private void mergeList() {
        List<A> as = new ArrayList<>();
        as.add(new A(1));
        as.add(new A(2));
        List<B> bs = new ArrayList<>();
        bs.add(new B(2));
        bs.add(new B(2));

        Observable.zip(Observable.just(as), Observable.just(bs), new Func2<List<A>, List<B>, List<AB>>() {
            @Override
            public List<AB> call(List<A> as, List<B> bs) {
                List<AB> abs = new ArrayList<>();
                for (int i=0;i<as.size();i++) {
//                    abs.add(as.get(i)+bs.get(i))
                }
                return abs;
            }
        }).subscribe(new Action1<List<AB>>() {
            @Override
            public void call(List<AB> ab) {
                for (AB item:ab) {
                    Logg.i(item.toString());
                }
            }
        });
    }
    /**
     * 将2个集合合并成一个集合，不等长
     */
    private void mergeListZip() {
        List<String> hellos = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            hellos.add("hello i:" + i);
        }
        List<Integer> worlds = new ArrayList<>();
        for (int j = 0; j < 5; j++) {
            worlds.add(j);
        }
        Observable.zip(Observable.from(hellos).subscribeOn(Schedulers.computation()), Observable.from(worlds).subscribeOn(Schedulers.computation())
                , new Func2<String, Integer, String>() {

                    @Override
                    public String call(String s, Integer integer) {
                         return "index:" + integer + "\t s:" + s;
                    }
                })
        .subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Logg.i(s);
            }
        });
    }
}
