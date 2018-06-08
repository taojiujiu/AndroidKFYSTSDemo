package com.lily.androidkfysts.service.bind;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.lily.androidkfysts.R;

public class BindServiceActivity extends Activity {
    boolean isConnect;
    BindService bindService;
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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button btn = findViewById(R.id.btn);
        Button stop = findViewById(R.id.stop);
        Button unbind = findViewById(R.id.unbind);
        Button other = findViewById(R.id.other);
        Button bind = findViewById(R.id.bind);

        final Button start = findViewById(R.id.start);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnect) {
//                    int i = bindService.testMethod();
                    bindService.testThreadMethod();
                    Log.e("tao", "  setBackgroundColor ");
                    btn.setBackgroundColor(Color.RED);
                }
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BindServiceActivity.this, BindService.class);
                startService(intent);
            }
        });
        bind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BindServiceActivity.this, BindService.class);
                bindService(intent, connection, Context.BIND_AUTO_CREATE);
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BindServiceActivity.this, BindService.class);
                stopService(intent);
            }
        });
        unbind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isConnect = false;

                unbindService(connection);
            }
        });

        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BindServiceActivity.this, OtherActivity.class);
                startActivity(intent);
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isConnect) {
            unbindService(connection);
            isConnect = false;
        }
    }
}
