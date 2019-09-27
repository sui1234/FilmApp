package com.example.filmapp;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.filmapp.BottomNavMenu.BottomNavigationViewHelper;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import io.supercharge.shimmerlayout.ShimmerLayout;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView, mRecycleViewRound;
    private PopularMovieAdapter popularMovieAdapter;
    private RelativeLayout today_rel;
    private CircleMovieAdapter circleMovieAdapter;
    private ArrayList<MovieItem> movieList, movieList2;
    private RequestQueue requestQueue, requestQueue2;
    private TextView mTodayMovieTitle, mTodayMovieRate, mText, mTodayMovieId;
    private ImageView mTodayMovieImage;

    private JSONArray jsonGenres;
    private String genres, locale;

    private Spinner mDropDown;
    ShimmerLayout shimmerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //tar bort status bar (där klockan ligger)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);


        mDropDown = findViewById(R.id.spinner);
        mRecycleViewRound = findViewById(R.id.circleMovies);
        mRecycleViewRound.setHasFixedSize(true);
        mRecycleViewRound.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        mTodayMovieId = findViewById(R.id.today_movie_id);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        movieList = new ArrayList<>();
        movieList2 = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);
        requestQueue2 = Volley.newRequestQueue(this);
        mText = findViewById(R.id.group_num);

        shimmerLayout = findViewById(R.id.shimmer_layout);

        locale = Locale.getDefault().getLanguage() + "-" + Locale.getDefault().getCountry();
        today_rel = (RelativeLayout) findViewById(R.id.today_relative);

        ArrayAdapter<String> dropdownAdapter = new ArrayAdapter<String>(MainActivity.this,
                R.layout.dropdown_item, getResources().getStringArray(R.array.dropdownArray));
        dropdownAdapter.setDropDownViewResource(R.layout.dropdown_item);
        today_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MovieInfoActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putString("movie_id", String.valueOf(mTodayMovieId.getText()));
                intent.putExtras(mBundle);
                startActivity(intent);
            }
        });
        mDropDown.setAdapter(dropdownAdapter);
        mDropDown.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        ((TextView) view).setTextColor(Color.WHITE);
                        Object item = parent.getItemAtPosition(pos);
                        Log.d(TAG, "onItemSelected: " + item.toString());
                        if (item.toString().equals("På bio nu")) {

                            parseJson("https://api.themoviedb.org/3/movie/now_playing?api_key=7005ceb3ddacaaf788e2327647f0fa57&language=" + locale + "&page=1");

                        }
                        if (item.toString().equals("Top listan")) {
                            parseJson("https://api.themoviedb.org/3/movie/top_rated?api_key=7005ceb3ddacaaf788e2327647f0fa57&language=" + locale + "&page=1\n");

                        }
                        if (item.toString().equals("Kommande filmer")) {
                            parseJson("https://api.themoviedb.org/3/movie/upcoming?api_key=7005ceb3ddacaaf788e2327647f0fa57&language=" + locale + "page=1");

                        }
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });


        parseJson("https://api.themoviedb.org/3/movie/upcoming?api_key=7005ceb3ddacaaf788e2327647f0fa57&language=" + locale + "&page=1");
        setupBottomNavView();
        overridePendingTransition(0, 0);

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
                            movieList2.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject result = jsonArray.getJSONObject(i);

                                String title = result.getString("original_title");
                                String release = result.getString("release_date");
                                String description = result.getString("overview");
                                String poster = result.getString("poster_path");
                                String id = result.getString("id");

                                String fullPosterUrl = "http://image.tmdb.org/t/p/w500" + poster;
                                if (i == 0) {

                                    mTodayMovieTitle = findViewById(R.id.group_type);
                                    mTodayMovieImage = findViewById(R.id.todayMoviewImage);
                                    mTodayMovieRate = findViewById(R.id.movieTodayRate);

                                    mTodayMovieRate.setText(result.getString("vote_average"));
                                    JSONArray genres_array = result.getJSONArray("genre_ids");
                                    mTodayMovieTitle.setText(result.getString("original_title"));
                                    mTodayMovieId.setText(result.getString("id"));

                                    getGenerParse(genres_array);

                                    Glide.with(getApplication()).load(fullPosterUrl).centerCrop().into(mTodayMovieImage);

                                }
                                if (description.length() > 120) {
                                    description = description.substring(0, 120) + "...";
                                }

                                if (i < 10) {
                                    movieList.add(new MovieItem(title, fullPosterUrl, release, description, id));

                                } else {
                                    movieList2.add(new MovieItem(title, fullPosterUrl, release, description, id));

                                }
                            }
                            popularMovieAdapter = new PopularMovieAdapter(MainActivity.this, movieList);
                            recyclerView.setAdapter(popularMovieAdapter);
                            circleMovieAdapter = new CircleMovieAdapter(MainActivity.this, movieList2);
                            mRecycleViewRound.setAdapter(circleMovieAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        movieList.remove(0);
                        popularMovieAdapter.notifyDataSetChanged();
                        circleMovieAdapter.notifyDataSetChanged();
                        new android.os.Handler().postDelayed(
                                new Runnable() {
                                    public void run() {

                                        shimmerLayout.stopShimmerAnimation();
                                        shimmerLayout.setVisibility(View.GONE);
                                    }
                                },
                                1000);
                    }


                    private void getGenerParse(final JSONArray gernes_array) {
                        jsonGenres = gernes_array;
                        String url = "https://api.themoviedb.org/3/genre/movie/list?api_key=7005ceb3ddacaaf788e2327647f0fa57&language=" + locale + "";
                        JsonObjectRequest req_grn = new JsonObjectRequest(Request.Method.GET, url, null,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            JSONArray grn_array = response.getJSONArray("genres");
                                            genres = "";
                                            for (int ig = 0; ig <= jsonGenres.length(); ig++) {
                                                int gre_id = (int) jsonGenres.get(ig);

                                                for (int i = 0; i < grn_array.length(); i++) {
                                                    JSONObject gener_name = grn_array.getJSONObject(i);
                                                    int id = gener_name.getInt("id");
                                                    String name = gener_name.getString("name");
                                                    if (gre_id == id) {
                                                        genres = name + " - " + genres;
                                                    }
                                                }
                                            }


                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        mText.setText(genres);

                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });

                        requestQueue2.add(req_grn);

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
        BottomNavigationViewHelper.enableNavigation(MainActivity.this, bottomNavigationViewEx);
    }


}
