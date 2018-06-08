package com.lily.androidkfysts.service.start;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.lily.androidkfysts.ipc.Book;

public class IntentServiceDemo extends IntentService {

    public IntentServiceDemo() {
        super("IntentServiceDemo");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.e("tao","  IntentServiceDemo   onHandleIntent");
        try {
            Book book = intent.getParcelableExtra("book");
            Log.e("tao",book.toString());
            Thread.sleep(5000);
            Log.e("tao","  IntentServiceDemo   Done");
            stopSelf();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("tao","  IntentServiceDemo   onDestroy");

    }
}
