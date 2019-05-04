package com.example.filmapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class MovieInfoActivity extends AppCompatActivity {

    ImageView moviePoster;
    TextView movieTitle;
    TextView movieDescription;
    RecyclerView recyclerView;
    RequestQueue requestQueue;
    CastAdapter castAdapter;
    ArrayList<CreditPerson> castList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_info);

        moviePoster = findViewById(R.id.poster);
        movieTitle = findViewById(R.id.title);
        movieDescription = findViewById(R.id.description);
        recyclerView = findViewById(R.id.castRecyclerView);
        castList = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        requestQueue = Volley.newRequestQueue(this);
        parseMovieInfoJson();
        parseCastJson();
    }

    private void parseMovieInfoJson() {
        String url = "https://api.themoviedb.org/3/movie/299534?api_key=7005ceb3ddacaaf788e2327647f0fa57&language=en-US";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String title = response.getString("original_title");
                            String description = response.getString("overview");
                            String poster = response.getString("backdrop_path");

                            String fullPosterUrl = "https://image.tmdb.org/t/p/original" + poster;

                            Glide.with(MovieInfoActivity.this)
                                    .load(fullPosterUrl)
                                    .centerCrop()
                                    .into(moviePoster);
                            movieTitle.setText(title);
                            movieDescription.setText(description);
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
        String url = "https://api.themoviedb.org/3/movie/299534/credits?api_key=7005ceb3ddacaaf788e2327647f0fa57";
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
                            recyclerView.setAdapter(castAdapter);


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
