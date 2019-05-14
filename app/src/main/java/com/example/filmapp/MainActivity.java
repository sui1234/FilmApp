package com.example.filmapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView, mRecycleViewRound;
    private PopularMoiveAdapter popularMoiveAdapter;
    private CirclearMovieAdapter circleMoiveAdapter;
    private ArrayList<MovieItem> movieList, movileList2;
    private RequestQueue requestQueue, requestQueue2;
    private TextView mTodaysMovieTitle, mTodaysMovieRate, mText;
    private ImageView mTodaysMovieImage;
    private JSONArray jsonGerenes;
    private String gereners;
    private Spinner mDropDown;
    ImageView searchImage;

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
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        movieList = new ArrayList<>();
        movileList2 = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);
        requestQueue2 = Volley.newRequestQueue(this);
        mText = findViewById(R.id.group_num);


        ArrayAdapter<String> dropdownAdapter = new ArrayAdapter<String>(MainActivity.this,
                R.layout.dropdown_item, getResources().getStringArray(R.array.dropdownArray));
        dropdownAdapter.setDropDownViewResource(R.layout.dropdown_item);
        mDropDown.setAdapter(dropdownAdapter);
        mDropDown.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        ((TextView) view).setTextColor(Color.WHITE);
                        Object item = parent.getItemAtPosition(pos);
                        Log.d(TAG, "onItemSelected: " + item.toString());
                        if (item.toString().equals("På bio nu")) {

                            parseJson("https://api.themoviedb.org/3/movie/now_playing?api_key=7005ceb3ddacaaf788e2327647f0fa57&language=sv-SE&page=1");

                        }
                        if (item.toString().equals("Top listan")) {
                            parseJson("https://api.themoviedb.org/3/movie/top_rated?api_key=7005ceb3ddacaaf788e2327647f0fa57&language=sv-SE&page=1\n");

                        }
                        if (item.toString().equals("Kommande filmer")) {
                            parseJson("https://api.themoviedb.org/3/movie/upcoming?api_key=7005ceb3ddacaaf788e2327647f0fa57&language=sv-SE&page=1");

                        }
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

        searchImage = findViewById(R.id.search_image);
        searchImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);

            }
        });



        parseJson("https://api.themoviedb.org/3/movie/upcoming?api_key=7005ceb3ddacaaf788e2327647f0fa57&language=sv-SE&page=1");


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {


            String url = bundle.getString("url");
            Log.d("Sui get intent", "url " + url);
            parseJson(url);
        }


    }

    private void parseJson(String url) {
        final ProgressDialog dialog = ProgressDialog.show(this, null, "Filmer laddas....");


        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray;
                            jsonArray = response.getJSONArray("results");

                            Log.d("Sui result", "length" + jsonArray.length());
                            movieList.clear();
                            movileList2.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject result = jsonArray.getJSONObject(i);

                                Log.d("Sui result", "jsonArray" + result);

                                try {
                                    String title = result.getString("original_title");
                                    String release = result.getString("release_date");
                                    String description = result.getString("overview");
                                    String poster = result.getString("poster_path");
                                    String id = result.getString("id");

                                    Log.d("Sui", "title" + title + release + id);
                                    String fullPosterUrl = "http://image.tmdb.org/t/p/w500" + poster;

                                    Log.d("Sui", "fullposter" + fullPosterUrl);
                                    if (i == 0) {

                                        mTodaysMovieTitle = findViewById(R.id.group_type);
                                        mTodaysMovieImage = findViewById(R.id.todayMoviewImage);
                                        mTodaysMovieRate = findViewById(R.id.movieTodayRate);

                                        Log.d("Sui first movie", "is " + mTodaysMovieTitle);

                                        mTodaysMovieRate.setText(result.getString("vote_average"));
                                        JSONArray gernes_array = result.getJSONArray("genre_ids");
                                        mTodaysMovieTitle.setText(result.getString("original_title"));


                                        getGenerParse(gernes_array);

                                        try {
                                            Glide.with(getApplication()).load(fullPosterUrl).centerCrop().into(mTodaysMovieImage);
                                        } catch (Exception E) {
                                            continue;
                                        }
                                    }
                                    if (description.length() > 120) {
                                        description = description.substring(0, 120) + "...";
                                    }

                                    if (i < 10) {
                                        movieList.add(new MovieItem(title, fullPosterUrl, release, description, id));
                                        Log.d("Suimovielist", "is < 10" + movieList);


                                    } else {
                                        movileList2.add(new MovieItem(title, fullPosterUrl, release, description, id));
                                        Log.d("Sui get movielist", "is >=10" + movileList2);

                                    }

                                } catch (Exception E) {
                                    continue;
                                }


                            }
                            popularMoiveAdapter = new PopularMoiveAdapter(MainActivity.this, movieList);

                            recyclerView.setAdapter(popularMoiveAdapter);
                            Log.d("Suirecyclerview", "successeful" + movieList);

                            circleMoiveAdapter = new CirclearMovieAdapter(MainActivity.this, movileList2);
                            mRecycleViewRound.setAdapter(circleMoiveAdapter);
                            Log.d("Suimrecycle", "successeful" + movileList2);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        movieList.remove(0);
                        popularMoiveAdapter.notifyDataSetChanged();
                        circleMoiveAdapter.notifyDataSetChanged();
                        Log.d("Sui notify", "added");
                        new android.os.Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        dialog.dismiss();
                                    }
                                },
                                1000);
                    }

                    private void getGenerParse(final JSONArray gernes_array) {
                        jsonGerenes = gernes_array;
                        Log.d("Suitest", "get Gener");
                        String url = "https://api.themoviedb.org/3/genre/movie/list?api_key=7005ceb3ddacaaf788e2327647f0fa57&language=sv-SE";
                        JsonObjectRequest req_grn = new JsonObjectRequest(Request.Method.GET, url, null,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            JSONArray grn_array = response.getJSONArray("genres");
                                            gereners = "";
                                            for (int ig = 0; ig <= jsonGerenes.length(); ig++) {
                                                int gre_id = (int) jsonGerenes.get(ig);

                                                for (int i = 0; i < grn_array.length(); i++) {
                                                    JSONObject gener_name = grn_array.getJSONObject(i);
                                                    int id = gener_name.getInt("id");
                                                    String name = gener_name.getString("name");
                                                    if (gre_id == id) {
                                                        gereners = name + " - " + gereners;
                                                    }
                                                }
                                            }


                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        mText.setText(gereners);

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
        Log.d("Suiadd ", "request");

    }


}
