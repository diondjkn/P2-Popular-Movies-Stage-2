package com.example.android.explicitintent.network.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.android.explicitintent.favorite.FavoriteContract;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class Result implements Parcelable {

    @SerializedName("vote_count")
    @Expose
    private Integer voteCount;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("video")
    @Expose
    private Boolean video;
    @SerializedName("vote_average")
    @Expose
    private Double voteAverage;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("popularity")
    @Expose
    private Double popularity;
    @SerializedName("poster_path")
    @Expose
    private String posterPath;
    @SerializedName("original_language")
    @Expose
    private String originalLanguage;
    @SerializedName("original_title")
    @Expose
    private String originalTitle;
    @SerializedName("genre_ids")
    @Expose
    private List<Integer> genreIds = null;
    @SerializedName("backdrop_path")
    @Expose
    private String backdropPath;
    @SerializedName("adult")
    @Expose
    private Boolean adult;
    @SerializedName("overview")
    @Expose
    private String overview;
    @SerializedName("release_date")
    @Expose
    private String releaseDate;

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getVideo() {
        return video;
    }

    public void setVideo(Boolean video) {
        this.video = video;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public Boolean getAdult() {
        return adult;
    }

    public void setAdult(Boolean adult) {
        this.adult = adult;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.posterPath);
        dest.writeString(this.originalTitle);
        dest.writeString(this.title);
        dest.writeString(this.overview);
        dest.writeString(this.releaseDate);
        dest.writeString(this.backdropPath);
        dest.writeValue(this.voteAverage);
        dest.writeList(this.genreIds);
    }


    public Result() {
    }

    protected Result(Parcel in) {
        this.id = in.readString();
        this.posterPath = in.readString();
        this.originalTitle = in.readString();
        this.title = in.readString();
        this.overview = in.readString();
        this.releaseDate = in.readString();
        this.backdropPath = in.readString();
        this.voteAverage = (Double) in.readValue(Float.class.getClassLoader());
        this.genreIds = new ArrayList<>();
        in.readList(this.genreIds, Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<Result> CREATOR = new Parcelable.Creator<Result>() {
        @Override
        public Result createFromParcel(Parcel source) {
            return new Result(source);
        }

        @Override
        public Result[] newArray(int size) {
            return new Result[size];
        }
    };

    public static Result fromCursor(Cursor query) {
        Result result = new Result();
        result.setId(query.getString(query.getColumnIndex(FavoriteContract.FavoriteMovieEntry.COLUMN_MOVIE_ID)));
        result.setPosterPath(query.getString(query.getColumnIndex(FavoriteContract.FavoriteMovieEntry.COLUMN_POSTER_PATH)));
        result.setOriginalTitle(query.getString(query.getColumnIndex(FavoriteContract.FavoriteMovieEntry.COLUMN_ORIGINAL_TITLE)));
        result.setTitle(query.getString(query.getColumnIndex(FavoriteContract.FavoriteMovieEntry.COLUMN_TITLE)));
        result.setOverview(query.getString(query.getColumnIndex(FavoriteContract.FavoriteMovieEntry.COLUMN_OVERVIEW)));
        result.setReleaseDate(query.getString(query.getColumnIndex(FavoriteContract.FavoriteMovieEntry.COLUMN_RELEASE_DATE)));
        result.setBackdropPath(query.getString(query.getColumnIndex(FavoriteContract.FavoriteMovieEntry.COLUMN_BACKDROP_PATH)));
        result.setVoteAverage(query.getDouble(query.getColumnIndex(FavoriteContract.FavoriteMovieEntry.COLUMN_VOTE_AVERAGE)));
        Gson gson = new Gson();
        String genreIds = query.getString(query.getColumnIndex(FavoriteContract.FavoriteMovieEntry.COLUMN_GENRE_IDS));
        List<Integer> genreIdList = gson.fromJson(genreIds, new TypeToken<List<Integer>>() {
        }.getType());
        result.setGenreIds(genreIdList);
        return result;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id='" + id + '\'' +
                ", posterPath='" + posterPath + '\'' +
                ", originalTitle='" + originalTitle + '\'' +
                ", title='" + title + '\'' +
                ", overview='" + overview + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", backdropPath='" + backdropPath + '\'' +
                ", voteAverage=" + voteAverage +
                ", genreIds=" + genreIds +
                '}';
    }


}