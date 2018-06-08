package com.lily.androidkfysts.service.bind;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.lily.androidkfysts.R;

class OtherActivity extends Activity implements View.OnClickListener {
    boolean isConnect;
    BindService bindService;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button click = findViewById(R.id.btn);
        Button stop = findViewById(R.id.stop);
        Button unBind = findViewById(R.id.unbind);
        Button bind = findViewById(R.id.bind);

        stop.setOnClickListener(this);
        bind.setOnClickListener(this);
        unBind.setOnClickListener(this);
        click.setOnClickListener(this);

        // 开启新的线程就可以做耗时的操作，为什么要用service ?
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
//                    Log.e("tao", "OtherActivity start");
                    Thread.sleep(5000);
//                    Log.e("tao", OtherActivity.this.toString());
//                    Log.e("tao", "OtherActivity done");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            isConnect = true;
            BindService.LocalBinder binder = (BindService.LocalBinder) service;
            bindService = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isConnect = false;
        }
    };



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn:
                if (bindService != null) {
                    bindService.testThreadMethod();
                }
                break;
            case R.id.bind:
                Intent intent = new Intent(OtherActivity.this, BindService.class);
                bindService(intent,connection, Context.BIND_AUTO_CREATE);
                break;
            case R.id.stop:
                Intent intent2 = new Intent(OtherActivity.this, BindService.class);
                stopService(intent2);
                break;
            case R.id.unbind:
                unbindService(connection);
                break;
        }
    }
}
