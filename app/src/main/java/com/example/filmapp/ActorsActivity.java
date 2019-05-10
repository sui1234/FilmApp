package com.example.filmapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
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

public class ActorsActivity extends AppCompatActivity {

    ImageView actorProfilePictureImage;
    TextView actorNameText;
    TextView actorBirthdayText;
    TextView actorDeathDayText;
    TextView actorBirthPlaceText;
    TextView actorBiographyText;

    String actorId;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actors);

        actorId = "3223";
       /* Bundle extras = getIntent().getExtras();
        if (extras != null) {
            actorId = String.valueOf(extras.getString("actor_id"));
        }*/

        actorProfilePictureImage = findViewById(R.id.actorProfilePicture);
        actorNameText = findViewById(R.id.actorName);
        actorBirthdayText = findViewById(R.id.actorBirthday);
        actorDeathDayText = findViewById(R.id.actorDeathDay);
        actorBirthPlaceText = findViewById(R.id.actorBirthplace);
        actorBiographyText = findViewById(R.id.actorBiography);
        requestQueue = Volley.newRequestQueue(this);
        final ProgressDialog dialog = ProgressDialog.show(this, null, getString(R.string.actorLoading));

        parseActorInformation();
        dialog.dismiss();

    }

    public void parseActorInformation() {

        String actorUrl = "https://api.themoviedb.org/3/person/" + actorId + "?api_key=7005ceb3ddacaaf788e2327647f0fa57&language=en-US";

        JsonObjectRequest actorRequest = new JsonObjectRequest(Request.Method.GET, actorUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            String actorName = response.getString("name");
                            String actorBirthday = response.getString("birthday");
                            String actorDeathday = response.getString("deathday");
                            String actorBirthPlace = response.getString("place_of_birth");
                            String actorBiography = response.getString("biography");

                            String actorProfilePicturePath = response.getString("profile_path");


                            String fullActorProfilePictureUrl = "https://image.tmdb.org/t/p/original" + actorProfilePicturePath;


                            Glide.with(ActorsActivity.this)
                                    .load(fullActorProfilePictureUrl)
                                    .centerCrop()
                                    .into(actorProfilePictureImage);
                            actorNameText.setText(actorName);
                            actorBirthdayText.setText(actorBirthday);
                            if (!actorDeathday.equals("null")) {
                                actorDeathDayText.setText(actorDeathday);
                            }
                            actorBirthPlaceText.setText(actorBirthPlace);
                            actorBiographyText.setText(actorBiography);

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
        requestQueue.add(actorRequest);
    }
}
