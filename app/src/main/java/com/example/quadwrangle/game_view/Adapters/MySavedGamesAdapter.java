package com.example.quadwrangle.game_view.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quadwrangle.R;
import com.example.quadwrangle.game_model.database.GameInSavedGamesPage;

import java.util.ArrayList;

public class MySavedGamesAdapter extends RecyclerView.Adapter<MySavedGamesAdapter.MyViewHolder>{

    Context context;
    ArrayList<GameInSavedGamesPage> savedGamesArrayList;

    public MySavedGamesAdapter(Context context, ArrayList<GameInSavedGamesPage> savedGamesArrayList) {
        this.context = context;
        this.savedGamesArrayList = savedGamesArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.saved_game_list_item, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        GameInSavedGamesPage savedGame = savedGamesArrayList.get(position);
        holder.save_name_text.setText(savedGame.getName());
        holder.type_text.setText(savedGame.getType());
        holder.date_text.setText(savedGame.getDate());

        // https://www.youtube.com/watch?v=2O7i4KglRuw
    }

    @Override
    public int getItemCount() {
        return savedGamesArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView save_name_text;
        TextView type_text;
        TextView date_text;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            save_name_text = itemView.findViewById(R.id.save_name_text);
            type_text = itemView.findViewById(R.id.type_text);
            date_text = itemView.findViewById(R.id.date_text);
        }
    }
}


