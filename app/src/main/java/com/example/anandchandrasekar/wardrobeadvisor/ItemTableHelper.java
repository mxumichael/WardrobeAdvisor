package com.example.anandchandrasekar.wardrobeadvisor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by anandchandrasekar on 11/8/15.
 */
public class ItemTableHelper {

    private static final String TABLE_NAME = "items";
    private static final String COLUMN_ID = "item_id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_DESC = "description";


    public static void createTable(SQLiteDatabase database,  Context context) {
        database.execSQL(
                "create table " + TABLE_NAME +
                        " (" + COLUMN_ID + " integer primary key, " +
                        COLUMN_NAME + " text, " + COLUMN_DESC + " text)"
        );
        loadDefaultFilters(database, context);
        Log.d("", "creating table");
    }

    public static void insertItem(SQLiteDatabase database, Item item) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID, item.getId());
        contentValues.put(COLUMN_NAME, item.getName());
        contentValues.put(COLUMN_DESC, item.getDescription());
        database.insert(TABLE_NAME, null, contentValues);
    }

    public static int numberOfRows(SQLiteDatabase db){
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        return numRows;
    }

    public static boolean updateItem (SQLiteDatabase db, Filter updatedFilter) {

        return true;
    }

    public static Integer deleteItem (SQLiteDatabase db, Integer id) {
        return db.delete(TABLE_NAME, COLUMN_ID + " = ? ", new String[] { Integer.toString(id) });
    }

    private static ArrayList<Item> getItemsWithQuery(SQLiteDatabase database, String query) {
        Cursor res =  database.rawQuery(query, null);
        res.moveToFirst();

        ArrayList<Item> items = new ArrayList<Item>();
        while(res.isAfterLast() == false){
            Item newItem = new Item(res.getInt(res.getColumnIndex(COLUMN_ID)),
                    res.getString(res.getColumnIndex(COLUMN_NAME)),
                    res.getString(res.getColumnIndex(COLUMN_DESC)));
            items.add(newItem);
            res.moveToNext();
        }
        return items;
    }

    public static ArrayList<Item> getAllItems(SQLiteDatabase database) {
        return getItemsWithQuery(database, "select * from " + TABLE_NAME);
    }

    public static ArrayList<Item> getItemById(SQLiteDatabase database, int id) {
        return getItemsWithQuery(database, "select * from " + TABLE_NAME + " where " + COLUMN_ID + " = " + id);
    }

    public static void loadDefaultFilters(SQLiteDatabase db, Context context) {
        insertItem(db, new Item(1, "Red Shirt", "abcdef"));
        insertItem(db, new Item(2, "Blue Sweater", ""));
        insertItem(db, new Item(3, "Green", ""));
        insertItem(db, new Item(4, "Shirt", ""));
        insertItem(db, new Item(5, "Shorts", ""));
        insertItem(db, new Item(6, "Blazer", ""));
        insertItem(db, new Item(7, "Sunny", ""));
        insertItem(db, new Item(8, "Rainy", ""));
        insertItem(db, new Item(9, "Cold", ""));
    }
}