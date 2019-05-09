package com.example.filmapp;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.ViewHolder> {

    private Context context;
    private ArrayList<CreditPerson> creditList;

    public CastAdapter(Context context, ArrayList<CreditPerson> creditList) {
        this.context = context;
        this.creditList = creditList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.credits_layout, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        CreditPerson creditPerson = creditList.get(i);

        viewHolder.character.setText(creditPerson.getCharacterJob());
        viewHolder.name.setText(creditPerson.getName());
        Glide.with(context)
                .load(creditPerson.getProfileImageUrl())
                .into(viewHolder.profileImage);

    }

    @Override
    public int getItemCount() {
        return creditList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView profileImage;
        public TextView name;
        public TextView character;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.profile_image);
            name = itemView.findViewById(R.id.name);
            character = itemView.findViewById(R.id.character);
        }
    }
}
