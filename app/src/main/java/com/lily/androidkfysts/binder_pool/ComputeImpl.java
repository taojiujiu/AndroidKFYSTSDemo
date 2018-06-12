package com.lily.androidkfysts.binder_pool;

import android.os.RemoteException;

public class ComputeImpl extends IConpute.Stub{
    @Override
    public int addInt(int a, int b) throws RemoteException {
        return a+b;
    }
}
