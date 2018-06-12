package com.lily.androidkfysts.binder_pool;

import android.app.Activity;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

public class BinderPoolActivity extends Activity{
    ISecurityCenter securityCenter ;
    IConpute iConpute;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Thread(){
            @Override
            public void run() {
                super.run();
                doWork();
            }
        }.start();
// 耗时操作，如果不放在线程中， 则  BinderPoolManager 中的  CountDownLatch 将一直处于阻塞状态
//        doWork();

    }

    private void doWork() {
        BinderPoolManager poolManager = BinderPoolManager.getInstance(this);
        IBinder securityIBinder = poolManager.queryBinder(BinderPoolManager.BINDER_SECUTIRY_CENTER);
        securityCenter = SecurityCenterImpl.asInterface(securityIBinder);
        Log.e("tao","visit ISecurityBinder");
        String msg = "Hello -安卓";
        System.out.println("contetn : " + msg);
        try {
            String password = securityCenter.encrypt(msg);
            System.out.println("encrypt : " + password);
            System.out.println("decrypt : " + securityCenter.decrypt(password));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        Log.e("tao","visit Compute");
        IBinder computeBinder = poolManager.queryBinder(BinderPoolManager.BINDER_COMPUTE);
        iConpute = ComputeImpl.asInterface(computeBinder);
        try {
            System.out.println("3+6 : " + iConpute.addInt(3,6));
        } catch (RemoteException e) {
            e.printStackTrace();
        }


    }
}
