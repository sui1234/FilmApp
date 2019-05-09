package com.example.filmapp.People;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.filmapp.MovieInfoActivity;
import com.example.filmapp.MovieItem;
import com.example.filmapp.R;
import com.example.filmapp.RelatedMovieAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.filmapp.MovieInfoActivity.EXTRA_MESSAGE;

public class people_view extends AppCompatActivity {
    private static final String TAG = "people_view";
    private TextView mPeopleTitle, mPeopleInfo, mPeopleBirth;
    private ImageView mPeopleImage;
    private String people_id;
    private ArrayList<MovieItem> relatedMovieList;
    RelatedMovieAdapter relatedMovieAdapter;
    RequestQueue requestQueue;
    RecyclerView relatedMovieRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_view);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        mPeopleTitle = findViewById(R.id.people_name);
        mPeopleBirth = findViewById(R.id.people_birth);
        mPeopleInfo = findViewById(R.id.people_info);
        mPeopleImage = findViewById(R.id.peopleImage);
        relatedMovieList = new ArrayList<>();

        Log.d(TAG, "onCreate: ");
        requestQueue = Volley.newRequestQueue(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            people_id = String.valueOf(extras.getString("people_id"));
        }
        parseRelatedMovies(people_id);
    }
    private void parseRelatedMovies(String people_id) {
        String url = "https://api.themoviedb.org/3/person/"+people_id+"?api_key=7005ceb3ddacaaf788e2327647f0fa57";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponseresponse: "+response);
                        try {
                            String name = response.getString("name");
                            String biography = response.getString("biography");
                            String place_of_birth = response.getString("place_of_birth");
                            String birthday = response.getString("birthday");
                            String profileImage = "https://image.tmdb.org/t/p/w500/"+response.getString("profile_path");

                            Glide.with(people_view.this)
                                    .load(profileImage)
                                    .centerCrop()
                                    .into(mPeopleImage);
                            mPeopleBirth.setText(birthday+"-"+place_of_birth);
                            mPeopleInfo.setText(biography);
                            mPeopleTitle.setText(name);
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
        String url = "https://api.themoviedb.org/3/person/12441/movie_credits?api_key=7005ceb3ddacaaf788e2327647f0fa57&language=en-US\n";
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

}
