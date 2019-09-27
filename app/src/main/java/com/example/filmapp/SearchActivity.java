package com.example.filmapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;

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
    private TextView firstTextView;
    private RecyclerView recycleView, castRecyclerView;
    private ArrayList<MovieItem> movieList;
    private ArrayList<MovieItem> mTvList;
    private ArrayList<CreditPerson> mPersonList;
    ShimmerLayout shimmerLayout;
    RequestQueue requestQueue;
    PopularMovieAdapter popularMovieAdapter;
    SearchView close;
    CastAdapter castAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        recycleView = findViewById(R.id.recycler_view1);
        castRecyclerView = findViewById(R.id.castRecyclerView_search);
        recycleView.setHasFixedSize(true);
        recycleView.setLayoutManager(new LinearLayoutManager(this));
        castRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


        movieList = new ArrayList<>();
        mTvList = new ArrayList<>();
        mPersonList = new ArrayList<CreditPerson>();
        shimmerLayout = findViewById(R.id.shimmer_layout_copy);
        requestQueue = Volley.newRequestQueue(this);


        searchView = findViewById(R.id.searchView);
        searchView.setFocusable(true);

        searchMovies();
        firstTextView = (TextView) findViewById(R.id.tvStatus);
        firstTextView.setVisibility(View.VISIBLE);
        searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSearchMovies();
            }
        });

        close = findViewById(R.id.searchView);

        close.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                movieList.clear();
                mTvList.clear();
                mPersonList.clear();
                firstTextView.setVisibility(View.VISIBLE);

                return false;
            }
        });

        setupBottomNavView();

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


        String url = "https://api.themoviedb.org/3/search/multi?api_key=7005ceb3ddacaaf788e2327647f0fa57&language=en-US&query=" + searchString + "&page=1&include_adult=false";

        Log.d("Sui url", "is " + url);
        movieList.clear();
        mTvList.clear();
        mPersonList.clear();
        firstTextView.setVisibility(View.VISIBLE);

        parseJson(url);

    }


    private void parseJson(String url) {

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
                                firstTextView.setVisibility(View.GONE);

                                JSONObject result = jsonArray.getJSONObject(i);
                                if (result.getString("media_type").equals("movie")) {


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

                                    Log.d("Sui", "movieList" + movieList);
                                } else if (result.getString("media_type").equals("person")) {
                                    String name = result.getString("name");
                                    String id = result.getString("id");
                                    String character = result.getString("popularity");
                                    String profileImage = result.getString("profile_path");

                                    if (!profileImage.equals("null")) {
                                        String fullProfileImageUrl = "http://image.tmdb.org/t/p/w185" + profileImage;
                                        mPersonList.add(new CreditPerson(name, id, fullProfileImageUrl, character));
                                    } else {
                                        String dummyPic = "https://emgroupuk.com/wp-content/uploads/2018/06/profile-icon-9.png";
                                        mPersonList.add(new CreditPerson(name, id, dummyPic, character));
                                    }
                                }


                            }

                            popularMovieAdapter = new PopularMovieAdapter(SearchActivity.this, movieList);
                            castAdapter = new CastAdapter(SearchActivity.this, mPersonList);

                            castRecyclerView.setAdapter(castAdapter);

                            recycleView.setAdapter(popularMovieAdapter);

                            Log.d("Sui recycleView", "is added");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

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

    private void setupBottomNavView() {
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bnve);
        BottomNavigationViewHelper.setypBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(SearchActivity.this, bottomNavigationViewEx);
    }


}
