package com.example.filmapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

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
    private PopularMovieAdapter popularMovieAdapter;
    private ArrayList<MovieItem> movieList;
    private RequestQueue requestQueue;
    private SearchView searchView;
    private PopularMovieAdapter getMovieAdapter;


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

        searchView = findViewById(R.id.search_view);
        searchMovies();

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

                            popularMovieAdapter = new PopularMovieAdapter(MainActivity.this, movieList);
                            recyclerView.setAdapter(popularMovieAdapter);

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


    private void searchMovies(){

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("Sui text","submit");
                searchView.getQuery();

                Log.d("Sui","get query " + searchView.getQuery());
                Toast.makeText(getApplicationContext(),query,Toast.LENGTH_SHORT).show();

                getSearchMovies();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                //Object [] editText = searchItem(newText);
                Log.d("Sui text","change");
                if (!TextUtils.isEmpty(newText)){
                    //movieList.setFilterText(newText);
                }else{
                    //movieList.clearTextFilter();
                }
                return false;
            }

        });
    }

    private void getSearchMovies(){

        String url = "https://api.themoviedb.org/3/search/multi?api_key=7005ceb3ddacaaf788e2327647f0fa57&language=en-US&query=" + searchView.getQuery().toString() +  "&page=1";

        Log.d("Sui","get url " + url);

        Log.d("SuiQuery", url);

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Sui","onResponse");
                        movieList.clear();

                        try {
                            JSONArray jsonArray = response.getJSONArray("results");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject result = jsonArray.getJSONObject(i);
                                Log.d("SuiForLoop","get jsonArray " + i);
                                Log.d("SuiForLoop","result" + result.toString());
                                Log.d("SuiArrayLength"," "+jsonArray.length());

                                try {
                                    String title = result.getString("original_title");
                                    String release = result.getString("release_date");
                                    String description = result.getString("overview");
                                    String poster = result.getString("poster_path");
                                    Log.d("SuiGetTitle","title " + title);

                                    String fullPosterUrl = "http://image.tmdb.org/t/p/w185" + poster;

                                    if (description.length() > 120) {
                                        String shortDescription = description.substring(0, 120) + "...";
                                        movieList.add(new MovieItem(title, fullPosterUrl, release, shortDescription));

                                        Log.d("Sui","movielist added");

                                    } else {
                                        Log.d("SearchResult", "No results found, please search for other things");
                                        movieList.add(new MovieItem(title, fullPosterUrl, release, description));
                                    }
                                } catch (Exception E) {
                                    continue;
                                }

                            }

                            Log.d("SuiMovieList"," "+ movieList.toArray().toString());
                            getMovieAdapter = new PopularMovieAdapter(MainActivity.this, movieList);
                            recyclerView.setAdapter(getMovieAdapter);
                            Log.d("SuiGetMovie","successful");

                            Log.d("Sui clear","movieList");

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
        Log.d("Sui","requestQueue");


    }
}
