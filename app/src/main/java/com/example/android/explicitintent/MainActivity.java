/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.explicitintent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ScrollView;

import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.GsonRequest;
import com.example.android.explicitintent.favorite.GetResultList;
import com.example.android.explicitintent.network.base.VolleySingleton;
import com.example.android.explicitintent.network.model.Movie;
import com.example.android.explicitintent.network.model.Result;
import com.facebook.stetho.Stetho;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.explicitintent.favorite.FavoriteContract.FavoriteMovieEntry.CONTENT_URI;
import static com.example.android.explicitintent.favorite.FavoriteContract.FavoriteMovieEntry.MOVIE_PROJECTION;


public class MainActivity extends AppCompatActivity
        implements Response.Listener<GetResultList> {



    private List<Result> results = new ArrayList<>();
    private GridView gridview;
    private static final String BUNDLE_SCROLL_POSITION = "scroll_position";
    //NestedScrollView sv0;
    ScrollView sv0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Stetho.initializeWithDefaults(this);
        setContentView(R.layout.activity_main);


        gridview = (GridView) findViewById(R.id.gridview);
        gridview.getLastVisiblePosition();
        gridview.setAdapter(new ImageAdapter(this, results));
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent i = new Intent(MainActivity.this, FullImageActivity.class);
                Result result = results.get(position);
                i.putExtra(FullImageActivity.KEY_MOVIE_DATA, result);
                startActivity(i);
            }
        });


        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        String sortby = sharedPref.getString("SORT_TYPE", "popular");

        if (sortby == "favorite") {
            loadFavoriteMoviesFromDb();
        } else {
            callVolley(sortby);
        }

//        sv0 = (ScrollView) findViewById(R.id.fullScrollView);
//
        if (savedInstanceState != null && savedInstanceState.containsKey(BUNDLE_SCROLL_POSITION)) {
            final int scrollPosition = savedInstanceState.getInt(BUNDLE_SCROLL_POSITION);
            if (scrollPosition >= 0 ) {
                gridview.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //gridview.scrollTo(scrollPosition[0], scrollPosition[1]);
                        gridview.smoothScrollToPosition(scrollPosition);
                    }
                }, 1000L);
            }
        }

    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);


        //outState.putIntArray(BUNDLE_SCROLL_POSITION, new int[]{gridview.getScrollX(), gridview.getScrollY()});
        outState.putInt(BUNDLE_SCROLL_POSITION, gridview.getFirstVisiblePosition());
    }

    private void callVolley(String option) {

        String url = "https://api.themoviedb.org/3/movie/" + option + "?api_key=";
        GsonRequest<Movie> gsonRequest =
                new GsonRequest<>(url, Movie.class, null,
                        new Response.Listener<Movie>() {
                            @Override
                            public void onResponse(Movie movie) {
                                // TODO do next things after response success
                                results = movie.getResults();
                                gridview.setAdapter(new ImageAdapter(MainActivity.this, results));
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO do next things after response error
                    }
                });
        VolleySingleton.getInstance(this).addToRequestQueue(gsonRequest, MainActivity.class);
    }

    @Override
    public void onResponse(@NonNull GetResultList currentResponse) {
        results = currentResponse.getResults();
        gridview.setAdapter(new ImageAdapter(MainActivity.this, results));
    }

    @Override
    protected void onStop() {
        super.onStop();
        VolleySingleton.getInstance(this).cancelPendingRequests(MainActivity.class);
    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        String sortby = sharedPref.getString("SORT_TYPE", "popular");

        if (sortby == "favorite") {
            loadFavoriteMoviesFromDb();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater()
                .inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String sortby = "popular";
        int id = item.getItemId();
        if (id == R.id.popular_movie) {
            sortby = "popular";
            SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
            //sortBy = sharedPref
            sharedPref.edit().putString("SORT_TYPE", sortby).apply();
            callVolley(sortby);
            return true;
        } else if (id == R.id.top_rated) {
            sortby = "top_rated";
            SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
            sharedPref.edit().putString("SORT_TYPE", sortby).apply();
            callVolley(sortby);
            return true;
        } else if (id == R.id.favorite) {
            sortby = "favorite";
            SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
            sharedPref.edit().putString("SORT_TYPE", sortby).apply();
            loadFavoriteMoviesFromDb();
            return true;
        }
        ;


        return super.onOptionsItemSelected(item);
    }


    private void loadFavoriteMoviesFromDb() {
        new Thread() {
            @Override
            public void run() {
                final ArrayList<Result> movieArrayList = new ArrayList<>();
                final Cursor query = getContentResolver().query(CONTENT_URI, MOVIE_PROJECTION, null, null, null);
                if (query != null) {
                    if (query.moveToFirst()) {
                        do {
                            movieArrayList.add(Result.fromCursor(query));
                        } while (query.moveToNext());
                    }
                }

                if (!MainActivity.this.isFinishing() && !MainActivity.this.isDestroyed()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            GetResultList response = new GetResultList();
                            response.setPage(1);
                            response.setTotal_pages(1);
                            response.setTotal_results(1);
                            response.setResults(movieArrayList);

                            onResponse(response);
                        }
                    });
                }
            }
        }.start();
    }

}