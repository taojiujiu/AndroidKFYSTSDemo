// IOnNewBookReceivedListener.aidl
package com.lily.androidkfysts;
import com.lily.androidkfysts.ipc.Book;
// Declare any non-default types here with import statements

interface IOnNewBookReceivedListener {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void onNewBookArrived(in Book newBook);
}
