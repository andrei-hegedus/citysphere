package com.bndiapps.citysphere.repo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bndiapps.citysphere.Rating;
import com.bndiapps.citysphere.database.RatingDatabaseHelper;
import com.bndiapps.citysphere.database.RatingEntryMetadata;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andrei on 8/26/16.
 */
public class RatingsRepo {

    private final RatingDatabaseHelper ratingsDbHelper;

    public RatingsRepo(Context context) {
        ratingsDbHelper = new RatingDatabaseHelper(context);
    }

    public void add(Rating rating) {
        SQLiteDatabase db = ratingsDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(RatingEntryMetadata.COLUMN_NAME_RATING, rating.getRating());
        values.put(RatingEntryMetadata.COLUMN_NAME_LAT, rating.getLatitude());
        values.put(RatingEntryMetadata.COLUMN_NAME_LNG, rating.getLongitude());

        db.insert(RatingEntryMetadata.TABLE_NAME, null, values);
    }

    public List<Rating> getAll() {
        List<Rating> ratings = new ArrayList<>();

        String[] projection = {
                RatingEntryMetadata.ID,
                RatingEntryMetadata.COLUMN_NAME_LAT,
                RatingEntryMetadata.COLUMN_NAME_LNG,
                RatingEntryMetadata.COLUMN_NAME_RATING
        };

        SQLiteDatabase db = ratingsDbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                RatingEntryMetadata.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        if (cursor.moveToFirst()) {
            ratings.add(createRating(cursor));
            while (cursor.moveToNext()) {
                ratings.add(createRating(cursor));
            }
        }


        return ratings;
    }

    private Rating createRating(Cursor cursor) {
        double lat = cursor.getDouble(cursor.getColumnIndexOrThrow(RatingEntryMetadata.COLUMN_NAME_LAT));
        double lng = cursor.getDouble(cursor.getColumnIndexOrThrow(RatingEntryMetadata.COLUMN_NAME_LNG));
        int ratingValue = cursor.getInt(cursor.getColumnIndexOrThrow(RatingEntryMetadata.COLUMN_NAME_RATING));

        Rating rating = new Rating(lat, lng, ratingValue);
        return rating;
    }
}
