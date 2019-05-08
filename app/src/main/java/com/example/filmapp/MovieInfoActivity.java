package com.example.filmapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MovieInfoActivity extends AppCompatActivity implements RelatedMovieAdapter.OnMovieListener {

    ImageView moviePoster;
    TextView movieTitleTv;
    TextView movieDescriptionTv;
    TextView movieReleaseDateTv;
    TextView movieRuntimeTv;
    TextView movieRatingTv;
    TextView movieGenreTv;
    RecyclerView castRecyclerView;
    RecyclerView crewRecyclerView;
    RecyclerView relatedMovieRecyclerView;
    String movieId;

    RequestQueue requestQueue;

    CastAdapter castAdapter;
    RelatedMovieAdapter relatedMovieAdapter;

    ArrayList<CreditPerson> castList;
    ArrayList<CreditPerson> crewList;
    ArrayList<MovieItem> relatedMovieList;

    public static final String EXTRA_MESSAGE = "com.example.filmapp.MovieInfoActivity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_info);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            movieId = String.valueOf(extras.getString("movie_id"));
        }

        moviePoster = findViewById(R.id.poster);
        movieTitleTv = findViewById(R.id.title);
        movieReleaseDateTv = findViewById(R.id.release);
        movieRuntimeTv = findViewById(R.id.runtime);
        movieRatingTv = findViewById(R.id.rating);
        movieGenreTv = findViewById(R.id.genres);
        movieDescriptionTv = findViewById(R.id.description);
        castRecyclerView = findViewById(R.id.castRecyclerView);
        crewRecyclerView = findViewById(R.id.crewRecyclerView);
        relatedMovieRecyclerView = findViewById(R.id.relatedMoviesRecycler);

        castList = new ArrayList<>();
        crewList = new ArrayList<>();
        relatedMovieList = new ArrayList<>();

        castRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        crewRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        relatedMovieRecyclerView.setLayoutManager(new GridLayoutManager(this, 4) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });


        requestQueue = Volley.newRequestQueue(this);
        final ProgressDialog dialog = ProgressDialog.show(this, null, "Filmer laddas....");

        parseMovieInfoJson();
        parseCastJson();
        parseCrewJson();
        parseRelatedMovies();
        dialog.dismiss();

    }

    private void parseMovieInfoJson() {
        String url = "https://api.themoviedb.org/3/movie/" + movieId + "?api_key=7005ceb3ddacaaf788e2327647f0fa57&language=en-US&language=sv-SE";
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

    private void parseCastJson() {
        String url = "https://api.themoviedb.org/3/movie/" + movieId + "/credits?api_key=7005ceb3ddacaaf788e2327647f0fa57&language=sv-SE*";
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
        String url = "https://api.themoviedb.org/3/movie/" + movieId + "/credits?api_key=7005ceb3ddacaaf788e2327647f0fa57&language=sv-SE";
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
        String url = "https://api.themoviedb.org/3/movie/" + movieId + "/similar?api_key=7005ceb3ddacaaf788e2327647f0fa57&language=en-US&page=1";
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

    @Override
    public void onMovieClick(int position) {
        int id = relatedMovieList.get(position).getId();
        Intent intent = new Intent(this, MovieInfoActivity.class);
        intent.putExtra(EXTRA_MESSAGE, id);
        startActivity(intent);
    }
}
