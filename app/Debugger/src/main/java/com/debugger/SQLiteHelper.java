package com.debugger;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by knarko on 11/26/13.
 */
public class SQLiteHelper extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "bugs.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_BUGS = "bugs";
    public static final String COLUMN_ID = "bug_id";
    public static final String COLUMN_SOURCE_CODE = "source_code";
    public static final String COLUMN_USER_CODE = "user_code";

    public SQLiteHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static final String CREATE_DB = "create table"
            + TABLE_BUGS
            + "("
            + COLUMN_ID + " text, "
            + COLUMN_SOURCE_CODE + " text,"
            + COLUMN_USER_CODE + " text"
            + ");";

     @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Do nothing
    }
}
