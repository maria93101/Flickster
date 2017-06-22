package com.codepath.flickster;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.codepath.flickster.models.Config;
import com.codepath.flickster.models.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MovieListActivity extends AppCompatActivity {

    // constants for string values

    public final static String API_BASE_URL = "https://api.themoviedb.org/3";

    // parameter name for the API key
    public final static String API_KEY_PARAM = "api_key";

    // tag for all logging calls for class
    public final static String TAG = "MovieListActivity";

    // Instance fields
    AsyncHttpClient client;

    // the list of currently playing movies
    ArrayList<Movie> movies;

    // the recycler view
    RecyclerView rvMovies;

    // the adapter wired to the recycler view
    MovieAdapter adapter;

    // image config
    Config config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        // initailize the client
        client = new AsyncHttpClient();

        // iniatialize the list of moviews
        movies = new ArrayList<>();

        // initailize the adapter - movies array cannot be reinitaiozed after here
        adapter = new MovieAdapter(movies);

        // resolve the recycler view and connect a layout manager and the adapter
        rvMovies = (RecyclerView) findViewById(R.id.rvMovies);
        rvMovies.setLayoutManager(new LinearLayoutManager(this));
        rvMovies.setAdapter(adapter);

        // get the configuration on app creation
        getConfiguration();
    }

    // get the list of currently playing movies from the API
    private void getNowPlaying()
    {
        // create the URL that we want to access
        String url = API_BASE_URL + "/movie/now_playing";

        // set up the request paramenters
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key)); // API KEY, always required!!!

        client.get(url, params, new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                // parse JSON data coming back
                // loading results into moview list
                try {
                    JSONArray results = response.getJSONArray("results");

                    // iterate through result set and create Movie objects
                    for (int i = 0; i < results.length(); i++)
                    {
                        Movie movie = new Movie(results.getJSONObject(i));
                        movies.add(movie); // add to list

                        // notify adapter that a row was added
                        adapter.notifyItemInserted(movies.size() - 1);
                    }
                    Log.i(TAG, String.format("Loaded %s movies", results.length())); // completed successfully

                } catch (JSONException e) {
                    logError("Failed to parse now playing movies", e, true);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("Failed to get data from now_laying endpoint", throwable, true);
            }
        });
    }


    // get the configuration from the API
    private void getConfiguration()
    {
        // create the URL that we want to access
        String url = API_BASE_URL + "/configuration";

        // set up the request paramenters
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key)); // API KEY, always required!!!

        // execute a GET request excpecting a JSON object reponse
        client.get(url, params, new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //super.onSuccess(statusCode, headers, response); // calls super class implementation

                try
                {
                    config = new Config(response);

                    Log.i(TAG, String.format("Loaded configuration with imageBaseUrl %s and postersize %s",
                            config.getImageBaseUrl(),
                            config.getPosterSize()));

                    // pass config to adapter
                    adapter.setConfig(config);

                    // get the now playing movie list
                    getNowPlaying();
                }
                catch (JSONException e)
                {
                    logError("Failed parsing configuration!", e, true);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                // replace old super call with our own call
                logError("Failed getting configuration", throwable, true);
            }
        });
    }

    // error logging helper to avoid silent failures!

    private void logError(String message, Throwable error, boolean alertUser)
    {
        Log.e(TAG, message, error);

        // alert the user if something went wrong
        if (alertUser)
        {
            // show a long toast with error message
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
    }
}

