package com.example.android.explicitintent.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.explicitintent.network.model.ReviewsResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dionlusi on 8/20/17.
 */



public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsHolder> {

    private List<ReviewsResult> mList = new ArrayList<>();
    private Context mContext;

    public ReviewsAdapter(List<ReviewsResult> reviewsResults){
        this.mList = reviewsResults;
    }


    @Override
    public ReviewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext=parent.getContext();
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(
                        android.R.layout.simple_list_item_2, parent, false);
        return new ReviewsHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewsHolder holder, int position) {
        final ReviewsResult reviews = mList.get(position);
        holder.tv.setText(reviews.getContent());
        holder.tv_header.setText(reviews.getAuthor());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(reviews.getUrl())));
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mList != null)
        return mList.size();
        else return 0;
    }

    static class ReviewsHolder extends RecyclerView.ViewHolder {
        TextView tv;
        TextView tv_header ;
        public ReviewsHolder(View itemView) {
            super(itemView);
            tv_header = (TextView)itemView.findViewById(android.R.id.text1);
            tv = (TextView)itemView.findViewById(android.R.id.text2);
        }
    }
}
