package com.Class;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;
import java.util.Map;


public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context, String name, CursorFactory factory,
                    int version) {
        super(context, name, factory, version);
    }

    static DBHelper dbHelper;
    SQLiteDatabase database;

    final static String DB_NAME = "Tru.db";
    final static int DB_VERSION = 1;

    static final String TABLE_USER_DETAIL = "user_detail";
    static final String TABLE_USER_DETAIL_key = "user_detail_key";
    static final String OLDREG_ID = "oldreg_id";
    static final String USER_ID = "u_id";
    static final String KEY = "key";
    static final String Name = "name";
    static final String email = "name";
    static final String DATABASE_USER = "create table " + TABLE_USER_DETAIL
            + "(" + USER_ID + " text);";


    static final String DATABASE_USER_key = "create table " + TABLE_USER_DETAIL_key
            + "(" + KEY + " text);";


    public boolean deleteCategory() {
        database.delete(TABLE_USER_DETAIL, null, null);
        return true;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // db.execSQL(DATABASE_USER);

        db.execSQL(DATABASE_USER_key);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_DETAIL);

        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_USER_key);
        onCreate(db);
    }


    public SQLiteDatabase getdatabase() {
        SQLiteDatabase database = this.getWritableDatabase();
        return database;
    }


    public boolean updateUser(String d) {
        ContentValues values = new ContentValues();
        if (d != null && d.length() > 0)
            values.put(USER_ID, d);


        database.update(TABLE_USER_DETAIL, values, null, null);
        return true;
    }

    public boolean deleteUser() {
        database.delete(TABLE_USER_DETAIL, null, null);

        return true;
    }

    public Map<String, String> getUser() {
        Map<String, String> mapParams = new HashMap<String, String>();
        Cursor cursor = database.query(TABLE_USER_DETAIL, null, null, null,
                null, null, null);
        cursor.moveToFirst();

        String[] columnNames = cursor.getColumnNames();

        for (int i = 0; i < columnNames.length; i++) {
            mapParams.put(columnNames[i], cursor.getString(i));
        }


        cursor.close();
        return mapParams;
    }


    public Map<String, String> getUserTablecoloms() {
        Map<String, String> mapParams = new HashMap<String, String>();
        Cursor cursor = database.query(TABLE_USER_DETAIL, null, null, null,
                null, null, null);
        cursor.moveToFirst();
        String[] columnNames = cursor.getColumnNames();

        for (int i = 0; i < columnNames.length; i++) {
            mapParams.put(columnNames[i], columnNames[i]);
        }

        cursor.close();
        return mapParams;
    }


    public String[] getUserTablecolomsstringarray() {
        Map<String, String> mapParams = new HashMap<String, String>();
        Cursor cursor = database.query(TABLE_USER_DETAIL, null, null, null,
                null, null, null);
        cursor.moveToFirst();
        String[] columnNames = cursor.getColumnNames();


        return columnNames;
    }


    public void open() {

        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        if (database != null) {
            database.close();
        }
    }

    public boolean deleteGCM() {

        database.delete(TABLE_USER_DETAIL_key, null, null);
        return true;

    }

    public boolean addkeyvalue(String regid) {

        ContentValues values = new ContentValues();
        values.put(KEY, regid);
        database.insert(TABLE_USER_DETAIL_key, null, values);
        return true;


    }

    public String getuser_key() {
        Cursor cc = database.query(TABLE_USER_DETAIL_key, null, null, null, null, null,
                null);
        if (cc.moveToFirst()) {
            return cc.getString(0);
        } else {
            return null;
        }
    }

    public static DBHelper getInstance(Context context) {
        if (dbHelper == null) {

            dbHelper = new DBHelper(context, DB_NAME, null, DB_VERSION);

        }
        return dbHelper;
    }
}
