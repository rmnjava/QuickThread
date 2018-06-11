package com.example.quinn.quickthreadtest;

import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.github.xwdz.quickthread.QuickManager;
import com.github.xwdz.quickthread.Response;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        QuickManager.getNetwork().execute(new Runnable() {
            @Override
            public void run() {
                //do something
            }
        });



        QuickManager.getIO().delay(new Runnable() {
            @Override
            public void run() {

            }
        }, 1000);


        Log.e("TAG", "---feature init ");

        //同步执行一个call
        Future future = QuickManager.getCache().sync(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Thread.sleep(10000);
                return "this is test";
            }
        });

        try {
            Log.e("TAG","future = " + future.get());
        } catch (Exception e) {
            e.printStackTrace();
        }


        //异步执行一个call
        QuickManager.getCache().async(new Callable<String>() {
            @Override
            public String call() throws Exception {
                if (Looper.getMainLooper() == Looper.myLooper()) {
                    Log.e("TAG", "main Thread");
                } else {
                    Log.e("TAG", "child Thread");
                }
                Thread.sleep(10000);
                return "123123";
            }
        }, new Response<String>() {
            @Override
            public void onSuccess(String s) {
                Log.e("TAG", "s = " + s);
            }

            @Override
            public void onError(Throwable e) {
            }
        });
    }

    public <T> Future<T> test() {
        return new Test<>("test");
    }

    public class MyRunnable implements Runnable {

        int threadNum;

        MyRunnable(int threadNum) {
            this.threadNum = threadNum;
        }

        @Override
        public void run() {
            Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
            Log.e("打印信息：", "等待中...执行第" + threadNum + "号线程");
            try {
                sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.e("打印信息：", "完成第" + threadNum + "号线程");
        }
    }
}
