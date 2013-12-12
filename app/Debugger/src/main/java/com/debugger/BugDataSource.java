package com.debugger;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * BugDataSource - Handles database removal, insertion and creation of Bugs
 */
public class BugDataSource {
    private SQLiteDatabase db;
    private BugSQLiteHelper dbHelper;
    private String[] columns = {
            BugSQLiteHelper.COLUMN_ID,
            BugSQLiteHelper.COLUMN_BUG_ID,
            BugSQLiteHelper.COLUMN_LANGUAGE,
            BugSQLiteHelper.COLUMN_USER_CODE,
            BugSQLiteHelper.COLUMN_SOURCE_CODE };

    public BugDataSource(Context context) {
        dbHelper = new BugSQLiteHelper(context);
    }

    public void open() throws SQLException {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void updateBug(Bug bug) {
        ContentValues values = new ContentValues();
        values.put(BugSQLiteHelper.COLUMN_USER_CODE, bug.getCurrentCode());

        db.update(BugSQLiteHelper.TABLE_BUGS,
                values,
                BugSQLiteHelper.COLUMN_ID + " = " + bug.getSolutionId(),
                null);
    }


    public Bug createBug(String bugId, String language, String code) {
        if (bugId == null || language == null || code == null)
            return null;


        ContentValues values = new ContentValues();
        values.put(BugSQLiteHelper.COLUMN_BUG_ID, bugId);
        values.put(BugSQLiteHelper.COLUMN_LANGUAGE, language);
        values.put(BugSQLiteHelper.COLUMN_SOURCE_CODE, code);
        values.put(BugSQLiteHelper.COLUMN_USER_CODE, code);

        long insertId = db.insert(BugSQLiteHelper.TABLE_BUGS, null, values);
        Cursor cursor = db.query(BugSQLiteHelper.TABLE_BUGS, columns,
                BugSQLiteHelper.COLUMN_ID + " = " + insertId,
                null, null, null, null);

        cursor.moveToFirst();
        Bug bug = cursorToBug(cursor);
        cursor.close();

        return bug;
    }

    /**
     * deleteBug Deletes a bug from the local DB
     * @param bug bug to delete
     */
    public void deleteBug(Bug bug) {
        db.delete(BugSQLiteHelper.TABLE_BUGS,
            BugSQLiteHelper.COLUMN_ID + " = " + bug.getSolutionId(), null);
    }

    /**
     * getAllBugs
     * @return List of all Bugs in the local database
     * //TODO: reverse order of _id
     */
     public List<Bug> getAllBugs() {
        List<Bug> bugs = new ArrayList<Bug>();
        Cursor cursor = db.query(BugSQLiteHelper.TABLE_BUGS,
            columns, null, null, null, null, BugSQLiteHelper.COLUMN_ID + " DESC");

        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            bugs.add(cursorToBug(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return bugs;
    }

    private Bug cursorToBug(Cursor cursor) {
        Bug bug = new Bug(
                cursor.getString(cursor.getColumnIndex(BugSQLiteHelper.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(BugSQLiteHelper.COLUMN_BUG_ID)),
                Language.valueOf(cursor.getString(cursor.getColumnIndex(BugSQLiteHelper.COLUMN_LANGUAGE))),
                cursor.getString(cursor.getColumnIndex(BugSQLiteHelper.COLUMN_SOURCE_CODE))
        );
        bug.setCurrentCode(cursor.getString(cursor.getColumnIndex(BugSQLiteHelper.COLUMN_USER_CODE)));
        return bug;
    }
}
