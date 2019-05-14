package com.example.filmapp.BottomNavMenu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.MenuItem;

import com.example.filmapp.FavoritesActivity;
import com.example.filmapp.MainActivity;
import com.example.filmapp.R;
import com.example.filmapp.SearchActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import androidx.annotation.NonNull;

import static android.content.Context.MODE_PRIVATE;
import static com.example.filmapp.MovieInfoActivity.SHARED_PREFS;

public class BottomNavigationViewHelper {
    private static final String TAG = "BottomNavigationViewHel";
    public static void setypBottomNavigationView (BottomNavigationViewEx bottomNavigationViewEx){
        bottomNavigationViewEx.enableAnimation(false);
        bottomNavigationViewEx.enableItemShiftingMode(false);
        bottomNavigationViewEx.enableItemShiftingMode(false);
        bottomNavigationViewEx.setTextVisibility(true);
    }
    public static void enableNavigation(final Context context, BottomNavigationViewEx viewEx){
        viewEx.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_home:
                        Intent intent1 = new Intent(context, MainActivity.class);
                        context.startActivity(intent1);
                        break;
                    case R.id.nav_serarch:
                            Intent intent2 = new Intent(context, SearchActivity.class);
                            context.startActivity(intent2);
                            break;
                    case R.id.nav_fav:
                        Intent intent3 = new Intent(context, FavoritesActivity.class);
                        context.startActivity(intent3);
                        break;
                }
                return false;
            }
        });
    }
}
