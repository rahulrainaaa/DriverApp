package com.lugmity.driverapp.database;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

/**
 * @class SQLiteHandler
 * @desc SQLite database handling Singleton-class
 */
public class SQLiteHandler {
    private static SQLiteHandler ourInstance = new SQLiteHandler();
    private SQLiteDatabase mydatabase = null;
    private String DB_NAME = null;

    /**
     * @method getDB_NAME
     * @desc Get currently selected database name.
     * @return database name String
     */
    public String getDB_NAME()
    {
        return DB_NAME;
    }

    public static SQLiteHandler getInstance() {
        return ourInstance;
    }

    /**
     * @param activity activity object context
     * @param DB_NAME  name of database
     * @return reference to selected database
     * @method setDatabase
     * @desc Select the database for SQLite operations.
     */
    public SQLiteDatabase setDatabase(Activity activity, String DB_NAME) {
        releaseDB();
        this.DB_NAME = DB_NAME.trim();
        mydatabase = activity.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
        return mydatabase;
    }

    /**
     * @method getDatabase
     * @desc get Database instance.
     * @return reference to selected database.
     */
    public SQLiteDatabase getDatabase()
    {
        return mydatabase;
    }

    /**
     * @method releaseDB
     * @desc Release the database if occupied.
     */
    public void releaseDB() {
        try {
            if (mydatabase != null) {
                if (mydatabase.isOpen()) {
                    mydatabase.close();
                }
                mydatabase = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            mydatabase = null;
            this.DB_NAME = null;
        }
    }

    /**
     * @param SQLQuery
     * @return true=created / false=failed
     * @method create
     * @desc Creates table in selected database.
     */
    public boolean create(String SQLQuery) {
        try {
            mydatabase.execSQL(SQLQuery.trim());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @param SQLQuery Raw SQL string
     * @return Cursor Result from table
     * @method query
     * @desc Apply SQL select raw query.
     */
    public Cursor query(String SQLQuery) {
        Cursor cursor = null;
        try {
            cursor = mydatabase.rawQuery(SQLQuery.trim(), null);
        } catch (Exception e) {
            e.printStackTrace();
            cursor = null;
        }
        return cursor;
    }

    /**
     * @param SQLQuery SQL raw string
     * @return +ve = done, -ve = error
     * @method execute
     * @desc execute Insert, update SQL.
     */
    public Integer execute(String SQLQuery) {
        try {
            mydatabase.execSQL(SQLQuery.trim());
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * @param raw  sql string without parameter values.
     * @param args array of parameters.
     * @return
     * @method createSQLiteStatement
     * @desc Create SQLite Statement from raw SQL string and parameters.
     */
    public SQLiteStatement createSQLiteStatement(String raw, String args[]) {
        if (args != null) {
            try {
                SQLiteStatement stmt = mydatabase.compileStatement(raw);
                for (int i = 0; i < args.length; i++) {
                    stmt.bindString(1, args[i].trim());
                }
                return stmt;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    /**
     * @param stmt
     * @return +ve(number of row changes)= done, -ve = Error
     * @method execute
     * @desc Execute SQLiteStatement.
     */
    public Integer execute(SQLiteStatement stmt) {
        try {
            stmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
}
