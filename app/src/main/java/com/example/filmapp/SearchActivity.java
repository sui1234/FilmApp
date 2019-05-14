package com.example.filmapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.filmapp.BottomNavMenu.BottomNavigationViewHelper;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.supercharge.shimmerlayout.ShimmerLayout;

public class SearchActivity extends AppCompatActivity {

    private Button searchButton;
    private SearchView searchView;
    private String searchString;
    private RecyclerView recycleView;
    private ArrayList<MovieItem> movieList;
    ShimmerLayout shimmerLayout;
    RequestQueue requestQueue;
    PopularMoiveAdapter popularMoiveAdapter;
    SearchView close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        getSupportActionBar().hide();
        recycleView = findViewById(R.id.recycler_view1);
        recycleView.setHasFixedSize(true);
        recycleView.setLayoutManager(new LinearLayoutManager(this));


        movieList = new ArrayList<>();
        shimmerLayout = findViewById(R.id.shimmer_layout_copy);
        requestQueue = Volley.newRequestQueue(this);


        searchView = findViewById(R.id.searchView);
        searchMovies();

        searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSearchMovies();
            }
        });

        close= findViewById(R.id.searchView);

        close.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
               movieList.clear();
                return false;
            }
        });

        setuoBottomnavView();

    }

    private void searchMovies() {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                searchView.getQuery();
                searchString = searchView.getQuery().toString();
                return false;
            }

        });
    }

    private void getSearchMovies() {

        String url = "https://api.themoviedb.org/3/search/movie?api_key=7005ceb3ddacaaf788e2327647f0fa57&language=en-US&query=" + searchString + "&page=1";

        Log.d("Sui url", "is " + url);

        parseJson(url);

    }


    private void parseJson(String url) {
        //final ProgressDialog dialog = ProgressDialog.show(this, null, "Filmer laddas....");
        shimmerLayout.startShimmerAnimation();

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {



                        try {
                            JSONArray jsonArray = null;
                            jsonArray = response.getJSONArray("results");

                            movieList.clear();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject result = jsonArray.getJSONObject(i);

                                String title = result.getString("original_title");
                                String release = result.getString("release_date");
                                String description = result.getString("overview");
                                String poster = result.getString("poster_path");
                                String id = result.getString("id");

                                String fullPosterUrl = "http://image.tmdb.org/t/p/w500" + poster;

                                if (description.length() > 120) {
                                    description = description.substring(0, 120) + "...";
                                }
                                movieList.add(new MovieItem(title, fullPosterUrl, release, description, id));

                                Log.d("Sui","movieList" + movieList);

                            }

                            popularMoiveAdapter = new PopularMoiveAdapter(SearchActivity.this, movieList);

                            recycleView.setAdapter(popularMoiveAdapter);

                            Log.d("Sui recycleView","is added");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        movieList.remove(0);

                        shimmerLayout.stopShimmerAnimation();
                        shimmerLayout.setVisibility(View.GONE);
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(request);

    }

    private void setuoBottomnavView(){
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bnve);
        BottomNavigationViewHelper.setypBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(SearchActivity.this, bottomNavigationViewEx);
    }


}
