package com.codepath.flickster;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.flickster.models.Config;
import com.codepath.flickster.models.Movie;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by mariadeangelis on 6/21/17.
 */

// wrap list of movies

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder>
{
    // list of movies
    ArrayList<Movie> movies;

    // config needed for iumage urls
    Config config;

    // context for adapter
    Context context;

    public void setConfig(Config config) {
        this.config = config;
    }

    // initialize with list
    public MovieAdapter(ArrayList<Movie> movies) {
        this.movies = movies;
    }


    // creates and inflates a new view
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {

        // get the context from the parent
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // create the view using the item_movie layout
        View movieView = inflater.inflate(R.layout.item_movie, parent, false);

        // return a new viewholder
        return new ViewHolder(movieView);
    }

    // binds an inflated view to a new item
    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        // get the movie sata at the specified position
        Movie movie = movies.get(position);

        // populate the view with the movie data
        holder.tvTitle.setText(movie.getTitle());
        holder.tvOverview.setText(movie.getOverview());

        // build url for poster image
        String imageUrl = config.getImageUrl(config.getPosterSize(), movie.getPosterPath());

        // load the image using glide
        Glide.with(context)
                .load(imageUrl)
                .bitmapTransform(new RoundedCornersTransformation(context, 15, 0))
                .placeholder(R.drawable.flicks_movie_placeholder)
                .error(R.drawable.flicks_movie_placeholder)
                .into(holder.ivPosterImage);
    }

    // returns the total number of items in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    // create the viewholder as static inner class (nested)
    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        // declare fields: track view objects
        ImageView ivPosterImage;
        TextView tvTitle;
        TextView tvOverview;

        public ViewHolder(View itemView) {
            super(itemView);

            // lookup view objects by id
            ivPosterImage = (ImageView) itemView.findViewById(R.id.ivPosterImage);
            tvOverview = (TextView) itemView.findViewById(R.id.tvOverview);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
        }
    }
}
