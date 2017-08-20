package com.example.android.explicitintent;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.GsonRequest;
import com.example.android.explicitintent.adapter.TrailerAdapter;
import com.example.android.explicitintent.network.base.VolleySingleton;
import com.example.android.explicitintent.network.model.Result;
import com.example.android.explicitintent.network.model.Trailer;
import com.example.android.explicitintent.network.model.TrailerResult;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.explicitintent.favorite.FavoriteContract.FavoriteMovieEntry.COLUMN_MOVIE_ID;
import static com.example.android.explicitintent.favorite.FavoriteContract.FavoriteMovieEntry.CONTENT_URI;
import static com.example.android.explicitintent.favorite.FavoriteContract.FavoriteMovieEntry.MOVIE_PROJECTION;
import static com.example.android.explicitintent.util.utility.getMovieValues;

public class FullImageActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Result> {

    ImageView img;
    Bitmap bitmap;
    private TextView cTitle;
    private TextView cReleaseDate;
    private ImageView iPoster;
    private TextView cOverview;
    private TextView cAverage;
    private MenuItem menuItemFavorite;
    private boolean isFavoriteMovie = false;
    public static final String KEY_MOVIE_DATA = "movie data";
    private static final int FAVORITE_MOVIE_LOADER_ID = 212;
    private static Toast toast;
    //private ArrayList<TrailerResult> TrailerList;
    private List<TrailerResult> TrailerList = new ArrayList<>();
    //private List<Trailer> TrailerList = new ArrayList<>();

    //private ArrayList<ReviewsResult> ReviewsList;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fullimageview);

        Result result = getMovie();

        ActionBar actionBar = getSupportActionBar();
        if(actionBar !=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);

        }

//Movie details layout contains title, release date, movie poster, vote average, and plot synopsis.

        //result.getTitle();
        //result.getReleaseDate();
        //result.getPosterPath();
        //result.getOverview();
        //result.getVoteAverage();

        cTitle = (TextView) findViewById(R.id.title);
        cTitle.setText(result.getTitle());

        cReleaseDate = (TextView) findViewById(R.id.release_date);
        cReleaseDate.setText("Release Date: " + result.getReleaseDate());

        cOverview = (TextView) findViewById(R.id.overview);
        cOverview.setText("Overview: \n " + result.getOverview());

        cAverage = (TextView) findViewById(R.id.vote_average);
        cAverage.setText("Vote Average: " + result.getVoteAverage().toString());

        iPoster = (ImageView) findViewById(R.id.poster);
        Picasso.with(this).load("https://image.tmdb.org/t/p/w320" + result.getPosterPath()).into(iPoster);

        callVolleyTrailer(result.getId());

    }


//    private class StableArrayAdapter extends BaseAdapter {
//
//        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();
//        public StableArrayAdapter(Context context, int textViewResourceId,
//                                  List<String> objects) {
//            super(context, textViewResourceId, objects);
//            for (int i = 0; i < objects.size(); ++i) {
//                mIdMap.put(objects.get(i), i);
//            }
//        }
//
//        @Override
//        public int getCount() {
//            return 0;
//        }
//
//        @Override
//        public Object getItem(int i) {
//            return null;
//        }
//
//        @Override
//        public long getItemId(int position) {
//            String item = getItem(position);
//            return mIdMap.get(item);
//        }
//
//        @Override
//        public boolean hasStableIds() {
//            return true;
//        }
//
//        @Override
//        public View getView(int i, View view, ViewGroup viewGroup) {
//            return null;
//        }
//
//        @Override
//        public CharSequence[] getAutofillOptions() {
//            return new CharSequence[0];
//        }
//
//
//    }


    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                // NavUtils.navigateUpFromSameTask(this);//this will messed up shared element transition
                // please suggest me if you have solution
                onBackPressed();
                return true;
            }

            case R.id.action_favorite: {
                isFavoriteMovie = !isFavoriteMovie;
                if (isFavoriteMovie) {
                    toast = Toast.makeText(this, "added to favorite", Toast.LENGTH_LONG);
                    toast.show();
                    saveMovieToDb();
                } else {
                    toast = Toast.makeText(this, "removed from favorite", Toast.LENGTH_LONG);
                    toast.show();
                    deleteMovieFromDb();
                }
                toggleFavoriteIcon();
            }
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.full_image_menu, menu);
        menuItemFavorite = menu.findItem(R.id.action_favorite);
        getSupportLoaderManager().restartLoader(FAVORITE_MOVIE_LOADER_ID, null, this);
        return true;
    }




    private void toggleFavoriteIcon() {
        menuItemFavorite.setIcon(
                ResourcesCompat.getDrawable(
                        getResources(),
                        isFavoriteMovie ? R.drawable.ic_favorite : R.drawable.ic_favorite_border,
                        null
                )
        );
    }


    public void deleteMovieFromDb() {
        Result movieData = getMovie();

        final String where = String.format("%s=?", COLUMN_MOVIE_ID);
        final String[] args = new String[]{String.valueOf(movieData.getId())};
        getContentResolver().delete(CONTENT_URI, where, args);
    }

    public void saveMovieToDb() {
        Result movieData = getMovie();

        final ContentValues movieValues = getMovieValues(movieData);
        getContentResolver().insert(CONTENT_URI, movieValues);
    }




    @Override
    public void onLoadFinished(Loader<Result> loader, Result data) {
        isFavoriteMovie = data != null;
        toggleFavoriteIcon();
    }


    @Override
    public Loader<Result> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Result>(this) {

            @Override
            protected void onStartLoading() {
                forceLoad();
            }

            @Override
            public Result loadInBackground() {
                return getMovieFromDb();
            }

            @Override
            public void deliverResult(Result data) {
                super.deliverResult(data);
            }
        };
    }



    @Override
    public void onLoaderReset(Loader<Result> loader) {
    }

    private Result getMovie() {
        return getIntent().getParcelableExtra(KEY_MOVIE_DATA);
    }


    public Result getMovieFromDb() {
        Result movieData = getMovie();

        final String where = String.format("%s=?", COLUMN_MOVIE_ID);
        final String[] args = new String[]{String.valueOf(movieData.getId())};
        final Cursor cursor = getContentResolver().query(CONTENT_URI, MOVIE_PROJECTION, where, args, null);
        if (cursor != null && cursor.getCount() >= 1) {
            cursor.moveToFirst();
            return Result.fromCursor(cursor);
        } else {
            return null;
        }
    }




    private void callVolleyTrailer(String option) {

        String url = "https://api.themoviedb.org/3/movie/"+option+"/videos?api_key=1831fea099b5dc948f71ac983802ac79";
        GsonRequest<Trailer> gsonRequest =
                new GsonRequest<>(url, Trailer.class, null,
                        new Response.Listener<Trailer>() {
                            @Override
                            public void onResponse(Trailer trailer) {
                                // TODO do next things after response success
                                Log.d("","");
                                TrailerList = trailer.getTrailerResults() ;

                                //
                                //
                                // Trailer[] values = TrailerList.toArray(new Trailer[TrailerList.size()]);

                                final RecyclerView listview = (RecyclerView) findViewById(R.id.trailer_list);
                                listview.setAdapter(new TrailerAdapter(TrailerList));
                                listview.setLayoutManager(new LinearLayoutManager(FullImageActivity.this));

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO do next things after response error
                        Log.d("","");
                    }
                });
        VolleySingleton.getInstance(this).addToRequestQueue(gsonRequest, FullImageActivity.class);
    }



    /*@Override
    public void onResponse(@NonNull GetResultList currentResponse) {
        TrailerList = getTrailerResults();
        //gridview.setAdapter(new ImageAdapter(MainActivity.this, results));
    }*/

    @Override
    protected void onStop() {
        super.onStop();
        VolleySingleton.getInstance(this).cancelPendingRequests(MainActivity.class);
    }
}