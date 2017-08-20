package com.example.android.explicitintent.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Trailer implements Serializable, Parcelable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("results")
    @Expose
    private List<TrailerResult> TrailerResults = null;
    public final static Parcelable.Creator<Trailer> CREATOR = new Creator<Trailer>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Trailer createFromParcel(Parcel in) {
            Trailer instance = new Trailer();
            instance.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
            in.readList(instance.TrailerResults, (com.example.android.explicitintent.network.model.TrailerResult.class.getClassLoader()));
            return instance;
        }

        public Trailer[] newArray(int size) {
            return (new Trailer[size]);
        }

    }
            ;
    private final static long serialVersionUID = -8803814057958663192L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<TrailerResult> getTrailerResults() {
        return TrailerResults;
    }

    public void setTrailerResults(List<TrailerResult> TrailerResults) {
        this.TrailerResults = TrailerResults;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeList(TrailerResults);
    }

    public int describeContents() {
        return 0;
    }

}

