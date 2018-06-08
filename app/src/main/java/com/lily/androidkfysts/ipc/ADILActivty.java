package com.lily.androidkfysts.ipc;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.lily.androidkfysts.IOnNewBookReceivedListener;
import com.lily.androidkfysts.R;

public class ADILActivty extends Activity implements View.OnClickListener {
    private IBookManager iBookManager = null;
    private boolean isConnect = false;
    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e("tao", "service connected " + getLocalClassName());
            iBookManager = IBookManager.Stub.asInterface(service);
            try {
                iBookManager.asBinder().linkToDeath(deathRecipient,0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            try {
                iBookManager.registerListener(iOnNewBookReceivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            isConnect = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isConnect = false;
            iBookManager = null;
            Log.e("tao", "………………………………………………………………onServiceDisconnected");

        }
    };
    public static final int MESSAGE_NEW_BOOK_ARRIVED = 1;

    private Handler handler = new Handler(Looper.getMainLooper()) {


        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_NEW_BOOK_ARRIVED:
                    Log.e("tao", "Client received notification *************** new book is " + msg.obj);

                    break;

                default:
                    super.handleMessage(msg);
                    break;
            }


        }
    };

    private IOnNewBookReceivedListener iOnNewBookReceivedListener = new IOnNewBookReceivedListener.Stub() {
        @Override
        public void onNewBookArrived(Book newBook) throws RemoteException {
            handler.obtainMessage(MESSAGE_NEW_BOOK_ARRIVED, newBook).sendToTarget();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aidl);
        Button bind = findViewById(R.id.bind);
        Button unBind = findViewById(R.id.unbind);
        Button addBook = findViewById(R.id.add_book);
        Button getList = findViewById(R.id.get_lists);
        bind.setOnClickListener(this);
        unBind.setOnClickListener(this);
        addBook.setOnClickListener(this);
        getList.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bind:
                Intent intent = new Intent(ADILActivty.this, ADILService.class);
//                Intent intent = new Intent();
//                intent.setAction("com.lily.test");
//                bindService(new Intent(Utils.createExplicitFromImplicitIntent(this, intent)), serviceConnection, Context.BIND_AUTO_CREATE);
                bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
                break;
            case R.id.unbind:
                unbind();
                break;
            case R.id.get_lists:
                // 服务端如果是超时的操作就应该开启新的线程去工作
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (iBookManager != null) {
                            try {
                                Log.e("tao", "lists is :" + iBookManager.getBookList().toString());
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();

                break;
            case R.id.add_book:
                if (iBookManager != null) {
                    try {
                        iBookManager.addBook(new Book(2, "JAVA"));
                        Log.e("tao", "add a book ,list size is " + iBookManager.getBookList().size());
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    @Override
    protected void onStop() {

        unbind();
        super.onStop();
    }

    private void unbind() {
        
        if(iBookManager !=null && iBookManager.asBinder().isBinderAlive()){
            try {
                Log.e("tao", "unregister listener  " + iOnNewBookReceivedListener);

                //
                iBookManager.unregisterListenr(iOnNewBookReceivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        unbindService(serviceConnection);
    }

    private  IBinder.DeathRecipient deathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            if(iBookManager ==null)
                return;
            iBookManager.asBinder().unlinkToDeath(deathRecipient,0);
            iBookManager = null;
            Log.e("tao", "@@@@@@@@@@@         binderDied       ");
        }
    };
}
