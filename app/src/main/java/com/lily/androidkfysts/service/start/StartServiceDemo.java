package com.lily.androidkfysts.service.start;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.lily.androidkfysts.ipc.Book;

public class StartServiceDemo extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("tao","StartServiceDemo  onCreate()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("tao","StartServiceDemo  onStartCommand()");
        Book book = intent.getParcelableExtra("book");
        Log.e("tao",book.toString());
        if(book != null){
            testDoLongThing();
//            testDoLongThingNoThread();
        }
        return super.onStartCommand(intent, flags, startId);
    }


    public void testDoLongThing(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e("tao","start testDoLongThing");
                try {
                    long i = 0;
                    while (i < 1000000000){
                        i ++;
                    }
                    Thread.sleep(5000);
                    Log.e("tao","Done testDoLongThing");
                    stopSelf();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void testDoLongThingNoThread(){
        long i = 0;
        while (i < 1000000000){
            i ++;
        }
        Log.e("tao","Done testDoLongThing");
        stopSelf();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("tao","StartServiceDemo  onDestroy()");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
