package com.cretlabs.rxjavaintroduction;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;

import com.cretlabs.rxjavaintroduction.utils.NetworkCall;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class NetworkingActivity extends AppCompatActivity {
    private static final String TAG = "NetworkingActivity";
    NetworkCall networkCall;
    AppCompatTextView mAppCompatTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_networking);
        mAppCompatTextView = findViewById(R.id.AN_txt);
        networkCall = new NetworkCall();
        Observer<String> networkCallObserver = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe() called with: d = [" + d + "]");
            }

            @Override
            public void onNext(String s) {
                Log.d(TAG, "onNext() called with: s = [" + s + "]");
                mAppCompatTextView.setText(s);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError() called with: e = [" + e + "]");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete() called");
            }
        };

        getDataFromServer().subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(networkCallObserver);

    }

    public Observable<String> getDataFromServer() {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                try {
                    String response = networkCall.makeServiceCall("http://thoughtnerds.com/api/get_recent_posts/");
                    emitter.onNext(response);
                } catch (Exception e) {
                    emitter.onError(e);
                }
            }
        });
    }

}
