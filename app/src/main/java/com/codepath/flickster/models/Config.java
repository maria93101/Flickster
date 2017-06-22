package com.codepath.flickster.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mariadeangelis on 6/21/17.
 */

public class Config
{
    // the base URL for loading images
    String imageBaseUrl;

    // the poster size to use when fetching images, part of the url
    String posterSize;

    // the backdrop size to use when fetching images
    String backdropSize;

    public String getImageBaseUrl() {
        return imageBaseUrl;
    }

    public String getPosterSize() {
        return posterSize;
    }

    // helper method for creating urls
    public String getImageUrl(String size, String path)
    {
        return String.format("%s%s%s", imageBaseUrl, size, path); // join all three
    }

    public String getBackdropSize() {
        return backdropSize;
    }

    public Config(JSONObject object) throws JSONException
    {
        JSONObject images = object.getJSONObject("images");
        // get the image base URL
        imageBaseUrl = images.getString("secure_base_url");

        // get the poster size
        JSONArray posterSizeOptions = images.getJSONArray("poster_sizes");

        // use the option at index 3 : w342
        posterSize = posterSizeOptions.optString(3, "w342");

        // parse backdrop sizes and uses the option at index 1
        JSONArray backdropSizeOptions = images.getJSONArray("backdrop_sizes");
        backdropSize = backdropSizeOptions.optString(1, "w780");
    }
}
