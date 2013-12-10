package com.debugger;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * TODO: add language column
 */
public class BugSQLiteHelper extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "bugs.db";
    private static final int DATABASE_VERSION = 2;

    public static final String TABLE_BUGS = "bugs";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_BUG_ID = "bug_id";
    public static final String COLUMN_LANGUAGE = "language";
    public static final String COLUMN_SOURCE_CODE = "source_code";
    public static final String COLUMN_USER_CODE = "user_code";

    private static final String CREATE_DB = "create table "
            + TABLE_BUGS
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_BUG_ID + " text not null, "
            + COLUMN_LANGUAGE + " text not null, "
            + COLUMN_SOURCE_CODE + " text not null, "
            + COLUMN_USER_CODE + " text not null"
            + ");";


    public BugSQLiteHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

     @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUGS);
        onCreate(db);
    }
}
