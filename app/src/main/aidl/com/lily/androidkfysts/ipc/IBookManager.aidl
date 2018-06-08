// IBookManagerTest.aidl
package com.lily.androidkfysts.ipc;

// 用到的自定义类，必须显示的在这里引用，即使处在同一包下，这个是 aidl 语言规定
import com.lily.androidkfysts.ipc.Book;
// Declare any non-default types here with import statements
import com.lily.androidkfysts.IOnNewBookReceivedListener;
// 这个定义需要实现的方法， SDK 会在定义无误的情况下自动生成相关的 java 代码
interface IBookManager {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */

            List<Book> getBookList();
            void addBook(in Book book);
            void registerListener(IOnNewBookReceivedListener listener);
            void unregisterListenr(IOnNewBookReceivedListener listener);

}
