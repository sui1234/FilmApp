package com.example.filmapp.Favoriter;


import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpResponse;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.filmapp.MovieInfoActivity;
import com.android.volley.RequestQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Fav_getter {
    private static final String TAG = "Fav_getter";
    static RequestQueue requestQueue;

    public static void Fav_getter_request_id(final Context context){
            String url = "https://api.themoviedb.org/3/account/{account_id}/favorite/movies?api_key=7005ceb3ddacaaf788e2327647f0fa57&session_id=0f864750f3c93c321b9972aa3fab11afdfed7226&language=en-US&sort_by=created_at.asc&page=1";
        requestQueue = Volley.newRequestQueue(context);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, "sdsddsd: ++"+response);
                            
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });
        requestQueue.add(request);

        }
        public static void Fav_create_session_id(String momo, Context context){
            Log.d(TAG, "Fav_create_session_id: momoär " +momo);
            requestQueue = Volley.newRequestQueue(context);

            HashMap<String, String> params = new HashMap<>();
            params.put("request_token", "a259a13abde02a87fbb10bb7d3507d3700fb4f71" );
            Log.d(TAG, "Fav_create_session_id: "+ new JSONObject(params));
            String url = "https://api.themoviedb.org/3/authentication/session/new?api_key=7005ceb3ddacaaf788e2327647f0fa57";
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d(TAG, "onResponse: "+response);
                    // Some code

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
    }