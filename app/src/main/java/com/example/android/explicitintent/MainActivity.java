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

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.GsonRequest;
import com.example.android.explicitintent.network.base.VolleySingleton;
import com.example.android.explicitintent.network.model.Movie;
import com.example.android.explicitintent.network.model.Result;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    /* Fields that will store our EditText and Button */
    private EditText mNameEntry;
    private Button mDoSomethingCoolButton;
    private Button mSortTopRated;
    private Button mSortPopular;

    private List<Result> results = new ArrayList<>();
    private GridView gridview;

    public static String[] arrayUrl2 = {
          "https://image.tmdb.org/t/p/w320/tWqifoYuwLETmmasnGHO7xBjEtt.jpg",
           "https://image.tmdb.org/t/p/w320/imekS7f1OuHyUP2LAiTEM0zBzUz.jpg",
            "https://image.tmdb.org/t/p/w320/5qcUGqWoWhEsoQwNUrtf3y3fcWn.jpg",
            "https://image.tmdb.org/t/p/w320/c24sv2weTHPsmDa7jEMN0m2P3RT.jpg",
            "https://image.tmdb.org/t/p/w320/9EXnebqbb7dOhONLPV9Tg2oh2KD.jpg",
            "https://image.tmdb.org/t/p/w320/qL0w9X1dVT3dnkZg3SrYtPFUHMs.jpg",
            "https://image.tmdb.org/t/p/w320/f8Ng1Sgb3VLiSwAvrfKeQPzvlfr.jpg",

    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //arrayUrl2 = (TextView) findViewById(R.id.text_view);
        // Arrays.asList(arrayUrl2)

        gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this, results));
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent i = new Intent(MainActivity.this, FullImageActivity.class);
                Result result = results.get(position);
                i.putExtra("result", new GsonBuilder().create().toJson(result));
                startActivity(i);
            }
        });

        callVolley("popular");

        /* Setting an OnClickListener allows us to do something when this button is clicked. */


    }


    private void callVolley(String option) {

        String url = "https://api.themoviedb.org/3/movie/"+option+"?api_key=1831fea099b5dc948f71ac983802ac79";
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
    protected void onStop() {
        super.onStop();
        VolleySingleton.getInstance(this).cancelPendingRequests(MainActivity.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater()
                .inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id==R.id.popular_movie){
            callVolley("popular");
            return true;
        }
        else if (id==R.id.top_rated){
            callVolley("top_rated");
            return true;
        };

        return super.onOptionsItemSelected(item);
    }
}