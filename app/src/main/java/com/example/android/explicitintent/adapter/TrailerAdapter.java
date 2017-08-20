package com.example.android.explicitintent.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.explicitintent.network.model.TrailerResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dionlusi on 8/20/17.
 */



public class TrailerAdapter  extends RecyclerView.Adapter<TrailerAdapter.TrailerHolder> {

    private List<TrailerResult> mList = new ArrayList<>();
    private Context mContext;

    public TrailerAdapter(List<TrailerResult> trailerResults){
        this.mList = trailerResults;
    }


    @Override
    public TrailerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext=parent.getContext();
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(
                        android.R.layout.simple_list_item_1, parent, false);
        return new TrailerHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerHolder holder, int position) {
        final TrailerResult trailer = mList.get(position);
        holder.tv.setText(trailer.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v="+trailer.getKey())));
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mList != null)
        return mList.size();
        else return 0;
    }

    static class TrailerHolder extends RecyclerView.ViewHolder {
        TextView tv;
        public TrailerHolder(View itemView) {
            super(itemView);
            tv = (TextView)itemView.findViewById(android.R.id.text1);
        }
    }
}
