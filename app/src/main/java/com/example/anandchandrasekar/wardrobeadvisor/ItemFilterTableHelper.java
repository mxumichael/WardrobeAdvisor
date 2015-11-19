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
public class ItemFilterTableHelper {

    private static final String TABLE_NAME = "item_filter";
    private static final String COLUMN_ITEM_ID = "item_id";
    private static final String COLUMN_FILTER_ID = "filter_id";
    private static final String COLUMN_FILTER_NAME = "filter_name";
    private static final String COLUMN_FILTER_KIND = "filter_kind";
    private static final String COLUMN_FILTER_IMAGE_PATH = "filter_image_path";

    private static final String FILTER_COLOR = "filter_kind_color";
    private static final String FILTER_STATE = "filter_kind_state";
    private static final String FILTER_TYPE = "filter_kind_type";
    private static final String FILTER_WEATHER = "filter_kind_weather";

    private static int filterCount = 0;

    public static void createTable(SQLiteDatabase database, Context context) {
        database.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ITEM_ID + " INTEGER REFERENCES Item, " +
                COLUMN_FILTER_KIND + " VARCHAR(255) NOT NULL, " +
                COLUMN_FILTER_ID + " INTEGER NOT NULL, " +
                COLUMN_FILTER_NAME + " VARCHAR(255) NOT NULL) ");
        loadDefaultFilters(database, context);
        Log.d("", "creating table");
    }

    public static void insertFilter(SQLiteDatabase database, ItemFilter itemFilter) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ITEM_ID, itemFilter.getItemId());
        contentValues.put(COLUMN_FILTER_KIND, itemFilter.getFilterKind());
        contentValues.put(COLUMN_FILTER_ID, itemFilter.getId());
        contentValues.put(COLUMN_FILTER_NAME, itemFilter.getFilterName());
//        contentValues.put(COLUMN_FILTER_IMAGE_PATH, itemFilter.getFilterImagePath());
        database.insert(TABLE_NAME, null, contentValues);
    }

    public static int numberOfRows(SQLiteDatabase db) {
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        return numRows;
    }

    public static boolean updateFilter(SQLiteDatabase db, ItemFilter updatedItemFilter) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ITEM_ID, updatedItemFilter.getItemId());
        contentValues.put(COLUMN_FILTER_KIND, updatedItemFilter.getFilterKind());
        contentValues.put(COLUMN_FILTER_ID, updatedItemFilter.getId());
        contentValues.put(COLUMN_FILTER_NAME, updatedItemFilter.getFilterName());
//        contentValues.put(COLUMN_FILTER_IMAGE_PATH, updatedItemFilter.getFilterImagePath());
        db.update(TABLE_NAME, contentValues, COLUMN_FILTER_ID + " = ? ", new String[]{Integer.toString(updatedItemFilter.getId())});

        return true;
    }

    public static Integer deleteFilter(SQLiteDatabase db, Integer id) {
        return db.delete(TABLE_NAME, COLUMN_FILTER_ID + " = ? ", new String[]{Integer.toString(id)});
    }

    private static ArrayList<ItemFilter> getFiltersWithQuery(SQLiteDatabase database, String query) {
        Cursor res = database.rawQuery(query, null);
        res.moveToFirst();

        ArrayList<ItemFilter> itemFilters = new ArrayList<ItemFilter>();
        while (res.isAfterLast() == false) {
            int anInt = res.getInt(res.getColumnIndex(COLUMN_FILTER_ID));
            String string = res.getString(res.getColumnIndex(COLUMN_FILTER_NAME));
            String string1 = res.getString(res.getColumnIndex(COLUMN_FILTER_KIND));
            String string2 = null;
            int anInt1 = res.getInt(res.getColumnIndex(COLUMN_ITEM_ID));
            ItemFilter currItemFilter = new ItemFilter(anInt,
                    string,
                    string1,
                    string2,
                    anInt1);
            itemFilters.add(currItemFilter);
            res.moveToNext();
        }
        return itemFilters;
    }

    private static int getFilterIdForQuery(SQLiteDatabase database, String query) {
        Cursor res = database.rawQuery(query, null);
        res.moveToFirst();

        if(res.isAfterLast() == true) {
            return -1;
        }
        else {
            return res.getInt(res.getColumnIndex(COLUMN_FILTER_ID));
        }
    }

    public static ArrayList<ItemFilter> getAllFilters(SQLiteDatabase database) {
        return getFiltersWithQuery(database, "select * from " + TABLE_NAME);
    }

    public static ArrayList<ItemFilter> getFiltersWithId(SQLiteDatabase database, int id) {
        return getFiltersWithQuery(database, "select * from " + TABLE_NAME + " where " + COLUMN_FILTER_ID + " = " + id);
    }

    public static ArrayList<ItemFilter> getFiltersOfKind(SQLiteDatabase database, String kind) {
        return getFiltersWithQuery(database, "select * from " + TABLE_NAME + " where " + COLUMN_FILTER_KIND + " = '" + kind + "'"
                + " GROUP BY " + COLUMN_FILTER_ID);
    }

    public static ItemFilter getItemFilterForFilterId(SQLiteDatabase database, String filterId) {
        ArrayList<ItemFilter> filtersWithQuery = getFiltersWithQuery(database, "SELECT * FROM ITEM_FILTER\n" +
                "WHERE FILTER_ID = '" + filterId + "'\n" +
                "GROUP BY FILTER_ID;");
        if (filtersWithQuery != null && filtersWithQuery.size() > 0) {
            return filtersWithQuery.get(0);
        } else {
            return null;
        }
    }

    public static void loadDefaultFilters(SQLiteDatabase db, Context context) {
        ArrayList<Item> allItems = ItemTableHelper.getAllItems(db);
        for(int i = 0; i < allItems.size(); i++) {
            Item item = allItems.get(i);

            String color = item.getColor().toLowerCase();
            String queryString = "SELECT * FROM " + TABLE_NAME + " WHERE " +
                                    COLUMN_FILTER_KIND + " = '" + FILTER_COLOR + "' AND " +
                                    COLUMN_FILTER_NAME + " = '" + color + "'";
            int filterId = getFilterIdForQuery(db, queryString);
            if(filterId == -1)
                filterId = ++filterCount;
            insertFilter(db, new ItemFilter(filterId, color, FILTER_COLOR, null, item.getId()));

            String type = item.getType().toLowerCase();
            queryString = "SELECT * FROM " + TABLE_NAME + " WHERE " +
                    COLUMN_FILTER_KIND + " = '" + FILTER_TYPE + "' AND " +
                    COLUMN_FILTER_NAME + " = '" + type + "'";
            filterId = getFilterIdForQuery(db, queryString);
            if(filterId == -1)
                filterId = ++filterCount;
            insertFilter(db, new ItemFilter(filterId, type, FILTER_TYPE, null, item.getId()));

            String weather = item.getWeather().toLowerCase();
            queryString = "SELECT * FROM " + TABLE_NAME + " WHERE " +
                    COLUMN_FILTER_KIND + " = '" + FILTER_WEATHER + "' AND " +
                    COLUMN_FILTER_NAME + " = '" + weather + "'";
            filterId = getFilterIdForQuery(db, queryString);
            if(filterId == -1)
                filterId = ++filterCount;
            insertFilter(db, new ItemFilter(filterId, weather, FILTER_WEATHER, null, item.getId()));

            String state = item.getState().toString();
            queryString = "SELECT * FROM " + TABLE_NAME + " WHERE " +
                    COLUMN_FILTER_KIND + " = '" + FILTER_STATE + "' AND " +
                    COLUMN_FILTER_NAME + " = '" + state +"'";
            filterId = getFilterIdForQuery(db, queryString);
            if(filterId == -1)
                filterId = ++filterCount;
            insertFilter(db, new ItemFilter(filterId, state, FILTER_STATE, null, item.getId()));
        }

//        insertFilter(db, new ItemFilter(1, "Red", context.getResources().getString(R.string.filter_kind_color), null, 1));
//        insertFilter(db, new ItemFilter(2, "Blue", context.getResources().getString(R.string.filter_kind_color), null, 1));
//        insertFilter(db, new ItemFilter(3, "Green", context.getResources().getString(R.string.filter_kind_color), null, 1));
//        insertFilter(db, new ItemFilter(4, "Shirt", context.getResources().getString(R.string.filter_kind_type), null, 1));
//        insertFilter(db, new ItemFilter(5, "Shorts", context.getResources().getString(R.string.filter_kind_type), null, 1));
//        insertFilter(db, new ItemFilter(6, "Blazer", context.getResources().getString(R.string.filter_kind_type), null, 1));
//        insertFilter(db, new ItemFilter(7, "Sunny", context.getResources().getString(R.string.filter_kind_weather), null, 1));
//        insertFilter(db, new ItemFilter(8, "Rainy", context.getResources().getString(R.string.filter_kind_weather), null, 1));
//        insertFilter(db, new ItemFilter(9, "Cold", context.getResources().getString(R.string.filter_kind_weather), null, 1));
    }
}