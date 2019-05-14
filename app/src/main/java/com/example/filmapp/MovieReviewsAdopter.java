package com.example.filmapp;

        import android.content.Context;
        import androidx.annotation.NonNull;
        import androidx.recyclerview.widget.RecyclerView;

        import android.content.Intent;
        import android.os.Bundle;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.TextView;

        import com.bumptech.glide.Glide;
        import com.example.filmapp.People.people_view;

        import java.util.ArrayList;

public class MovieReviewsAdopter extends RecyclerView.Adapter<MovieReviewsAdopter.ViewHolder> {

    private Context context;
    private ArrayList<CreditPerson> reviewsList;
    private static final String TAG = "MovieReviewsAdopter";
    public MovieReviewsAdopter(Context context, ArrayList<CreditPerson> creditList) {
        this.context = context;
        this.reviewsList = creditList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movie_reviews_item, viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        CreditPerson moviewReview = reviewsList.get(i);

        viewHolder.message.setText(moviewReview.getProfileImageUrl());
        viewHolder.name.setText(moviewReview.getName());
        Glide.with(context)
                .load("http://www.atabjkpg.se/wp-content/uploads/2017/12/default-user-image.png")
                .into(viewHolder.profileImage);

    }

    @Override
    public int getItemCount() {
        return reviewsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {

        public ImageView profileImage;
        public TextView name;
        public TextView message;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.authorImage);
            name = itemView.findViewById(R.id.authorName);
            message = itemView.findViewById(R.id.messageReview);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
           /*inal CreditPerson currentItem = reviewsList.get(getAdapterPosition());
            Intent intent = new Intent (v.getContext(), people_view.class);
            Bundle mBundle = new Bundle();
            mBundle.putString("people_id", currentItem.getId());
            intent.putExtras(mBundle);
            context.startActivity(intent);*/
        }
    }
}
