package com.example.filmapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PopularMoiveAdapter popularMoiveAdapter;
    private ArrayList<MovieItem> movieList;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        movieList = new ArrayList<>();

        requestQueue = Volley.newRequestQueue(this);
        parseJson();
    }

    private void parseJson() {
        String url = "https://api.themoviedb.org/3/movie/upcoming?api_key=7005ceb3ddacaaf788e2327647f0fa57&language=en-US&page=1";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("results");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject result = jsonArray.getJSONObject(i);

                                String title = result.getString("original_title");
                                String release = result.getString("release_date");
                                String description = result.getString("overview");
                                String poster = result.getString("poster_path");

                                String fullPosterUrl = "http://image.tmdb.org/t/p/w185" + poster;
                                if (description.length() > 120) {
                                    String shortDescription = description.substring(0, 120) + "...";
                                    movieList.add(new MovieItem(title, fullPosterUrl, release, shortDescription));
                                } else {

                                    movieList.add(new MovieItem(title, fullPosterUrl, release, description));
                                }



                            }

                            popularMoiveAdapter = new PopularMoiveAdapter(MainActivity.this, movieList);
                            recyclerView.setAdapter(popularMoiveAdapter);

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
        requestQueue.add(request);
    }
}
