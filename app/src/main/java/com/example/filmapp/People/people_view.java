package com.example.filmapp.People;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.example.filmapp.BottomNavMenu.BottomNavigationViewHelper;
import com.example.filmapp.MainActivity;
import com.example.filmapp.MovieInfoActivity;
import com.example.filmapp.MovieItem;
import com.example.filmapp.R;
import com.example.filmapp.RelatedMovieAdapter;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.filmapp.MovieInfoActivity.EXTRA_MESSAGE;

public class people_view extends AppCompatActivity implements RelatedMovieAdapter.OnMovieListener{
    private static final String TAG = "people_view";
    private TextView mPeopleTitle, mPeopleInfo, mPeopleBirth;
    private ImageView mPeopleImage;
    private String people_id;
    private ArrayList<MovieItem> relatedMovieList;
    RelatedMovieAdapter peopleRelatedMovieAdapter;
    RequestQueue requestQueue;
    RecyclerView peopleRelatedMovieRecyclerView;


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
        peopleRelatedMovieRecyclerView = findViewById(R.id.peopleRelatedMoviesRecycler);
        peopleRelatedMovieRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        relatedMovieList = new ArrayList<>();

        Log.d(TAG, "onCreate: ");
        requestQueue = Volley.newRequestQueue(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            people_id = String.valueOf(extras.getString("people_id"));
            Log.d("people_ID", people_id);
        }
        parsePerson(people_id);
        parseRelatedMovies();
        setuoBottomnavView();
        overridePendingTransition(0, 0);

    }
    private void parsePerson(String people_id) {
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
        Log.d("Related_movies_init", "Related_movies_init");
       final String url = "https://api.themoviedb.org/3/person/" + people_id + "/movie_credits?api_key=7005ceb3ddacaaf788e2327647f0fa57&language=en-US";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Related_movies_response", "Related_movies_on_response");
                        Log.d("Related_movies_url", url);
                        try {
                            JSONArray jsonArray = response.getJSONArray("cast");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject result = jsonArray.getJSONObject(i);

                                String title = result.getString("original_title");
                                Log.d("original_title", title);
                                String poster = result.getString("poster_path");
                                int id = result.getInt("id");

                                /*String fullPosterUrl = "http://image.tmdb.org/t/p/w185" + poster;*/
                                //Log.d("posterURL", fullPosterUrl);

                                if (!poster.equals("null")) {

                                    String fullPosterUrl = "http://image.tmdb.org/t/p/w185" + poster;

                                    relatedMovieList.add(new MovieItem(title, fullPosterUrl, id));

                                } else {

                                    String dummyPic = "https://emgroupuk.com/wp-content/uploads/2018/06/profile-icon-9.png";

                                    relatedMovieList.add(new MovieItem(title, dummyPic, id));

                                }
                                Log.d(TAG, relatedMovieList.get(i).getPosterImageUrl());



                               // relatedMovieList.add(new MovieItem(title, fullPosterUrl, id));
                            }


                            peopleRelatedMovieAdapter = new RelatedMovieAdapter(people_view.this, relatedMovieList, people_view.this);
                            peopleRelatedMovieRecyclerView.setAdapter(peopleRelatedMovieAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("Related_movies_e", e.toString());
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
        Intent intent = new Intent(this, people_view.class);
        intent.putExtra(EXTRA_MESSAGE, id);
        startActivity(intent);
    }

    private void setuoBottomnavView(){
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bnve);
        BottomNavigationViewHelper.setypBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(people_view.this, bottomNavigationViewEx);
    }

}
