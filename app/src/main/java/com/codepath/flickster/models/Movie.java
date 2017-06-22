package com.codepath.flickster.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

/**
 * Created by mariadeangelis on 6/21/17.
 */

// track all info for movie to display
@Parcel   // annotation class is Parcelable!
public class Movie {

    // values from API, public for parceler
    private String title;
    private String overview;
    private String posterPath; // only the path
    private String backdropPath;

    Double voteAverage; // value returned from API

    //  no arg contructor required for Par.
    public Movie(){}

    // initialize the JSON data
    public Movie(JSONObject object) throws JSONException {
        title = object.getString("title");
        overview = object.getString("overview");
        posterPath = object.getString("poster_path");
        backdropPath = object.getString("backdrop_path");

        voteAverage = object.getDouble("vote_average");
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }
}
