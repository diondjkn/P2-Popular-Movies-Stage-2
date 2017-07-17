package com.example.android.explicitintent;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.explicitintent.network.model.Result;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;


public class FullImageActivity extends Activity {

    ImageView img;
    Bitmap bitmap;
    private TextView cTitle;
    private TextView cReleaseDate;
    private ImageView iPoster;
    private TextView cOverview;
    private TextView cAverage;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fullimageview);

        Intent i = getIntent();
        String gson = i.getStringExtra("result");
        Result result = new GsonBuilder().create().fromJson(gson, Result.class);

//Movie details layout contains title, release date, movie poster, vote average, and plot synopsis.

        //result.getTitle();
        //result.getReleaseDate();
        //result.getPosterPath();
        //result.getOverview();
        //result.getVoteAverage();

        cTitle = (TextView) findViewById(R.id.title);
        cTitle.setText(result.getTitle());

        cReleaseDate = (TextView) findViewById(R.id.release_date);
        cReleaseDate.setText("Release Date: "+result.getReleaseDate());

        cOverview = (TextView) findViewById(R.id.overview);
        cOverview.setText("Overview: \n "+result.getOverview());

        cAverage = (TextView) findViewById(R.id.vote_average);
        cAverage.setText("Vote Average: "+result.getVoteAverage().toString());

        iPoster = (ImageView) findViewById(R.id.poster);
        Picasso.with(this).load("https://image.tmdb.org/t/p/w320"+result.getPosterPath()).into(iPoster);


    }
}





