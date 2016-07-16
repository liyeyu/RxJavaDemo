package rxdemo.liyeyu.com.rxjavademo;

import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.trello.rxlifecycle.components.RxActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class SearchActivity extends RxActivity {

    private AppCompatEditText mEt_search;
    private ListView mListView;
    private List<String> mArrayList = new ArrayList<>();
    private String[] strs = new String[]{"abc","ab","ba","hello","adb"};
    private ArrayAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mEt_search = (AppCompatEditText) findViewById(R.id.et_search);
        mListView = (ListView) findViewById(R.id.lv_search);
        mAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,mArrayList);
        mListView.setAdapter(mAdapter);

        search(mEt_search);
    }
    private void search(EditText editText) {
        RxTextView.textChanges(editText)
                .debounce(200, TimeUnit.MILLISECONDS)
                //将editText的CharSequence转化为String
                .map(new Func1<CharSequence, String>() {
                    @Override
                    public String call(CharSequence charSequence) {
                        return charSequence.toString();
                    }
                })
                .observeOn(Schedulers.io())
                //在子线程中执行完成
                .map(new Func1<String, List<String>>() {
                    @Override
                    public List<String> call(String s) {
                        mArrayList.clear();
                        if(!TextUtils.isEmpty(s)){
                            for (String str:strs) {
                                if(str.contains(s)){
                                    mArrayList.add(str);
                                }
                            }
                        }
                        return mArrayList;
                    }
                })
                //切换到主线程
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<String>>() {
                    @Override
                    public void call(List<String> strings) {
                        mAdapter.notifyDataSetChanged();
                    }
                });
    }

}
