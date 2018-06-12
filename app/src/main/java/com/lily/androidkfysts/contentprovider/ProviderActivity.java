package com.lily.androidkfysts.contentprovider;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.lily.androidkfysts.R;
import com.lily.androidkfysts.ipc.Book;

public class ProviderActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.avtivty_provider);

//        Uri uri = Uri.parse("content://com.lily.androidkfysts.book.provider");
//        getContentResolver().query(uri,null,null,null,null);
//        getContentResolver().query(uri,null,null,null,null);
//        getContentResolver().query(uri,null,null,null,null);
        Uri bookUri = Uri.parse("content://com.lily.androidkfysts.book.provider/book");
        ContentValues values = new ContentValues();
        values.put("id", 7);
        values.put("name", "Insert Book");
        getContentResolver().insert(bookUri, values);
        Cursor bookCusor = getContentResolver().query(bookUri, new String[]{"id", "name"}, null, null, null);
        while (bookCusor.moveToNext()) {
            Book book = new Book();
            book.bookId = bookCusor.getInt(0);
            book.bookName = bookCusor.getString(1);
            Log.e("tao", "ProviderActivity query book : " + book.toString());
        }
        bookCusor.close();

        Uri userUri = Uri.parse("content://com.lily.androidkfysts.book.provider/user");
        Cursor userCursor = getContentResolver().query(userUri, new String[]{"id", "name", "sex"}, null, null, null);
        while (userCursor.moveToNext()) {
            User user = new User();
            user.id = userCursor.getInt(0);
            user.name = userCursor.getString(1);
            user.sex = userCursor.getInt(2);
            Log.e("tao","ProviderActivity query user :"+ user.toString());
        }
        userCursor.close();
    }
}
