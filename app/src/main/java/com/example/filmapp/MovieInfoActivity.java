package com.example.filmapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.filmapp.BottomNavMenu.BottomNavigationViewHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MovieInfoActivity extends AppCompatActivity implements RelatedMovieAdapter.OnMovieListener {

    ImageView moviePoster;
    TextView movieTitleTv;
    TextView movieDescriptionTv;
    TextView movieReleaseDateTv;
    TextView movieRuntimeTv,mAdd_to_fav,mRemoveFromFav;
    TextView movieRatingTv;
    TextView movieGenreTv, mRelatedMovie;
    RecyclerView castRecyclerView;
    RecyclerView crewRecyclerView;
    RecyclerView relatedMovieRecyclerView,reviewsRecycle;
    FrameLayout frameLayout;
    YouTubePlayerView youTubePlayerView;
    TabLayout mTabs;
    String movieId,lokale;
    ScrollView scrollView;

    RequestQueue requestQueue;

    CastAdapter castAdapter;
    MovieReviewsAdopter reviewsAdopter;
    RelatedMovieAdapter relatedMovieAdapter;

    ArrayList<CreditPerson> castList;
    ArrayList<CreditPerson> reviewsList;
    ArrayList<CreditPerson> crewList;
    ArrayList<MovieItem> relatedMovieList;
    private static final String TAG = "MovieInfoActivity";
    public static final String EXTRA_MESSAGE = "com.example.filmapp.MovieInfoActivity";

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String FAVORITE_ARRAY_LIST = "favorites";
    ArrayList<String> movieIDArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_movie_info);


        loadFavoritesFromPreferences();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            movieId = String.valueOf(extras.getString("movie_id"));
        }
        scrollView = findViewById(R.id.scrollView);
        moviePoster = findViewById(R.id.poster);
        movieTitleTv = findViewById(R.id.title);
        movieReleaseDateTv = findViewById(R.id.release);
        mAdd_to_fav = findViewById(R.id.add_to_fav);
        movieRuntimeTv = findViewById(R.id.runtime);
        movieRatingTv = findViewById(R.id.rating);
        mRemoveFromFav = findViewById(R.id.remove_to_fav);
        movieGenreTv = findViewById(R.id.genres);
        movieDescriptionTv = findViewById(R.id.description);
        castRecyclerView = findViewById(R.id.castRecyclerView);
        crewRecyclerView = findViewById(R.id.crewRecyclerView);
        relatedMovieRecyclerView = findViewById(R.id.relatedMoviesRecycler);
        reviewsRecycle = findViewById(R.id.reviewsRecyclerView);
        frameLayout = findViewById(R.id.header);
        mTabs = findViewById(R.id.tabs);
        youTubePlayerView = findViewById(R.id.youtube_player_view);
        mRelatedMovie = findViewById(R.id.relatedMovies);
        castList = new ArrayList<>();
        crewList = new ArrayList<>();
        reviewsList = new ArrayList<CreditPerson>();
        relatedMovieList = new ArrayList<>();
        lokale = Locale.getDefault().getLanguage()+"-"+Locale.getDefault().getCountry();



        castRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        crewRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        reviewsRecycle.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        relatedMovieRecyclerView.setLayoutManager(new GridLayoutManager(this, 4) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        castRecyclerView.setLayoutManager(new GridLayoutManager(this, 4) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        crewRecyclerView.setLayoutManager(new GridLayoutManager(this, 4) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        mRemoveFromFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "favo_movie: "+movieId);
                JSONObject user = new JSONObject();
                try {
                    user.put("media_type", "movie");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    user.put("media_id",Integer.parseInt(movieId));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    user.put("favorite", false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d(TAG, "Fav_create_session_id: "+ user);
                String url = "https://api.themoviedb.org/3/account/{account_id}/favorite?api_key=7005ceb3ddacaaf788e2327647f0fa57&session_id=0f864750f3c93c321b9972aa3fab11afdfed7226\n";
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, user, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse: "+response);
                        checkMovieStat();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //handle errors
                        Log.d(TAG, "onResponse++: "+error);

                    }

                });
                requestQueue.add(request);
            }
        });

        mAdd_to_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "favo_movie: "+movieId);
                JSONObject user = new JSONObject();
                try {
                    user.put("media_type", "movie");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    user.put("media_id",Integer.parseInt(movieId));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    user.put("favorite", true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d(TAG, "Fav_create_session_id: "+ user);
                String url = "https://api.themoviedb.org/3/account/{account_id}/favorite?api_key=7005ceb3ddacaaf788e2327647f0fa57&session_id=0f864750f3c93c321b9972aa3fab11afdfed7226\n";
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, user, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse: "+response);
                        // Some code
                        checkMovieStat();


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //handle errors
                        Log.d(TAG, "onResponse++: "+error);

                    }

                });
                requestQueue.add(request);
            }
        });

        mTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int tabNum = mTabs.getSelectedTabPosition();
                if (tabNum == 0){
                    movieDescriptionTv.setVisibility(View.VISIBLE);
                    mRelatedMovie.setVisibility(View.VISIBLE);
                    castRecyclerView.setVisibility(View.GONE);
                    crewRecyclerView.setVisibility(View.GONE);
                    relatedMovieRecyclerView.setVisibility(View.VISIBLE);
                    reviewsRecycle.setVisibility(View.GONE);
                    relatedMovieList.clear();
                    parseRelatedMovies();


                }
                if (tabNum == 1){
                    movieDescriptionTv.setVisibility(View.GONE);
                    relatedMovieRecyclerView.setVisibility(View.GONE);
                    castRecyclerView.setVisibility(View.VISIBLE);
                    movieDescriptionTv.setVisibility(View.GONE);
                    relatedMovieRecyclerView.setVisibility(View.GONE);
                    mRelatedMovie.setVisibility(View.GONE);
                    crewRecyclerView.setVisibility(View.GONE);
                    reviewsRecycle.setVisibility(View.GONE);

                    castList.clear();
                    parseCastJson();
                }
                if (tabNum == 2){
                    movieDescriptionTv.setVisibility(View.GONE);
                    relatedMovieRecyclerView.setVisibility(View.GONE);
                    castRecyclerView.setVisibility(View.GONE);
                    movieDescriptionTv.setVisibility(View.GONE);
                    crewRecyclerView.setVisibility(View.VISIBLE);
                    relatedMovieRecyclerView.setVisibility(View.GONE);
                    mRelatedMovie.setVisibility(View.GONE);
                    reviewsRecycle.setVisibility(View.GONE);
                    crewList.clear();
                    parseCrewJson();
                }
                if (tabNum == 3){
                    movieDescriptionTv.setVisibility(View.GONE);
                    relatedMovieRecyclerView.setVisibility(View.GONE);
                    castRecyclerView.setVisibility(View.GONE);
                    movieDescriptionTv.setVisibility(View.GONE);
                    crewRecyclerView.setVisibility(View.GONE);
                    relatedMovieRecyclerView.setVisibility(View.GONE);
                    mRelatedMovie.setVisibility(View.GONE);
                    reviewsRecycle.setVisibility(View.VISIBLE);
                    reviewsList.clear();
                    parseMovieReview();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        requestQueue = Volley.newRequestQueue(this);
        final ProgressDialog dialog = ProgressDialog.show(this, null, "Filmer laddas....");

        parseMovieInfoJson();
        parseCastJson();
        parseCrewJson();
        parseRelatedMovies();
        parseVideosJson();
        dialog.dismiss();
        parseMovieReview();
        setuoBottomnavView();
        overridePendingTransition(0, 0);
        checkMovieStat();

    }

    private void parseMovieInfoJson() {
        String url = "https://api.themoviedb.org/3/movie/" + movieId + "?api_key=7005ceb3ddacaaf788e2327647f0fa57&language="+lokale+"";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
//                          hämtar genres
                            JSONArray jsonArray = response.getJSONArray("genres");

                            StringBuilder genres = new StringBuilder();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject result = jsonArray.getJSONObject(i);

                                String genre = result.getString("name");
                                genres.append(genre + " • ");
                            }

                            String title = response.getString("original_title");
                            String description = response.getString("overview");
                            String poster = response.getString("backdrop_path");
                            String releaseDate = response.getString("release_date");
                            double rating = response.getDouble("vote_average");
                            int runtime = response.getInt("runtime");

                            String hoursMinutes = Integer.toString(runtime / 60) + "h " + Integer.toString(runtime % 60) + "m";
                            String fullPosterUrl = "https://image.tmdb.org/t/p/original" + poster;
                            String[] releaseDateArr = releaseDate.split("-");
                            String releaseYear = releaseDateArr[0];

                            Glide.with(MovieInfoActivity.this)
                                    .load(fullPosterUrl)
                                    .centerCrop()
                                    .into(moviePoster);
                            movieTitleTv.setText(title);
                            movieDescriptionTv.setText(description);
                            movieReleaseDateTv.setText(releaseYear);
                            movieRatingTv.setText(Double.toString(rating));
                            movieRuntimeTv.setText(hoursMinutes);
                            movieGenreTv.setText(genres.substring(0, genres.length() - 2));

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
    private void checkMovieStat() {
        String url = "https://api.themoviedb.org/3/movie/"+movieId+"/account_states?api_key=7005ceb3ddacaaf788e2327647f0fa57&session_id=0f864750f3c93c321b9972aa3fab11afdfed7226";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Boolean dtatus = response.getBoolean("favorite");
                            if(dtatus.equals(false)){
                                Log.d(TAG, "onResponse: status är false"+response);
                                mAdd_to_fav.setVisibility(View.VISIBLE);
                                mRemoveFromFav.setVisibility(View.GONE);
                            }else
                            {
                                Log.d(TAG, "onResponse: status är true"+response);
                                mAdd_to_fav.setVisibility(View.GONE);
                                mRemoveFromFav.setVisibility(View.VISIBLE);

                            }
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
    private void parseMovieReview() {
        String url = "https://api.themoviedb.org/3/movie/"+movieId+"/reviews?api_key=7005ceb3ddacaaf788e2327647f0fa57";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("results");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject result = jsonArray.getJSONObject(i);
                                Log.d("antalet ", "onResponse: "+result.length());
                                String name = result.getString("author");
                                String contect = result.getString("content");
                                String id = result.getString("id");
                                String url = result.getString("url");
                                reviewsList.add(new CreditPerson(name, id, contect, url));
                            }

                            reviewsAdopter = new MovieReviewsAdopter(MovieInfoActivity.this, reviewsList);
                            reviewsRecycle.setAdapter(reviewsAdopter);


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
    private void parseCastJson() {
        String url = "https://api.themoviedb.org/3/movie/" + movieId + "/credits?api_key=7005ceb3ddacaaf788e2327647f0fa57&language="+lokale+"";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("cast");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject result = jsonArray.getJSONObject(i);

                                String name = result.getString("name");
                                String id = result.getString("id");
                                String character = result.getString("character");
                                String profileImage = result.getString("profile_path");

                                if (!profileImage.equals("null")) {
                                    String fullProfileImageUrl = "http://image.tmdb.org/t/p/w185" + profileImage;
                                    castList.add(new CreditPerson(name, id, fullProfileImageUrl, character));
                                } else {
                                    String dummyPic = "https://emgroupuk.com/wp-content/uploads/2018/06/profile-icon-9.png";
                                    castList.add(new CreditPerson(name, id, dummyPic, character));
                                }
                            }

                            castAdapter = new CastAdapter(MovieInfoActivity.this, castList);
                            castRecyclerView.setAdapter(castAdapter);


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

    private void parseCrewJson() {
        String url = "https://api.themoviedb.org/3/movie/" + movieId + "/credits?api_key=7005ceb3ddacaaf788e2327647f0fa57&language="+lokale+"";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("crew");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject result = jsonArray.getJSONObject(i);

                                String name = result.getString("name");
                                String id = result.getString("id");
                                String job = result.getString("job");
                                String profileImage = result.getString("profile_path");

                                if (!profileImage.equals("null")) {
                                    String fullProfileImageUrl = "http://image.tmdb.org/t/p/w185" + profileImage;
                                    crewList.add(new CreditPerson(name, id, fullProfileImageUrl, job));
                                } else {
                                    String dummyPic = "https://emgroupuk.com/wp-content/uploads/2018/06/profile-icon-9.png";
                                    crewList.add(new CreditPerson(name, id, dummyPic, job));
                                }
                            }

                            castAdapter = new CastAdapter(MovieInfoActivity.this, crewList);
                            crewRecyclerView.setAdapter(castAdapter);

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

    private void parseRelatedMovies() {
        String url = "https://api.themoviedb.org/3/movie/" + movieId + "/similar?api_key=7005ceb3ddacaaf788e2327647f0fa57&language="+lokale+"&page=1";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("results");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject result = jsonArray.getJSONObject(i);

                                String title = result.getString("original_title");
                                String poster = result.getString("poster_path");
                                int id = result.getInt("id");

                                String fullPosterUrl = "http://image.tmdb.org/t/p/w185" + poster;

                                relatedMovieList.add(new MovieItem(title, fullPosterUrl, id));
                            }

                            relatedMovieAdapter = new RelatedMovieAdapter
                                    (MovieInfoActivity.this, relatedMovieList, MovieInfoActivity.this);
                            relatedMovieRecyclerView.setAdapter(relatedMovieAdapter);

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

    private void parseVideosJson() {
        String url = "https://api.themoviedb.org/3/movie/" + movieId + "/videos?api_key=7005ceb3ddacaaf788e2327647f0fa57&language="+lokale+"";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("results");

                            JSONObject result = jsonArray.getJSONObject(0);

                            final String key = result.getString("key");
                            String site = result.getString("site");

                            if (result != null && site.equals("YouTube")) {
                                frameLayout.removeView(moviePoster);

                                getLifecycle().addObserver(youTubePlayerView);

                                youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                                    @Override
                                    public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                                        youTubePlayer.cueVideo(key, 0);
                                    }
                                });
                            } else {
                                frameLayout.removeView(youTubePlayerView);
                            }

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

    @Override
    public void onMovieClick(int position) {
        int id = relatedMovieList.get(position).getId();
        Intent intent = new Intent(this, MovieInfoActivity.class);
        intent.putExtra(EXTRA_MESSAGE, id);
        startActivity(intent);
    }
    private void setuoBottomnavView(){
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bnve);
        BottomNavigationViewHelper.setypBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(MovieInfoActivity.this, bottomNavigationViewEx);

    }



    public void saveFavoriteToPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(movieIDArrayList);
        editor.putString(FAVORITE_ARRAY_LIST, json);
        editor.apply();
    }

    public void loadFavoritesFromPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(FAVORITE_ARRAY_LIST, null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        movieIDArrayList = gson.fromJson(json, type);

        if (movieIDArrayList == null) {
            movieIDArrayList = new ArrayList<>();
        }
    }

    public void saveToFavorites(View v) {
        // OM filmen finns i listan så ta bort den ANNARS lägg till den
        if (movieIDArrayList.contains(movieId)) {
            movieIDArrayList.remove(movieId);
            Log.d("!!!", "saveToFavorites: Removed " + movieId);
        } else {
            movieIDArrayList.add(movieId);
            Log.d("!!!", "saveToFavorites: Added " + movieId);
            saveFavoriteToPreferences();

            Toast.makeText(this, "movie id is: " + movieId, Toast.LENGTH_SHORT).show();

            for (int i = 0; i < movieIDArrayList.size(); i++) {
                String id = movieIDArrayList.get(i);
                Log.d("!!!", "saveToFavorites: " + id);
            }
        }
    }
}