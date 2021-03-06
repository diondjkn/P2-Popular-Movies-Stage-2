package com.example.android.explicitintent;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.NestedScrollView;
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
import com.example.android.explicitintent.adapter.ReviewsAdapter;
import com.example.android.explicitintent.adapter.TrailerAdapter;
import com.example.android.explicitintent.network.base.VolleySingleton;
import com.example.android.explicitintent.network.model.Result;
import com.example.android.explicitintent.network.model.Reviews;
import com.example.android.explicitintent.network.model.ReviewsResult;
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
    private static final String BUNDLE_REVIEW_LIST = "review_list";
    private static final String BUNDLE_VIDEO_LIST = "video_list";
    private static final String BUNDLE_REVIEW_LAYOUT = "review_layout";
    private static final String BUNDLE_TRAILER_LAYOUT = "trailer_layout";


    public static final String KEY_MOVIE_DATA = "movie data";
    private static final int FAVORITE_MOVIE_LOADER_ID = 212;
    private static final String BUNDLE_SCROLL_POSITION = "scroll_position";
    private static Toast toast;
    private List<TrailerResult> TrailerList = new ArrayList<>();
    private List<ReviewsResult> ReviewList = new ArrayList<>();
    RecyclerView listTrailerView;
    RecyclerView listReviewView;
    NestedScrollView sv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fullimageview);

        Result result = getMovie();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);

        }




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

        listTrailerView = (RecyclerView) findViewById(R.id.trailer_list);
        listReviewView = (RecyclerView) findViewById(R.id.reviews_list);
        createTrailerRV();
        createReviewRV();

        callVolleyTrailer(result.getId());
        callVolleyReviews(result.getId());

 //       android:nestedScrollingEnabled="true"

         //final NestedScrollView sv = (NestedScrollView)findViewById(R.id.fullScrollView);
        //sv.fullScroll(sv.getBottom());
        //sv.scrollTo(0, 1200);
        //sv.scrollBy(0, 100);
//        sv.post(new Runnable() {
//            @Override
//            public void run() {
//                sv.scrollTo(0, 1000);
//            }
//        });

//        if (savedInstanceState != null && savedInstanceState.containsKey(BUNDLE_SCROLL_POSITION)) {
//            final int[] scrollPosition = savedInstanceState.getIntArray(BUNDLE_SCROLL_POSITION);
//            if (scrollPosition != null && scrollPosition.length == 2) {

//                scrollView.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        scrollView.scrollTo(0, 100);
//                    }
//                }
//                );

//            }
//        }
        if(savedInstanceState != null)
        {
            Parcelable savedReviewLayoutState = savedInstanceState.getParcelable(BUNDLE_REVIEW_LAYOUT);
            listReviewView.getLayoutManager().onRestoreInstanceState(savedReviewLayoutState);
            Parcelable savedTrailerLayoutState = savedInstanceState.getParcelable(BUNDLE_TRAILER_LAYOUT);
            listTrailerView.getLayoutManager().onRestoreInstanceState(savedTrailerLayoutState);
        }

        sv = (NestedScrollView)findViewById(R.id.fullScrollView);

        if (savedInstanceState != null && savedInstanceState.containsKey(BUNDLE_SCROLL_POSITION)) {
            final int[] scrollPosition = savedInstanceState.getIntArray(BUNDLE_SCROLL_POSITION);
            if (scrollPosition != null && scrollPosition.length == 2) {
                sv.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        sv.smoothScrollTo(scrollPosition[0], scrollPosition[1]);
                    }
                }, 1000L);
            }
        }


    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        ArrayList<Step> stepsSaved = new ArrayList<>(stepAdapter.getSteps());
//        if (stepsSaved != null && !stepsSaved.isEmpty()) {
//            outState.putParcelableArrayList(SAVED_STEPS_KEY, stepsSaved);
//        }
        outState.putParcelable(BUNDLE_REVIEW_LAYOUT, listReviewView.getLayoutManager().onSaveInstanceState());
        outState.putParcelable(BUNDLE_TRAILER_LAYOUT, listTrailerView.getLayoutManager().onSaveInstanceState());
        outState.putIntArray(BUNDLE_SCROLL_POSITION, new int[]{sv.getScrollX(), sv.getScrollY()});
    }





//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        if (TrailerList != null && !TrailerList.isEmpty()) {
//            //outState.putParcelableArrayList(BUNDLE_VIDEO_LIST, TrailerList);
//        }
//        if (ReviewList != null && !ReviewList.isEmpty()) {
//            //outState.putParcelableArrayList(BUNDLE_REVIEW_LIST, ReviewList);
//        }
//         NestedScrollView sv = (NestedScrollView)findViewById(R.id.fullScrollView);
//        outState.putIntArray(BUNDLE_SCROLL_POSITION, new int[]{sv.getScrollX(), sv.getScrollY()});
//    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {

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
                //sv.smoothScrollTo(0,1000);
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

        String url = "https://api.themoviedb.org/3/movie/" + option + "/videos?api_key=";
        GsonRequest<Trailer> gsonRequest =
                new GsonRequest<>(url, Trailer.class, null,
                        new Response.Listener<Trailer>() {
                            @Override
                            public void onResponse(Trailer trailer) {
                                // TODO do next things after response success
                                Log.d("", "");
                                TrailerList = trailer.getTrailerResults();
                                createTrailerRV();

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO do next things after response error
                        Log.d("", "");
                    }
                });
        VolleySingleton.getInstance(this).addToRequestQueue(gsonRequest, FullImageActivity.class);
    }


    private void createTrailerRV(){
        listTrailerView.setAdapter(new TrailerAdapter(TrailerList));
        listTrailerView.setLayoutManager(new LinearLayoutManager(FullImageActivity.this));

    }

    private void callVolleyReviews(String option) {

        String url = "https://api.themoviedb.org/3/movie/" + option + "/reviews?api_key=";
        GsonRequest<Reviews> gsonRequest =
                new GsonRequest<>(url, Reviews.class, null,
                        new Response.Listener<Reviews>() {
                            @Override
                            public void onResponse(Reviews reviews) {
                                // TODO do next things after response success
                                Log.d("", "");
                                ReviewList = reviews.getReviewsResults();
                                createReviewRV();

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO do next things after response error
                        Log.d("", "");
                    }
                });
        VolleySingleton.getInstance(this).addToRequestQueue(gsonRequest, FullImageActivity.class);
    }

    private void createReviewRV(){
        listReviewView.setAdapter(new ReviewsAdapter(ReviewList));
        listReviewView.setLayoutManager(new LinearLayoutManager(FullImageActivity.this));

    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onStop() {
        super.onStop();
        VolleySingleton.getInstance(this).cancelPendingRequests(MainActivity.class);
    }
}