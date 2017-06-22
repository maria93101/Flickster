package com.codepath.flickster;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.flickster.models.Config;
import com.codepath.flickster.models.Movie;

import org.parceler.Parcels;

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

    // config needed for image urls
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

        // determine the current orientation
        boolean isPortrait = context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;

        // build url for poster image
        String imageUrl = null;

        // if in portrait mode, load the poster image
        if (isPortrait)
        {
            imageUrl = config.getImageUrl(config.getPosterSize(), movie.getPosterPath());
        }
        else
        {
            // load the backdrop image
            imageUrl = config.getImageUrl(config.getBackdropSize(), movie.getBackdropPath());
        }

        // get the correct placeholder and imageview for the current orientation
        int placeholderId = isPortrait ? R.drawable.flicks_movie_placeholder : R.drawable.flicks_backdrop_placeholder;
        ImageView imageView = isPortrait ? holder.ivPosterImage : holder.ivBackdropImage;

        // load the image using glide
        Glide.with(context)
                .load(imageUrl)
                .bitmapTransform(new RoundedCornersTransformation(context, 15, 0))
                .placeholder(placeholderId)
                .error(placeholderId)
                .into(imageView);
    }

    // returns the total number of items in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    // create the viewholder as static inner class (nested)
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        // declare fields: track view objects
        ImageView ivPosterImage;
        ImageView ivBackdropImage;
        TextView tvTitle;
        TextView tvOverview;

        public ViewHolder(View itemView)
        {
            super(itemView);

            // lookup view objects by id
            ivPosterImage = (ImageView) itemView.findViewById(R.id.ivPosterImage);
            ivBackdropImage = (ImageView) itemView.findViewById(R.id.ivBackdropImage);
            tvOverview = (TextView) itemView.findViewById(R.id.tvOverview);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);

            // add this as the itemView's OnClickListener
            itemView.setOnClickListener(this);
        }

        public void onClick(View v)
        {
            // gets item position
            int position = getAdapterPosition();
            // make sure the position is valid, i.e. actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                // get the movie at the position, this won't work if the class is static
                Movie movie = movies.get(position);
                // create intent for the new activity
                Intent intent = new Intent(context, MovieDetailsActivity.class);
                // serialize the movie using parceler, use its short name as a key
                intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));
                // show the activity
                context.startActivity(intent);
            }
        }
    }
}
