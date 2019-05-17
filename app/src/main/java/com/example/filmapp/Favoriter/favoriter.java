package com.example.filmapp.Favoriter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.supercharge.shimmerlayout.ShimmerLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.filmapp.BottomNavMenu.BottomNavigationViewHelper;
import com.example.filmapp.MainActivity;
import com.example.filmapp.MovieInfoActivity;
import com.example.filmapp.MovieItem;
import com.example.filmapp.PopularMoiveAdapter;
import com.example.filmapp.R;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class favoriter extends AppCompatActivity {
    RequestQueue requestQueue;
    private static final String TAG = "favoriter";
    ShimmerLayout shimmerLayout;
    private ArrayList<MovieItem> movieList, movileList2;
    private PopularMoiveAdapter popularMoiveAdapter;
    private RecyclerView recyclerView, mRecycleViewRound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoriter);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        shimmerLayout = findViewById(R.id.shimmer_layout1s);
        recyclerView = findViewById(R.id.recycler_view_fav);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        movieList = new ArrayList<>();
        setuoBottomnavView();
        parseMovieInfoJson();
    }
    private void parseMovieInfoJson() {
        shimmerLayout.startShimmerAnimation();
        String url = "https://api.themoviedb.org/3/account/{account_id}/favorite/movies?api_key=7005ceb3ddacaaf788e2327647f0fa57&session_id=0f864750f3c93c321b9972aa3fab11afdfed7226&language=en-US&sort_by=created_at.asc&page=1\n";
        requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
//                          hämtar genres
                            JSONArray jsonArray = response.getJSONArray("results");
                            Log.d(TAG, "onResponse: "+jsonArray);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject result = jsonArray.getJSONObject(i);

                                String title = result.getString("original_title");
                                String release = result.getString("release_date");
                                String description = result.getString("overview");
                                String poster = result.getString("poster_path");
                                String id     = result.getString("id");

                                String fullPosterUrl = "http://image.tmdb.org/t/p/w500" + poster;

                                if (description.length() > 120) {
                                    description = description.substring(0, 120) + "...";
                                }
                                movieList.add(new MovieItem(title, fullPosterUrl, release, description,id));

                            }
                            Log.d(TAG, "onResponse: ++"+movieList);
                            popularMoiveAdapter = new PopularMoiveAdapter(favoriter.this, movieList);
                            recyclerView.setAdapter(popularMoiveAdapter);

                            shimmerLayout.stopShimmerAnimation();
                            shimmerLayout.setVisibility(View.GONE);


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
    private void setuoBottomnavView(){
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bnve_fav);
        BottomNavigationViewHelper.setypBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(favoriter.this, bottomNavigationViewEx);
    }
}
