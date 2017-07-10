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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import java.io.InputStream;
import android.graphics.BitmapFactory;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    /* Fields that will store our EditText and Button */
    private EditText mNameEntry;
    private Button mDoSomethingCoolButton;
    private Button mSortTopRated;
    private Button mSortPopular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // show The Image in a ImageView
        new DownloadImageTask((ImageView) findViewById(R.id.imageView1))
                .execute("https://image.tmdb.org/t/p/w320/5qcUGqWoWhEsoQwNUrtf3y3fcWn.jpg");

        // public void onClick(View v) {
        //     startActivity(new Intent(this, IndexActivity.class));
        //     finish();

        //}


        GridView m_gridview = (GridView) findViewById(R.id.movies_gridview);
        //m_gridview.setAdapter(new ImageAdapter(this));

        m_gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(MainActivity.this, "" + position,
                        Toast.LENGTH_SHORT).show();
            }
        });

        /*
         * Using findViewById, we get a reference to our Button from xml. This allows us to
         * do things like set the onClickListener which determines what happens when the button
         * is clicked.
         */
        mDoSomethingCoolButton = (Button) findViewById(R.id.b_do_something_cool);
        mNameEntry = (EditText) findViewById(R.id.et_text_entry);
        mSortTopRated = (Button) findViewById(R.id.b_top_rated);
        mSortPopular = (Button) findViewById(R.id.b_popular);

        /* Setting an OnClickListener allows us to do something when this button is clicked. */
        mDoSomethingCoolButton.setOnClickListener(new OnClickListener() {

            /**
             * The onClick method is triggered when this button (mDoSomethingCoolButton) is clicked.
             *
             * @param v The view that is clicked. In this case, it's mDoSomethingCoolButton.
             */

            @Override
            public void onClick(View v) {
                // TODO OK (1) Retrieve the text from the EditText and store it in a variable
                String textEntered = mNameEntry.getText().toString();
                String cDion = " ";

                Context context = MainActivity.this;

                Class destinationActivity = ChildActivity.class;

                Intent startChildActivityIntent = new Intent(context, destinationActivity);

                startChildActivityIntent.putExtra(Intent.EXTRA_TEXT, textEntered);
                startChildActivityIntent.putExtra(cDion, "dion keren");

                startActivity(startChildActivityIntent);
            }
        });
        mSortTopRated.setOnClickListener(new OnClickListener() {

            /**
             *  Catatan tombol Top Rated
             * */

            @Override
            public void onClick(View v) {
                // TODO OK (1) Retrieve the text from the EditText and store it in a variable
                String textEntered = mNameEntry.getText().toString();

                Context context = MainActivity.this;

                Class destinationActivity = ChildActivity.class;

                Intent startChildActivityIntent = new Intent(context, destinationActivity);

                startChildActivityIntent.putExtra(Intent.EXTRA_TEXT, textEntered);

                startActivity(startChildActivityIntent);
            }
        });
        mSortPopular.setOnClickListener(new OnClickListener() {

            /**
             *  Catatan tombol Popular
             * */

            @Override
            public void onClick(View v) {
                // TODO OK (1) Retrieve the text from the EditText and store it in a variable
                String textEntered = mNameEntry.getText().toString();

                Context context = MainActivity.this;

                Class destinationActivity = ChildActivity.class;

                Intent startChildActivityIntent = new Intent(context, destinationActivity);

                startChildActivityIntent.putExtra(Intent.EXTRA_TEXT, textEntered);

                startActivity(startChildActivityIntent);
            }
        });
    }


    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

}