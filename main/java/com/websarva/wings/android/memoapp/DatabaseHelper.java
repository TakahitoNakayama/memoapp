package com.websarva.wings.android.memoapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME="MemoData";
    private static final int DATABASE_VERSION=1;

    public DatabaseHelper(Context context){
        super(context.getApplicationContext(),DATABASE_NAME,null,DATABASE_VERSION );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder builder=new StringBuilder();
        builder.append
                ("CREATE TABLE memodata (_id INTEGER PRIMARY KEY,title TEXT,contents TEXT)");
        String sql=builder.toString();
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
