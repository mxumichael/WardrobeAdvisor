package com.example.anandchandrasekar.wardrobeadvisor;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

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
        ItemTableHelper.createTable(db, context);
        ItemFilterTableHelper.createTable(db, context);
        this.database = db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);
    }

    public ArrayList<ItemFilter> getFiltersOfKind(String kind) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("", "getting filter of kind " + kind);
        return ItemFilterTableHelper.getFiltersOfKind(db, kind);
    }

    public Item getItemById(Integer id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return ItemTableHelper.getItemById(db, id);
    }

    public ArrayList<Item> getItemsByState(Integer state) {
        SQLiteDatabase db = this.getReadableDatabase();
        return ItemTableHelper.getItemsByState(db, state);
    }

    public ArrayList<Item> getItemListForFilterIdListAndState(Integer state, List<String> filterIds) {
        SQLiteDatabase db = this.getReadableDatabase();
        return ItemTableHelper.getItemListForFilterIdListAndState(db, filterIds, state);
    }

    public boolean updateItemState(Integer id, Integer new_state) {
        SQLiteDatabase db = this.getWritableDatabase();
        return ItemTableHelper.updateItemState(db, id, new_state);
    }
    public int updateItemsFromStateToState(Integer original_state, Integer new_state) {
        SQLiteDatabase db = this.getWritableDatabase();
        return ItemTableHelper.updateItemsFromStateToState(db, original_state, new_state);
    }
}
