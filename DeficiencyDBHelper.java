package com.visione.amndd.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import static android.provider.BaseColumns._ID;
import static com.visione.amndd.data.DeficiencyContract.DeficiencyEntry.COLUMN_NAME_IMAGE;
import static com.visione.amndd.data.DeficiencyContract.DeficiencyEntry.TABLE_NAME;

public class DeficiencyDBHelper extends SQLiteOpenHelper {

    //for creating tables
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY," +
                    DeficiencyContract.DeficiencyEntry.COLUMN_NAME_DATE + " TEXT," +
                    DeficiencyContract.DeficiencyEntry.COLUMN_NAME_DEFICIENCY + " TEXT," +
                    DeficiencyContract.DeficiencyEntry.COLUMN_NAME_SOLUTION + " TEXT," +
                    DeficiencyContract.DeficiencyEntry.COLUMN_NAME_DIAGNOSED + " TEXT," +
                    DeficiencyContract.DeficiencyEntry.COLUMN_NAME_IMAGE + " BLOB NOT NULL)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Deficiency.db";

    public DeficiencyDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }


    //insert data without diagnose
    public void insertDeficiency(Deficiency deficiency){
        // Gets the data repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(DeficiencyContract.DeficiencyEntry.COLUMN_NAME_DATE, deficiency.getDate());
        values.put(DeficiencyContract.DeficiencyEntry.COLUMN_NAME_DEFICIENCY, deficiency.getDeficiency());
        values.put(DeficiencyContract.DeficiencyEntry.COLUMN_NAME_SOLUTION, deficiency.getSolution());
        values.put(DeficiencyContract.DeficiencyEntry.COLUMN_NAME_DIAGNOSED, deficiency.getDiagnosed());
        values.put(DeficiencyContract.DeficiencyEntry.COLUMN_NAME_IMAGE, deficiency.getImage());


        // Insert the new row, returning the primary key value of the new row
        db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public int updateDeficiency(Deficiency deficiency){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DeficiencyContract.DeficiencyEntry.COLUMN_NAME_DIAGNOSED, deficiency.getDiagnosed());

        // Which row to update, based on the title
        String selection = _ID + "=" + deficiency.getId();
        //String[] selectionArgs = { "" };

        return db.update(TABLE_NAME, values, selection, null);
    }

    public List<Deficiency> getData(){
        SQLiteDatabase db = this.getReadableDatabase();
        
        List<Deficiency> deficiencyList = new ArrayList<>();

        // Select all Query
        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        Cursor cursor = db.rawQuery(selectQuery, null);

        // Looping through all rows and adding to list
        if(cursor.moveToFirst()){
            do{
                Deficiency deficiency = new Deficiency();
                deficiency.setId(cursor.getString(0));
                deficiency.setDate(cursor.getString(1));
                deficiency.setDeficiency(cursor.getString(2));
                deficiency.setSolution(cursor.getString(3));
                deficiency.setDiagnosed(cursor.getString(4));
                deficiency.setImage(cursor.getBlob(cursor.getColumnIndex(COLUMN_NAME_IMAGE)));

                deficiencyList.add(deficiency);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return deficiencyList;
    }

    public void deleteTable(){
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("delete from "+ TABLE_NAME);
    }
}
