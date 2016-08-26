package com.bndiapps.citysphere.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by andrei on 8/26/16.
 */
public class RatingDatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "location_ratings.db";
    public static final int DATABASE_VERSION = 1;

    private static final String INT_TYPE = " INT";
    private static final String DOUBLE_TYPE = " DOUBLE";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + RatingEntryMetadata.TABLE_NAME + " (" +
                    RatingEntryMetadata.ID + " INTEGER PRIMARY KEY," +
                    RatingEntryMetadata.COLUMN_NAME_LAT + DOUBLE_TYPE + COMMA_SEP +
                    RatingEntryMetadata.COLUMN_NAME_LNG + DOUBLE_TYPE + COMMA_SEP +
                    RatingEntryMetadata.COLUMN_NAME_RATING + INT_TYPE + " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + RatingEntryMetadata.TABLE_NAME;

    public RatingDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
