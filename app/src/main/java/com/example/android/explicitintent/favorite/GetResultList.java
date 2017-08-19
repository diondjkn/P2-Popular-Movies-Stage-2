package com.example.android.explicitintent.favorite;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.android.explicitintent.network.model.Result;

import java.util.ArrayList;
/**
 * Created by dionlusi on 8/12/17.
 */

public class GetResultList implements Parcelable {

    private int page;
    private ArrayList<Result> results;
    private int total_results;
    private int total_pages;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public ArrayList<Result> getResults() {
        return results;
    }

    public void setResults(ArrayList<Result> results) {
        this.results = results;
    }

    public int getTotal_results() {
        return total_results;
    }

    public void setTotal_results(int total_results) {
        this.total_results = total_results;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.page);
        dest.writeTypedList(this.results);
        dest.writeInt(this.total_results);
        dest.writeInt(this.total_pages);
    }

    public GetResultList() {
    }

    protected GetResultList(Parcel in) {
        this.page = in.readInt();
        this.results = in.createTypedArrayList(Result.CREATOR);
        this.total_results = in.readInt();
        this.total_pages = in.readInt();
    }

    public static final Creator<GetResultList> CREATOR = new Creator<GetResultList>() {
        @Override
        public GetResultList createFromParcel(Parcel source) {
            return new GetResultList(source);
        }

        @Override
        public GetResultList[] newArray(int size) {
            return new GetResultList[size];
        }
    };
}
