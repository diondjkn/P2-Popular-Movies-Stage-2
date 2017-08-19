package com.example.android.explicitintent.favorite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.android.explicitintent.favorite.FavoriteContract.FavoriteMovieEntry;

public class FavoriteDbH extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favoriteMovie.db";

    private static final int VERSION = 1;

    private Context context;

    FavoriteDbH(Context context) {
        super(context.getApplicationContext(), DATABASE_NAME, null, VERSION);
        this.context = context.getApplicationContext();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE = "CREATE TABLE " + FavoriteMovieEntry.TABLE_NAME + " (" +
                FavoriteMovieEntry._ID + " INTEGER PRIMARY KEY, " +
                FavoriteMovieEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL, " +
                FavoriteMovieEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                FavoriteMovieEntry.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL, " +
                FavoriteMovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                FavoriteMovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                FavoriteMovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                FavoriteMovieEntry.COLUMN_BACKDROP_PATH + " TEXT NOT NULL, " +
                FavoriteMovieEntry.COLUMN_VOTE_AVERAGE + " DOUBLE NOT NULL, " +
                FavoriteMovieEntry.COLUMN_GENRE_IDS + " TEXT NOT NULL);";

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO can implement update instead of drop later
        db.execSQL("DROP TABLE IF EXISTS " + FavoriteContract.FavoriteMovieEntry.TABLE_NAME);
        onCreate(db);
    }

    /**
     * Delete the whole database.
     */
    void deleteDatabase() {
        context.deleteDatabase(DATABASE_NAME);
    }
}
