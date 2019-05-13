package com.example.filmapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

public class SearchActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "url";
    private Button searchButton;
    private SearchView searchView;
    private String searchString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchView = findViewById(R.id.searchView);
        searchMovies();

        searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getSearchMovies();

            }
        });



    }

    private void searchMovies(){

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                searchView.getQuery();
                searchString = searchView.getQuery().toString();
                return false;
            }

        });
    }

    private void getSearchMovies(){


        String url = "https://api.themoviedb.org/3/search/multi?api_key=7005ceb3ddacaaf788e2327647f0fa57&language=en-US&query=" + searchString +  "&page=1";
        Log.d("Sui send url", "searchString " + searchString);

        Intent intent = new Intent(SearchActivity.this, MainActivity.class);
        intent.putExtra(EXTRA_MESSAGE, url);

        Log.d("Sui url","is "+ url);
        startActivity(intent);

    }



}
