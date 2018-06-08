package com.lily.androidkfysts.ipc.handwrite;

import android.os.Binder;
import android.os.IInterface;

public interface TestInterface extends IInterface {

    public static final class Stub extends Binder implements TestInterface{
        @Override
        public android.os.IBinder asBinder() {
            return this;
        }
    }
}
