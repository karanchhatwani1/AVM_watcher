package com.example.android.avmwatcher.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.android.avmwatcher.Model.GetVideoDetails;
import com.example.android.avmwatcher.Model.MoviesItemClickListenerNew;
import com.example.android.avmwatcher.R;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder> {

    private Context mContext;
    private List<GetVideoDetails> uploads;

    MoviesItemClickListenerNew moviesItemClickListenerNew;

    public MovieAdapter(Context mContext, List<GetVideoDetails> uploads, MoviesItemClickListenerNew moviesItemClickListenerNew) {
        this.mContext = mContext;
        this.uploads = uploads;
        this.moviesItemClickListenerNew = moviesItemClickListenerNew;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.movie_item,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.MyViewHolder holder, int position) {
        GetVideoDetails getVideoDetails = uploads.get(position);
        holder.tvTitle.setText(getVideoDetails.getVideo_name());
        Glide.with(mContext).load(getVideoDetails.getVideo_thumb()).into(holder.imgMovie);

    }

    @Override
    public int getItemCount() {
        return uploads.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvTitle;
        ImageView imgMovie;
        ConstraintLayout container;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.item_movie_title);
            imgMovie = itemView.findViewById(R.id.item_movies);
            container = itemView.findViewById(R.id.container);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    moviesItemClickListenerNew.onMovieClick(uploads.get(getAdapterPosition()),imgMovie);
                }
            });
        }
    }
}
