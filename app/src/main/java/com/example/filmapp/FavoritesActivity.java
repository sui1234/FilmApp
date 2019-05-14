package com.example.filmapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static com.example.filmapp.MovieInfoActivity.FAVORITE_ARRAY_LIST;
import static com.example.filmapp.MovieInfoActivity.SHARED_PREFS;

public class FavoritesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private RecyclerView.LayoutManager layoutManager;
//    ArrayList<String> movieIDArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Ska ta sharedpreferences / putExtra

        ArrayList<String> movieIDArrayList  = loadFavoritesFromPreferences();
        movieAdapter = new MovieAdapter(movieIDArrayList, this);
        recyclerView.setAdapter(movieAdapter);
    }

    public ArrayList<String> loadFavoritesFromPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(FAVORITE_ARRAY_LIST, null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        ArrayList<String> arr = gson.fromJson(json, type);

        if (arr == null) {
            arr = new ArrayList<>();
        }

        return arr;
    }
}
