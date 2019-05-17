package com.example.filmapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static com.example.filmapp.MovieInfoActivity.FAVORITE_ARRAY_LIST;
import static com.example.filmapp.MovieInfoActivity.SHARED_PREFS;

public class FavoritesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<MovieDetails> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        getSupportActionBar().hide();


        // Array uppackad från SharedPreferences
        ArrayList<String> movieIDArrayList = loadFavoritesFromPreferences();

        movies = new ArrayList<>();
        movieAdapter = new MovieAdapter(movies, this);
        recyclerView.setAdapter(movieAdapter);

        for (int i = 0; i < movieIDArrayList.size(); i++) {
            String id = movieIDArrayList.get(i);
            String url = "https://api.themoviedb.org/3/movie/" + id + "?api_key=7005ceb3ddacaaf788e2327647f0fa57&language=en-US";

            parseFavoriteMovies(url);
        }
        Log.d("Test", String.valueOf(movieAdapter.getItemCount()));
    }

    public ArrayList<String> loadFavoritesFromPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(FAVORITE_ARRAY_LIST, null);
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        ArrayList<String> arr = gson.fromJson(json, type);

        if (arr == null) {
            arr = new ArrayList<>();
        }
        return arr;
    }


    public  void parseFavoriteMovies(String url) {

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String poster = response.getString("backdrop_path");
                            String title =  response.getString("title");
                            String description = response.getString("overview");
                            String releaseDate = response.getString("release_date");

                            String fullPosterUrl = "https://image.tmdb.org/t/p/original" + poster;
                            MovieDetails movie = new MovieDetails(fullPosterUrl, title, releaseDate, description);
                            movies.add(movie);
                            movieAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
}
