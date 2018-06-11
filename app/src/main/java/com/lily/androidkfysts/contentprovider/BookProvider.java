package com.lily.androidkfysts.contentprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

public class BookProvider extends ContentProvider {

    private static final String TAG = "BookProvider";
    private static final String AUTHORITY = "com.lily.androidkfysts.book.provider";
    // 设置 URI
    public static final Uri BOOK_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/book");
    public static final Uri USER_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/user");
    // 设置 URI DODE
    public static final int BOOK_URI_CODE = 1;
    public static final int USER_URI_CODE = 2;

    // 通过 UriMatcher 关联相应的 URI URI_CODE ,是为了更方便的通过 URI_CODE 区分用户请求的 URI

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, "book", BOOK_URI_CODE);
        uriMatcher.addURI(AUTHORITY, "user", USER_URI_CODE);
    }


    private Context context;
    private SQLiteDatabase database;

    @Override
    public boolean onCreate() {
        Log.e("tao", "on create ,current thread is " + Thread.currentThread().getName());
        context = getContext();
        initProviderData();
        return true;
    }

    private void initProviderData() {
        database = new DbOpenHelper(context).getWritableDatabase();
        database.execSQL("delete from " + DbOpenHelper.BOOK_TABLE_NAME);
        database.execSQL("delete from " + DbOpenHelper.USER_TABLE_NAME);
        database.execSQL("insert into book values(3,'Andorid');");
        database.execSQL("insert into book values(4,'Ios');");
        database.execSQL("insert into book values(5,'HTML5');");
        database.execSQL("insert into user values(1,'lily',1);");
        database.execSQL("insert into user values(2,'tao',1);");
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Log.e("tao", "query, current thread : " + Thread.currentThread().getName());
        String table = getTableNames(uri);
        if (table == null) {
            throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        return database.query(table, projection, selection, selectionArgs, null, null, sortOrder, null);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        Log.e("tao", "getType");
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Log.e("tao", "insert");
        String table = getTableNames(uri);
        if (table == null) {
            throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        database.insert(table, null, values);
        context.getContentResolver().notifyChange(uri, null);
        return uri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.e("tao", "delete");
        String table = getTableNames(uri);
        if (table == null) {
            throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        int count = database.delete(table, selection, selectionArgs);
        if (count > 0) {
            context.getContentResolver().notifyChange(uri, null);
        }
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.e("tao", "update");
        String table = getTableNames(uri);
        if (table == null) {
            throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        int row = database.update(table, values, selection, selectionArgs);
        if (row > 0){
            context.getContentResolver().notifyChange(uri,null);
        }
        return row;
    }

    private String getTableNames(Uri uri) {
        String tableName = null;
        switch (uriMatcher.match(uri)) {
            case BOOK_URI_CODE:
                tableName = DbOpenHelper.BOOK_TABLE_NAME;
                break;
            case USER_URI_CODE:
                tableName = DbOpenHelper.USER_TABLE_NAME;
                break;
        }
        return tableName;
    }
}
