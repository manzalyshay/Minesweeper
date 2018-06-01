package com.example.shaym.minesweeper.Storage;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "RecordsManager";

    // Records table name
    private static final String TABLE_RECORDS = "records";

    // Records Table Columns names
    private static final String KEY_RAWTIME = "rawtime";
    private static final String KEY_LVL = "lvl";
    private static final String KEY_NAME = "name";
    private static final String KEY_TIME = "time";
    private static final String KEY_LAT = "lat";
    private static final String KEY_LNG = "lng";

    
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_RECORDS_TABLE = "CREATE TABLE " + TABLE_RECORDS + "("
                + KEY_RAWTIME + " INTEGER PRIMARY KEY," + KEY_LVL + " INTEGER,"
                + KEY_NAME + " TEXT," + KEY_TIME + " TEXT," + KEY_LNG + " DOUBLE," +
                KEY_LAT + " DOUBLE" + ")";
        db.execSQL(CREATE_RECORDS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECORDS);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new Record
    public void addRecord(Record Record) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_RAWTIME, Record.getRawtime());
        values.put(KEY_LVL, Record.getLvl());
        values.put(KEY_NAME, Record.getName());
        values.put(KEY_TIME, Record.getTime());
        Location location = Record.getLocation();
        if(location != null) {
            values.put(KEY_LNG, location.getLongitude());
            values.put(KEY_LAT, location.getLatitude());
        } else{

            values.put(KEY_LNG, 0);
            values.put(KEY_LAT,0);
        }
        // Inserting Row
        db.insert(TABLE_RECORDS, null, values);
        db.close(); // Closing database connection
    }

    // Getting single Record
    public Record getRecord(int rawtime) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_RECORDS, new String[] { KEY_RAWTIME,
                        KEY_LVL, KEY_NAME, KEY_TIME, KEY_LNG, KEY_LAT }, KEY_RAWTIME + "=?",
                new String[] { String.valueOf(rawtime) }, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        int rtime =Integer.parseInt(cursor.getString(0));
        int lvl = Integer.parseInt(cursor.getString(1));
        String name = cursor.getString(2);
        String time = cursor.getString(3);
        Double lng = cursor.getDouble(4);
        Double lat = cursor.getDouble(5);
        Location temp = new Location("");
        temp.setLongitude(lng);
        temp.setLatitude(lat);

        Record Record = new Record(rtime ,lvl,name, time, temp);

        // return Record
        return Record;
    }

    // Getting All records
    public List<Record> getAllRecords() {
        List<Record> RecordList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_RECORDS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Location temp = new Location("");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Record Record = new Record();
                Record.setRawtime(Integer.parseInt(cursor.getString(0)));
                Record.setLvl(Integer.parseInt(cursor.getString(1)));
                Record.setName(cursor.getString(2));
                Record.setTime(cursor.getString(3));
                temp.setLongitude(cursor.getDouble(4));
                temp.setLatitude(cursor.getDouble(5));
                Record.setLocation(temp);
                // Adding Record to list
                RecordList.add(Record);
            } while (cursor.moveToNext());
        }

        // return Record list
        return RecordList;
    }

    /* Updating single Record
    public int updateRecord(Record Record) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_RAWTIME, Record.getRawtime());
        values.put(KEY_LVL, Record.getLvl());
        values.put(KEY_NAME, Record.getName());
        values.put(KEY_TIME, Record.getTime());

        // updating row
        return db.update(TABLE_RECORDS, values, KEY_RAWTIME + " = ?",
                new String[] { String.valueOf(Record.getID()) });
    }
    */

    // Deleting single Record
    public void deleteRecord(Record Record) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RECORDS, KEY_RAWTIME + " = ?",
                new String[] { String.valueOf(Record.getRawtime()) });
        db.close();
    }


    // Getting Records Count
    public int getRecordsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_RECORDS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

}