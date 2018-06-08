package com.lily.androidkfysts.service.messenger;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

public class MessengerService extends Service {
    public static final int MESSAGE_FROM_CLIENT = 1;
    public static final int MESSAGE_FROM_SERVIEC = 2;

    private static String TAG = "MessengerService";

    private static class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_FROM_CLIENT:
                    Log.e("tao", TAG + " FORM CLIENT : " + msg.getData().getString("msg"));
                    Messenger clientMessenger = msg.replyTo;
                    Message relpyMessage = Message.obtain(null,MESSAGE_FROM_SERVIEC);
                    Bundle data = new Bundle();
                    data.putString("reply","ok,I'am service and I received your message");
                    relpyMessage.setData(data);
                    try {
                        clientMessenger.send(relpyMessage);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    }

    private final Messenger messenger = new Messenger(new MessengerHandler());

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }

}
