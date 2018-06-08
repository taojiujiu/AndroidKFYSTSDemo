package com.lily.androidkfysts.service.messenger;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

public class MessengerActvity extends Activity {
    private Messenger messengerService;
    private Messenger replyMessenger = new Messenger(new replyMessengerHandler());

    private static class replyMessengerHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MessengerService.MESSAGE_FROM_SERVIEC:
                    Log.e("tao","receive msg from service : \n" + msg.getData().getString("reply"));

                default:
                super.handleMessage(msg);
                break;
            }
        }
    }


    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            messengerService = new Messenger(service);
            Message message = Message.obtain(null,MessengerService.MESSAGE_FROM_CLIENT);
            Bundle data = new Bundle();
            data.putString("msg","hello world, this is come from client message");
            message.setData(data);
            // 设置 客户端 交互的 messenger
            message.replyTo = replyMessenger;
            try {
                messengerService.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(MessengerActvity.this,MessengerService.class);
        bindService(intent,connection, Context.BIND_AUTO_CREATE);
    }
}
