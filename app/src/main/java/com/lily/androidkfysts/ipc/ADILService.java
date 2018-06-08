package com.lily.androidkfysts.ipc;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.lily.androidkfysts.IOnNewBookReceivedListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class ADILService extends Service {

    public final String TAG = this.getClass().getSimpleName();
    private AtomicBoolean isServiceDestory = new AtomicBoolean(false);
    private CopyOnWriteArrayList<Book> books = new CopyOnWriteArrayList<>();

    //    private CopyOnWriteArrayList<IOnNewBookReceivedListener> listenerLists = new CopyOnWriteArrayList<>();
    private RemoteCallbackList<IOnNewBookReceivedListener> listenerLists = new RemoteCallbackList<>();

    private final IBookManager.Stub iBookManager = new IBookManager.Stub() {
        @Override
        public List<Book> getBookList() throws RemoteException {
            try {
                // 模拟超市操作
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (this) {
                Log.e("tao", TAG + " invoking getbookLists() method , now this is " + books.size());
                if (books != null) {
                    return books;
                }
                return new ArrayList<>();
            }
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            synchronized (this) {
                if (books == null) {
                    books = new CopyOnWriteArrayList<>();
                }
                if (book == null) {
                    Log.e("tao", TAG + " book is null");
                    book = new Book(1, "Android");
                }
                if (!books.contains(book)) {
                    books.add(book);
                    onNewBookArrived(book);
                }
                Log.e("tao", TAG + " invoking addBook() method , now this is " + books.size());

            }

        }

        @Override
        public void registerListener(IOnNewBookReceivedListener listener) throws RemoteException {
//            CopyOnWriteArrayList<IOnNewBookReceivedListener> 方法
//            if(!listenerLists.contains(listener)){
//                listenerLists.add(listener);
//            }else {
//                Log.e("tao","listener already exists");
//            }
//            Log.e("tao","register listener size "+ listenerLists.size());

//            RemoteCallbackList<IOnNewBookReceivedListener> 方法
            listenerLists.register(listener);

            // beginBroadcast() 和 finishBroadcast 必须配对使用否则会报错
            Log.e("tao", "register listener size " + listenerLists.beginBroadcast() );
            listenerLists.finishBroadcast();



        }

        @Override
        public void unregisterListenr(IOnNewBookReceivedListener listener) throws RemoteException {
//            CopyOnWriteArrayList<IOnNewBookReceivedListener> 方法
//            if (listenerLists.contains(listener)) {
//                listenerLists.remove(listener);
//                Log.e("tao", "unregister lists is success");
//            } else {
//                Log.e("tao", "unregister lists is failed, not found this listener");
//            }
//            Log.e("tao", "register listener size " + listenerLists.size());

//            RemoteCallbackList<IOnNewBookReceivedListener> 方法
            listenerLists.unregister(listener);
                        Log.e("tao", "unregister listener size " + listenerLists.beginBroadcast());

            listenerLists.finishBroadcast();
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return iBookManager;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        books.add(new Book(1, "Android"));
        books.add(new Book(2, "Ios"));
        new Thread(new ServiceWork()).start();
    }

    private class ServiceWork implements Runnable {
        @Override
        public void run() {
            while (!isServiceDestory.get()) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int bookId = books.size() + 1;
                Book newBook = new Book(bookId, "New Book# " + bookId);
                try {
                    onNewBookArrived(newBook);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void onNewBookArrived(Book newBook) throws RemoteException {
//            CopyOnWriteArrayList<IOnNewBookReceivedListener> 方法
//        books.add(newBook);
//        Log.e("tao", "onNewBookArrived ，notification listeners :" + listenerLists.size());
//        for (int i = 0; i < listenerLists.size(); i++) {
//            IOnNewBookReceivedListener listener = listenerLists.get(i);
//            Log.e("tao", "onNewBookArrived ，notification  :" + listener);
//            listener.onNewBookArrived(newBook);
//        }

//            RemoteCallbackList<IOnNewBookReceivedListener> 方法
        final  int N = listenerLists.beginBroadcast();
        for(int i = 0 ; i < N; i ++){
            IOnNewBookReceivedListener l = listenerLists.getBroadcastItem(i);
            if(l != null){
                l.onNewBookArrived(newBook);
            }
        }
        listenerLists.finishBroadcast();

    }
}
