package com.example.a300cem_assignment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private final static int _DBVersion = 1;
    private final static String DATABASE_NAME = "assignmentDB.db";
    private final static String CONTACTS_TABLE_NAME  = "recordTable";
    private static final String CONTACTS_COLUMN_ID = "_id";
    private static final String CONTACTS_COLUMN_TITLE = "_title";
    private static final String CONTACTS_COLUMN_DESCRIPTION = "_description";
    private static final String CONTACTS_COLUMN_DATE = "_date";
    private static final String CONTACTS_COLUMN_AUDIOFILENAME = "_audiofilename";
    private static final String CONTACTS_COLUMN_PHOTOPATH = "_photopath";

    DBHelper(Context context) {
        super(context, DATABASE_NAME, null, _DBVersion);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL = "CREATE TABLE IF NOT EXISTS " + CONTACTS_TABLE_NAME + "( " +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "_title TEXT, " +
                "_description TEXT," +
                "_date TEXT," +
                "_audiofilename TEXT," +
                "_photopath TEXT" +
                ");";
        db.execSQL(SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS recordTable");
        onCreate(db);
    }

    boolean insertRecord(RecordObject ro) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONTACTS_COLUMN_TITLE, ro.getTitle());
        contentValues.put(CONTACTS_COLUMN_DESCRIPTION, ro.getDescription());
        contentValues.put(CONTACTS_COLUMN_DATE, ro.getDate());
        contentValues.put(CONTACTS_COLUMN_AUDIOFILENAME, ro.getAudioFileName());
        contentValues.put(CONTACTS_COLUMN_PHOTOPATH, ro.getPhotoPath());
        db.insert(CONTACTS_TABLE_NAME, null, contentValues);
        return true;
    }

    Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery( "select * from "+CONTACTS_TABLE_NAME+" where _id="+id+";", null );
    }

    ArrayList<RecordObject> getAllRecord() {
        ArrayList<RecordObject> array_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+CONTACTS_TABLE_NAME+";", null );
        res.moveToFirst();
        RecordObject ro;
        while(!res.isAfterLast()){
            ro = new RecordObject(res.getString(res.getColumnIndex(CONTACTS_COLUMN_TITLE)),
                    res.getString(res.getColumnIndex(CONTACTS_COLUMN_DESCRIPTION)),
                    res.getString(res.getColumnIndex(CONTACTS_COLUMN_DATE)),
                    res.getString(res.getColumnIndex(CONTACTS_COLUMN_AUDIOFILENAME)),
                    res.getString(res.getColumnIndex(CONTACTS_COLUMN_PHOTOPATH)));
            ro.setId(res.getString(res.getColumnIndex(CONTACTS_COLUMN_ID)));
            array_list.add(ro);
            res.moveToNext();
        }
        res.close();
        return array_list;
    }
}
