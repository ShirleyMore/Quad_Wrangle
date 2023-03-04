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
import com.example.quadwrangle.game_view.ui.saved_games.IClickable;

import java.util.ArrayList;

public class MySavedGamesAdapter extends RecyclerView.Adapter<MySavedGamesAdapter.MyViewHolder> implements IClickable {

    Context context;
    ArrayList<GameInSavedGamesPage> savedGamesArrayList;
    private final IClickable iClickable;

    public MySavedGamesAdapter(Context context, ArrayList<GameInSavedGamesPage> savedGamesArrayList, IClickable iClickable) {
        this.context = context;
        this.savedGamesArrayList = savedGamesArrayList;
        this.iClickable = iClickable;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.saved_game_list_item, parent, false);

        return new MyViewHolder(view, iClickable);
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

    @Override
    public void onItemClick(int position) {

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView save_name_text;
        TextView type_text;
        TextView date_text;

        public MyViewHolder(@NonNull View itemView, IClickable iClickable) {
            super(itemView);
            save_name_text = itemView.findViewById(R.id.save_name_text);
            type_text = itemView.findViewById(R.id.type_text);
            date_text = itemView.findViewById(R.id.date_text);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (iClickable != null) {
                        int position = getAdapterPosition();

                        if (position != RecyclerView.NO_POSITION)
                            iClickable.onItemClick(position);
                    }
                // https://www.youtube.com/watch?v=7GPUpvcU1FE&t=61s
                }
            });
        }
    }
}


