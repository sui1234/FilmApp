package com.example.filmapp;

import android.app.ProgressDialog;
import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView, mRecycleViewRound;
    private PopularMoiveAdapter popularMoiveAdapter;
    private CirclearMovieAdapter circleMoiveAdapter;
    private ArrayList<MovieItem> movieList;
    private RequestQueue requestQueue,requestQueue2;
    private TextView mTodaysMovieTitle, mTodaysMovieRate,mText;
    private ImageView mTodaysMovieImage;
    private  JSONArray jsonArray,jsonArray2,jsonGerenes;
    private String gereners;
    private Spinner mDropDown;
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
        mRecycleViewRound.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        movieList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);
        requestQueue2 = Volley.newRequestQueue(this);
        mText = findViewById(R.id.group_num);


        ArrayAdapter<String> dropdownAdapter = new ArrayAdapter<String> (MainActivity.this,
                R.layout.dropdown_item,getResources().getStringArray(R.array.dropdownArray));
        dropdownAdapter.setDropDownViewResource(R.layout.dropdown_item);
        mDropDown.setAdapter(dropdownAdapter);
        mDropDown.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        ((TextView) view).setTextColor(Color.WHITE);
                        Object item = parent.getItemAtPosition(pos);
                        Log.d(TAG, "onItemSelected: "+item.toString());
                        if (item.toString().equals("På bio nu")){

                            parseJson("https://api.themoviedb.org/3/movie/now_playing?api_key=7005ceb3ddacaaf788e2327647f0fa57&language=se-SV&page=1");

                        }
                        if (item.toString().equals("Top listan")){
                            parseJson("https://api.themoviedb.org/3/movie/top_rated?api_key=7005ceb3ddacaaf788e2327647f0fa57&language=sv-SE&page=1\n");

                        }
                        if (item.toString().equals("Kommande filmer")){
                            parseJson("https://api.themoviedb.org/3/movie/upcoming?api_key=7005ceb3ddacaaf788e2327647f0fa57&language=en-US&page=1");

                        }
                    }
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });


    parseJson("https://api.themoviedb.org/3/movie/upcoming?api_key=7005ceb3ddacaaf788e2327647f0fa57&language=en-US&page=1");






    }
    private void parseJson(String url) {
        final ProgressDialog dialog = ProgressDialog.show(this, null, "Filmer laddas....");

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
                                String id     = result.getString("id");

                                String fullPosterUrl = "http://image.tmdb.org/t/p/w500" + poster;
                                if (i == 0){

                                    mTodaysMovieTitle = findViewById(R.id.group_type);
                                    mTodaysMovieImage =  findViewById(R.id.todayMoviewImage);
                                    mTodaysMovieRate = findViewById(R.id.movieTodayRate);

                                    mTodaysMovieRate.setText(result.getString("vote_average"));
                                    JSONArray gernes_array = result.getJSONArray("genre_ids");
                                    mTodaysMovieTitle.setText(result.getString("original_title"));


                                        getGenerParse(gernes_array);

                                    Glide.with(getApplication()).load(fullPosterUrl).centerCrop().into(mTodaysMovieImage);

                                }
                                if (description.length() > 120) {
                                    String shortDescription = description.substring(0, 120) + "...";
                                    movieList.add(new MovieItem(title, fullPosterUrl, release, shortDescription,id));
                                } else {
                                    movieList.add(new MovieItem(title, fullPosterUrl, release, description,id));
                                }
                            }
                            popularMoiveAdapter = new PopularMoiveAdapter(MainActivity.this, movieList);

                            recyclerView.setAdapter(popularMoiveAdapter);
                            circleMoiveAdapter = new CirclearMovieAdapter(MainActivity.this, movieList);
                            mRecycleViewRound.setAdapter(circleMoiveAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        movieList.remove(0);
                        popularMoiveAdapter.notifyDataSetChanged();
                        circleMoiveAdapter.notifyDataSetChanged();
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
                        String url = "https://api.themoviedb.org/3/genre/movie/list?api_key=7005ceb3ddacaaf788e2327647f0fa57&language=sv-SE";
                        JsonObjectRequest req_grn = new JsonObjectRequest(Request.Method.GET, url, null,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            JSONArray grn_array = response.getJSONArray("genres");
                                            gereners = "";
                                            for (int ig = 0; ig <= jsonGerenes.length();ig++){
                                                int gre_id = (int) jsonGerenes.get(ig);

                                                for (int i = 0;i<grn_array.length();i++){
                                                    JSONObject gener_name = grn_array.getJSONObject(i);
                                                    int id = gener_name.getInt("id");
                                                    String name = gener_name.getString("name");
                                                    if (gre_id == id){
                                                        gereners = name+" - "+ gereners;
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

    }


}
