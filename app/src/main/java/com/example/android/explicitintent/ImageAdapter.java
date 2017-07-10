package com.example.android.explicitintent;

/**
 * Created by dionlusi on 7/9/17.
 */

import com.squareup.picasso.Picasso;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    int imageTotal = 7;
    public static String[] mThumbIds = {
            "https://image.tmdb.org/t/p/w320/tWqifoYuwLETmmasnGHO7xBjEtt.jpg",
            "https://image.tmdb.org/t/p/w320/5qcUGqWoWhEsoQwNUrtf3y3fcWn.jpg",
            "https://image.tmdb.org/t/p/w320/imekS7f1OuHyUP2LAiTEM0zBzUz.jpg",
            "https://image.tmdb.org/t/p/w320/5qcUGqWoWhEsoQwNUrtf3y3fcWn.jpg",
            "https://image.tmdb.org/t/p/w320/5qcUGqWoWhEsoQwNUrtf3y3fcWn.jpg",
            "https://image.tmdb.org/t/p/w320/5qcUGqWoWhEsoQwNUrtf3y3fcWn.jpg",
            "https://image.tmdb.org/t/p/w320/5qcUGqWoWhEsoQwNUrtf3y3fcWn.jpg",

    };

    public ImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return imageTotal;
    }

    @Override
    public String getItem(int position) {
        return mThumbIds[position];
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(480, 480));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }
        String url = getItem(position);
        Picasso.with(mContext)
                .load(url)
                .placeholder(R.drawable.loader)
                .fit()
                .centerCrop().into(imageView);
        return imageView;
    }
}