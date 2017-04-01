package a2dv606.androidproject.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

import a2dv606.androidproject.Model.DateLog;
import a2dv606.androidproject.Model.TimeLog;

/**
 * Created by f on 2017-03-28.
 */
public class DrinkDataSource {
    private SQLiteDatabase database;
    private DrinkDbHelper dbHelper;
    long insertId;
    private String[] allDateColumns = {DrinkDbHelper.COLUMN_ID,
            DrinkDbHelper.COLUMN_WATER_NEED, DrinkDbHelper.COLUMN_WATER_DRUNK ,DrinkDbHelper.COLUMN_DATE};

    private String[] allTimeColumns = {DrinkDbHelper.COLUMN_TIME_ID ,DrinkDbHelper.COLUMN_WATER_DRUNK_ONCE ,DrinkDbHelper.COLUMN_TIME_DATE,DrinkDbHelper.COLUMN_TIME};

    public DrinkDataSource(Context context) {
        dbHelper = new DrinkDbHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }
    public DateLog create(int amount,int n,String d) {
        ContentValues values = new ContentValues();
        values.put(DrinkDbHelper.COLUMN_WATER_DRUNK, amount);
        values.put(DrinkDbHelper.COLUMN_WATER_NEED, n);
        values.put(DrinkDbHelper.COLUMN_DATE, d);
        long insertId = database.insert(DrinkDbHelper.Date_TABLE_NAME, null, values);
        Cursor cursor = database.query(DrinkDbHelper.Date_TABLE_NAME,
                allDateColumns, DrinkDbHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        DateLog newTime = cursorToDataLog(cursor);
        cursor.close();
        return newTime;
    }
    public void close() {
        dbHelper.close();
    }

    public int updateDrinkingAmount(int waterDrunk, int amount ) {
        waterDrunk = waterDrunk + amount;
        ContentValues cv = new ContentValues();
        cv.put(DrinkDbHelper.COLUMN_WATER_DRUNK, waterDrunk);
        return database.update(DrinkDbHelper.Date_TABLE_NAME, cv,
                DrinkDbHelper.COLUMN_ID + "= (SELECT " + DrinkDbHelper.COLUMN_ID + " DESC LIMIT 1 )", null);
    }

    public int getDrinkingAmount() {
        int waterAmount = 0;

            Cursor cursor = database.query(DrinkDbHelper.Date_TABLE_NAME,
                    new String[]{DrinkDbHelper.COLUMN_ID,
                            DrinkDbHelper.COLUMN_WATER_DRUNK}, null, null, null, null, DrinkDbHelper.COLUMN_ID + " DESC", " 1");
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                waterAmount = cursor.getInt(cursor.getColumnIndex( DrinkDbHelper.COLUMN_WATER_DRUNK));
            }
             cursor.close();
            return waterAmount;



    }



    public List<DateLog> getAllDates() {
        List<DateLog> dateLog = new ArrayList<DateLog>();

        Cursor cursor = database.query(DrinkDbHelper.Date_TABLE_NAME,
                allDateColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            DateLog task = cursorToDataLog(cursor);
            dateLog.add(task);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return dateLog;
    }

    private DateLog cursorToDataLog(Cursor cursor) {
        DateLog date = new DateLog();
        date.setID(cursor.getLong(0));
        date.setWaterNeed(cursor.getInt(1));
        date.setWaterDrunk(cursor.getInt(2));
        date.setDate(cursor.getString(3));
        return date;
    }

    public TimeLog createTime(int amount,String date,String time) {
        ContentValues values = new ContentValues();
        values.put(DrinkDbHelper.COLUMN_WATER_DRUNK_ONCE, amount);
        values.put(DrinkDbHelper.COLUMN_TIME_DATE, date);
        values.put(DrinkDbHelper.COLUMN_TIME, time);
        long insertId = database.insert(DrinkDbHelper.TIME_TABLE_NAME, null, values);
        Cursor cursor = database.query(DrinkDbHelper.TIME_TABLE_NAME,
                allTimeColumns, DrinkDbHelper.COLUMN_TIME_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        TimeLog newTime = cursorToTimeLog(cursor);
        cursor.close();
        return newTime;
    }
    private TimeLog cursorToTimeLog(Cursor cursor) {
        TimeLog time = new TimeLog();
        time.setID(cursor.getLong(0));
        time.setAmount(cursor.getInt(1));
        time.setDate(cursor.getString(2));
        time.setTime(cursor.getString(3));
        return time;
    }

    public List<TimeLog> getAllTimes() {
        List<TimeLog> timeLog = new ArrayList<TimeLog>();

        Cursor cursor = database.query(DrinkDbHelper.TIME_TABLE_NAME,
                allTimeColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            TimeLog task = cursorToTimeLog(cursor);
            timeLog.add(task);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return timeLog;
    }
    public void delete(TimeLog data) {
        Long id = data.getID();
        database.delete(DrinkDbHelper.TIME_TABLE_NAME,
                DrinkDbHelper.COLUMN_TIME_ID + "=" + id, null);

    }
    public boolean update(int ID, int waterDrunk) {
        ContentValues cv = new ContentValues();
        cv.put(DrinkDbHelper.COLUMN_WATER_DRUNK_ONCE, waterDrunk);
        return database.update(DrinkDbHelper.TIME_TABLE_NAME, cv,
                DrinkDbHelper.COLUMN_TIME_ID + "=" + ID, null) > 0;
    }


    public List<TimeLog> sortByTimeAsc() {
        List<TimeLog> list = new ArrayList<TimeLog>();
        Cursor cursor=database.query(DrinkDbHelper.TIME_TABLE_NAME, allTimeColumns, null,
                null, null, null, DrinkDbHelper.COLUMN_TIME + " ASC ");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursorToTimeLog(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<TimeLog> sortByTimeDesc() {
        List<TimeLog> list = new ArrayList<TimeLog>();
        Cursor cursor=database.query(DrinkDbHelper.TIME_TABLE_NAME, allTimeColumns, null,
                null, null, null, DrinkDbHelper.COLUMN_TIME + " DESC ");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursorToTimeLog(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<TimeLog> sortByAmountAsc() {
        List<TimeLog> lists = new ArrayList<TimeLog>();
        Cursor cursor=database.query(DrinkDbHelper.TIME_TABLE_NAME, allTimeColumns, null,
                null, null, null, DrinkDbHelper.COLUMN_WATER_DRUNK_ONCE + " ASC ");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            TimeLog list = cursorToTimeLog(cursor);
            lists.add(list);
            cursor.moveToNext();
        }
        cursor.close();
        return lists;
    }
    public List<TimeLog> sortByAmountDesc() {
        List<TimeLog> lists = new ArrayList<TimeLog>();
        Cursor cursor=database.query(DrinkDbHelper.TIME_TABLE_NAME, allTimeColumns, null,
                null, null, null, DrinkDbHelper.COLUMN_WATER_DRUNK_ONCE + " DESC ");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            TimeLog list = cursorToTimeLog(cursor);
            lists.add(list);
            cursor.moveToNext();
        }
        cursor.close();
        return lists;
    }
}