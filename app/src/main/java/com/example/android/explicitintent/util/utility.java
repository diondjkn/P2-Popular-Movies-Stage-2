package com.example.android.explicitintent.util;

import android.content.ContentValues;

import com.example.android.explicitintent.favorite.FavoriteContract;
import com.example.android.explicitintent.network.model.Result;
import com.google.gson.Gson;

/**
 * Created by dionlusi on 8/12/17.
 */

public class utility {

    public static ContentValues getMovieValues(Result movie) {
        final ContentValues values = new ContentValues();
        values.put(FavoriteContract.FavoriteMovieEntry.COLUMN_MOVIE_ID, movie.getId());
        values.put(FavoriteContract.FavoriteMovieEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
        values.put(FavoriteContract.FavoriteMovieEntry.COLUMN_ORIGINAL_TITLE, movie.getOriginalTitle());
        values.put(FavoriteContract.FavoriteMovieEntry.COLUMN_TITLE, movie.getTitle());
        values.put(FavoriteContract.FavoriteMovieEntry.COLUMN_OVERVIEW, movie.getOverview());
        values.put(FavoriteContract.FavoriteMovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
        values.put(FavoriteContract.FavoriteMovieEntry.COLUMN_BACKDROP_PATH, movie.getBackdropPath());
        values.put(FavoriteContract.FavoriteMovieEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());
        values.put(FavoriteContract.FavoriteMovieEntry.COLUMN_GENRE_IDS, new Gson().toJson(movie.getGenreIds()));
        return values;
    }

}
