package com.debugger;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * BugData - Handles database removal, insertion and object creation
 */
public class BugData {
    private SQLiteDatabase db;
    private SQLiteHelper dbHelper;
    private String[] cols = { SQLiteHelper.COLUMN_ID,
        SQLiteHelper.COLUMN_USER_CODE,
        SQLiteHelper.COLUMN_SOURCE_CODE };

    public BugData(Context context) {
        dbHelper = new SQLiteHelper(context);
    }

    public void open() throws SQLException {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    /**
     * deleteBug - Deletes a bug from the local DB
     * @param id - id of bug to delete
     *
     * TODO: Explore using primary db key instead of bug ID
     *           requires changes in Bug and SQLiteHelper
     */
    public void deleteBug(String id) {
        //System.out.println("Deleted bug " + id);

        db.delete(SQLiteHelper.TABLE_BUGS,
            SQLiteHelper.COLUMN_ID + " = " + id, null);
    }

    /**
     * getAllBugs
     * @return List of all Bugs in the local database
     */
    public List<Bug> getAllBugs() {
        List<Bug> bugs = new ArrayList<Bug>();
        Cursor cursor = db.query(SQLiteHelper.TABLE_BUGS,
            cols, null, null, null, null, null);

        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            bugs.add(cursorToBug(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return bugs;
    }

    /**
     * cursorToBug - creates a bug from the given db cursor
     * @param cursor - cursor pointing to Bug data
     * @return newly created Bug object
     */
    private Bug cursorToBug(Cursor cursor) {
        return new Bug(cursor.getString(
                cursor.getColumnIndex(SQLiteHelper.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_SOURCE_CODE)));
    }
}
