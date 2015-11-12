package com.example.anandchandrasekar.wardrobeadvisor;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "WardrobeDB.db";
    private Context context;
    private SQLiteDatabase database;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        FilterTableHelper.createTable(db, context);
        this.database = db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);
    }

    public ArrayList<Filter> getFiltersOfKind(String kind) {
        Log.d("", "getting filter of kind "+ kind);
        return FilterTableHelper.getFiltersOfKind(this.database, kind);
    }
}