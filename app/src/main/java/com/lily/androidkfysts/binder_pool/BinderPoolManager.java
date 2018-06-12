package com.lily.androidkfysts.binder_pool;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.lily.androidkfysts.IBinderPool;

import java.util.concurrent.CountDownLatch;

public class BinderPoolManager {


    private Context context;
    @SuppressLint("StaticFieldLeak")
    private static volatile BinderPoolManager INSTANCE;
    private IBinderPool iBinderPool;
    private CountDownLatch countDownLatch;
    public final static int BINDER_SECUTIRY_CENTER = 1;
    public static final int BINDER_COMPUTE = 2;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e("tao", "binder onServiceConnected");

            iBinderPool = IBinderPool.Stub.asInterface(service);
            try {
                iBinderPool.asBinder().linkToDeath(deathRecipient, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            countDownLatch.countDown();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private IBinder.DeathRecipient deathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            iBinderPool.asBinder().unlinkToDeath(deathRecipient, 0);
            iBinderPool = null;
            connectBinderPoolService();
        }
    };

    private BinderPoolManager(Context context) {
        this.context = context;
        Log.e("tao", "binder BinderPoolManager");
        connectBinderPoolService();
    }

    public static BinderPoolManager getInstance(Context context) {
        synchronized (BinderPoolManager.class) {
            if (INSTANCE == null) {
                INSTANCE = new BinderPoolManager(context);
            }
        }
        return INSTANCE;
    }


    private synchronized void connectBinderPoolService() {
        Log.e("tao", "binder connectBinderPoolService");

        countDownLatch = new CountDownLatch(1);
        Intent intent = new Intent(context, BinderPoolService.class);
        Log.e("tao", "binder connectBinderPoolService");

        context.bindService(intent, connection, Context.BIND_AUTO_CREATE);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public IBinder queryBinder(int binderCode) {
        IBinder binder = null;
        if (iBinderPool != null) {
            try {
                binder = iBinderPool.queryBinder(binderCode);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return binder;

    }


    public static class BinderPoolImpl extends IBinderPool.Stub {
        @Override
        public IBinder queryBinder(int binderCode) throws RemoteException {
            IBinder binder = null;
            switch (binderCode) {
                case BINDER_SECUTIRY_CENTER:
                    binder = new SecurityCenterImpl();
                    break;
                case BINDER_COMPUTE:
                    binder = new ComputeImpl();
                    break;
                default:
                    break;
            }
            return binder;
        }
    }

}
