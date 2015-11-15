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
public class FilterTableHelper {

    private static final String TABLE_NAME = "item_filter";
    private static final String COLUMN_ITEM_ID = "item_id";
    private static final String COLUMN_FILTER_ID = "filter_id";
    private static final String COLUMN_FILTER_NAME = "filter_name";
    private static final String COLUMN_FILTER_KIND = "filter_kind";
    private static final String COLUMN_FILTER_IMAGE_PATH = "filter_image_path";


    public static void createTable(SQLiteDatabase database, Context context) {
        database.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ITEM_ID + " INTEGER REFERENCES Item, " +
                COLUMN_FILTER_KIND + " VARCHAR(255) NOT NULL, " +
                COLUMN_FILTER_ID + " INTEGER NOT NULL, " +
                COLUMN_FILTER_NAME + " VARCHAR(255) NOT NULL) ");
        loadDefaultFilters(database, context);
        Log.d("", "creating table");
    }

    public static void insertFilter(SQLiteDatabase database, Filter filter) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ITEM_ID, filter.getItemId());
        contentValues.put(COLUMN_FILTER_KIND, filter.getFilterKind());
        contentValues.put(COLUMN_FILTER_ID, filter.getId());
        contentValues.put(COLUMN_FILTER_NAME, filter.getFilterName());
//        contentValues.put(COLUMN_FILTER_IMAGE_PATH, filter.getFilterImagePath());
        database.insert(TABLE_NAME, null, contentValues);
    }

    public static int numberOfRows(SQLiteDatabase db) {
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        return numRows;
    }

    public static boolean updateFilter(SQLiteDatabase db, Filter updatedFilter) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ITEM_ID, updatedFilter.getItemId());
        contentValues.put(COLUMN_FILTER_KIND, updatedFilter.getFilterKind());
        contentValues.put(COLUMN_FILTER_ID, updatedFilter.getId());
        contentValues.put(COLUMN_FILTER_NAME, updatedFilter.getFilterName());
//        contentValues.put(COLUMN_FILTER_IMAGE_PATH, updatedFilter.getFilterImagePath());
        db.update(TABLE_NAME, contentValues, COLUMN_FILTER_ID + " = ? ", new String[]{Integer.toString(updatedFilter.getId())});

        return true;
    }

    public static Integer deleteFilter(SQLiteDatabase db, Integer id) {
        return db.delete(TABLE_NAME, COLUMN_FILTER_ID + " = ? ", new String[]{Integer.toString(id)});
    }

    private static ArrayList<Filter> getFiltersWithQuery(SQLiteDatabase database, String query) {
        Cursor res = database.rawQuery(query, null);
        res.moveToFirst();

        ArrayList<Filter> filters = new ArrayList<Filter>();
        while (res.isAfterLast() == false) {
            Filter currFilter = new Filter(res.getInt(res.getColumnIndex(COLUMN_FILTER_ID)),
                    res.getString(res.getColumnIndex(COLUMN_FILTER_NAME)),
                    res.getString(res.getColumnIndex(COLUMN_FILTER_KIND)),
                    res.getString(res.getColumnIndex(COLUMN_FILTER_IMAGE_PATH)),
                    res.getInt(res.getColumnIndex(COLUMN_ITEM_ID)));
            filters.add(currFilter);
            res.moveToNext();
        }
        return filters;
    }

    public static ArrayList<Filter> getAllFilters(SQLiteDatabase database) {
        return getFiltersWithQuery(database, "select * from " + TABLE_NAME);
    }

    public static ArrayList<Filter> getFiltersWithId(SQLiteDatabase database, int id) {
        return getFiltersWithQuery(database, "select * from " + TABLE_NAME + " where " + COLUMN_FILTER_ID + " = " + id);
    }

    public static ArrayList<Filter> getFiltersOfKind(SQLiteDatabase database, String kind) {
        return getFiltersWithQuery(database, "select * from " + TABLE_NAME + " where " + COLUMN_FILTER_KIND + " = '" + kind + "'");
    }

    public static void loadDefaultFilters(SQLiteDatabase db, Context context) {
        insertFilter(db, new Filter(1, "Red", context.getResources().getString(R.string.filter_kind_color), null, 1));
        insertFilter(db, new Filter(2, "Blue", context.getResources().getString(R.string.filter_kind_color), null, 1));
        insertFilter(db, new Filter(3, "Green", context.getResources().getString(R.string.filter_kind_color), null, 1));
        insertFilter(db, new Filter(4, "Shirt", context.getResources().getString(R.string.filter_kind_type), null, 1));
        insertFilter(db, new Filter(5, "Shorts", context.getResources().getString(R.string.filter_kind_type), null, 1));
        insertFilter(db, new Filter(6, "Blazer", context.getResources().getString(R.string.filter_kind_type), null, 1));
        insertFilter(db, new Filter(7, "Sunny", context.getResources().getString(R.string.filter_kind_weather), null, 1));
        insertFilter(db, new Filter(8, "Rainy", context.getResources().getString(R.string.filter_kind_weather), null, 1));
        insertFilter(db, new Filter(9, "Cold", context.getResources().getString(R.string.filter_kind_weather), null, 1));
    }
}