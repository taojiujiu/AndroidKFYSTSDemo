package com.lily.androidkfysts.service.bind;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

@SuppressLint("Registered")
public class BindService extends Service {

    private final IBinder mBinder = new LocalBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("tao","BindService onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("tao","BindService onStartCommand");
        return super.onStartCommand(intent, flags, startId);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e("tao","BindService onBind");
        Log.e("tao","BindService ********" + BindService.this.toString());

        return mBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("tao","BindService onDestroy");

    }

    public int testMethod(){
        long i = 0;
        while(i < 1000000000){
            i ++;
        }
        Log.e("tao","BindService testMethod DONE");
        return 1;
    }

    public void testThreadMethod(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                long i = 0;
                Log.e("tao","BindService testThreadMethod Start");

                try {
                    Thread.sleep(50000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.e("tao","BindService testThreadMethod DONE");
                Log.e("tao","BindService ********" + BindService.this.toString());
            }
        }).start();
    }

    public class LocalBinder extends Binder {

        BindService getService() {
            return BindService.this;
        }
    }

    @Override
    public void unbindService(ServiceConnection conn) {
        super.unbindService(conn);
        Log.e("tao","BindService unbindService");
    }


}
