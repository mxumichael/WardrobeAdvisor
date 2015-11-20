package com.example.anandchandrasekar.wardrobeadvisor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anandchandrasekar on 11/8/15.
 */
public class ItemTableHelper {

    private static final String TABLE_NAME = "Item";
    private static final String COLUMN_ID = "item_id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_DESC = "description";
    private static final String COLUMN_SIZE = "size";
    private static final String COLUMN_STATE = "state";
    private static final String COLUMN_IMAGE = "image";
    private static final String COLUMN_TYPE = "type";
    private static final String COLUMN_WEATHER = "weather";
    private static final String COLUMN_COLOR = "color";
    private static final String COLUMN_BRAND = "brand";


    public static void createTable(SQLiteDatabase database, Context context) {
        database.execSQL("    CREATE TABLE " + TABLE_NAME + "\n" +
                "    (\n" +
                COLUMN_ID + "          INTEGER PRIMARY KEY,\n" +
                COLUMN_NAME + "              VARCHAR(255),\n" +
                COLUMN_DESC + "      VARCHAR(255),\n" +
                COLUMN_SIZE + "              VARCHAR(255),\n" +
                COLUMN_STATE + "             INTEGER,\n" +
                COLUMN_IMAGE + "             VARCHAR(255),\n" +
                COLUMN_TYPE + "             VARCHAR(255),\n" +
                COLUMN_WEATHER + "             VARCHAR(255),\n" +
                COLUMN_COLOR + "             VARCHAR(255),\n" +
                COLUMN_BRAND + "             VARCHAR(255)\n" +
                "    )");
        loadDefaultItems(database, context);
        Log.d("", "creating table");
    }

    public static void insertItem(SQLiteDatabase database, Item item) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID, item.getId());
        contentValues.put(COLUMN_NAME, item.getName());
        contentValues.put(COLUMN_DESC, item.getDescription());
        contentValues.put(COLUMN_SIZE, item.getSize());
        contentValues.put(COLUMN_STATE, item.getState());
        contentValues.put(COLUMN_IMAGE, item.getImagePath());
        contentValues.put(COLUMN_TYPE, item.getType());
        contentValues.put(COLUMN_WEATHER, item.getWeather());
        contentValues.put(COLUMN_COLOR, item.getColor());
        contentValues.put(COLUMN_BRAND, item.getBrand());
        database.insert(TABLE_NAME, null, contentValues);
    }

    public static int numberOfRows(SQLiteDatabase db) {
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        return numRows;
    }

    public static boolean updateItem(SQLiteDatabase db, ItemFilter updatedItemFilter) {

        return true;
    }

    public static Integer deleteItem(SQLiteDatabase db, Integer id) {
        return db.delete(TABLE_NAME, COLUMN_ID + " = ? ", new String[]{Integer.toString(id)});
    }

    private static ArrayList<Item> getItemsWithQuery(SQLiteDatabase database, String query) {
        Cursor res = database.rawQuery(query, null);
        res.moveToFirst();

        ArrayList<Item> items = new ArrayList<Item>();
        while (res.isAfterLast() == false) {
            Item newItem = new Item(res.getInt(res.getColumnIndex(COLUMN_ID)),
                    res.getString(res.getColumnIndex(COLUMN_NAME)),
                    res.getString(res.getColumnIndex(COLUMN_TYPE)),
                    res.getString(res.getColumnIndex(COLUMN_COLOR)),
                    res.getString(res.getColumnIndex(COLUMN_SIZE)),
                    res.getString(res.getColumnIndex(COLUMN_BRAND)),
                    res.getString(res.getColumnIndex(COLUMN_WEATHER)),
                    res.getString(res.getColumnIndex(COLUMN_DESC)),
                    res.getString(res.getColumnIndex(COLUMN_IMAGE)),
                    res.getInt(res.getColumnIndex(COLUMN_STATE)));
            items.add(newItem);
            res.moveToNext();
        }
        return items;
    }

    public static ArrayList<Item> getAllItems(SQLiteDatabase database) {
        return getItemsWithQuery(database, "select * from " + TABLE_NAME);
    }

    public static Item getItemById(SQLiteDatabase database, int id) {
        ArrayList<Item> list = getItemsWithQuery(database, "select * from " + TABLE_NAME + " where " + COLUMN_ID + " = " + id);
        return list.get(0);
    }

    public static void loadDefaultItems(SQLiteDatabase db, Context context) {

        JSONObject jsonRObject = null;
        try {
            jsonRObject = new JSONObject(context.getString(R.string.item_json));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray jsonArray = jsonRObject.optJSONArray("Items");
        for(int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = null;
            try {
                jsonObject = jsonArray.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            int id = Integer.parseInt(jsonObject.optString("id").toString());
            String name = jsonObject.optString("name").toString();
            String type = jsonObject.optString("type").toString();
            String color = jsonObject.optString("color").toString();
            String size = jsonObject.optString("size").toString();
            String brand = jsonObject.optString("brand").toString();
            String weather = jsonObject.optString("weather").toString();
            String desc = jsonObject.optString("desc").toString();
            String image = jsonObject.optString("image").toString();
            insertItem(db, new Item(id, name, type, color, size, brand, weather, desc, image));
        }
    }

    public static List<Item> getItemListForFilterIdList(SQLiteDatabase database, List<String> filterIds) {
        // TODO: Change to a StringBuilder if you care about efficiency
        String formattedFilterIds = "";
        for (String filterId : filterIds) {
            formattedFilterIds = formattedFilterIds + filterId + ", ";
        }
        // Strip off the extra ", "
        formattedFilterIds = formattedFilterIds.substring(0, formattedFilterIds.length() - 2);

        return getItemsWithQuery(database, "SELECT * FROM Item I\n" +
                "JOIN Item_Filter IF ON I.item_id = IF.item_id\n" +
                "WHERE IF.filter_id IN (" + formattedFilterIds + ")\n" +
                "GROUP BY I.item_id");
    }

    public static ArrayList<Item> getItemsByState(SQLiteDatabase database, Integer state) {
        ArrayList<Item> list = getItemsWithQuery(database, "select * from " + TABLE_NAME + " where " + COLUMN_STATE + " = " + state);
        return list;
    }

    public static ArrayList<Item> getItemListForFilterIdListAndState(SQLiteDatabase database, List<String> filterIds, Integer state) {
        // TODO: Change to a StringBuilder if you care about efficiency
        String formattedFilterIds = "";
        for (String filterId : filterIds) {
            formattedFilterIds = formattedFilterIds + filterId + ", ";
        }
        // Strip off the extra ", "
        formattedFilterIds = formattedFilterIds.substring(0, formattedFilterIds.length() - 2);

        return getItemsWithQuery(database, "SELECT * FROM Item I\n" +
                "JOIN Item_Filter IF ON I.item_id = IF.item_id\n" +
                "WHERE IF.filter_id IN (" + formattedFilterIds + ") and I." + COLUMN_STATE + " = " + state + "\n" +
                "GROUP BY I.item_id");
    }

    public static int updateItemsFromStateToState(SQLiteDatabase database, Integer original_state, Integer new_state) {
        String where_clause = " WHERE " + COLUMN_STATE + " = " + new_state ;
        ContentValues values = new ContentValues();
        values.put(COLUMN_STATE, new_state);

        int affected = database.update(TABLE_NAME,values,where_clause,null);

        return affected;
    }


    public static boolean updateItemState(SQLiteDatabase db, Integer id, Integer new_state) {
        String update_query = "UPDATE " + TABLE_NAME + " SET " + COLUMN_STATE + " = " + new_state
                        + " WHERE " + COLUMN_ID + " = " + id;
        db.execSQL(update_query);
        return true;
    }
}
