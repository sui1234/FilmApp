package com.example.filmapp;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.RequestQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.android.volley.VolleyLog.TAG;

public class GloabAPI extends Application {
    private JSONArray Mazen ;
    private RequestQueue requestQueue;
    public JsonObjectRequest request;
    public JSONArray jsonArray;
    public String mMovie,mMovieJSON;







    public void setmMovies(String url) {
        request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray jsonArray = response.getJSONArray("results");

                            Log.d(TAG, "first_: "+jsonArray);

                            SharedPreferences settings = getSharedPreferences("pref", 0);
                            SharedPreferences.Editor editor = settings.edit();

                            editor.putString("jsonStrings", String.valueOf(jsonArray));
                            editor.commit();
                            String mMovieJSONs = String.valueOf(jsonArray);
                            Log.d(TAG, "setmMovies: "+mMovieJSONs);

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
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }


}
