package com.example.quadwrangle.game_view.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quadwrangle.R;
import com.example.quadwrangle.game_model.database.UserInLeaderboard;

import java.util.ArrayList;

public class MyUsernameAdapter extends RecyclerView.Adapter<MyUsernameAdapter.MyViewHolder>{

    Context context;
    ArrayList<UserInLeaderboard> usersArrayList;

    public MyUsernameAdapter(Context context, ArrayList<UserInLeaderboard> usersArrayList) {
        this.context = context;
        this.usersArrayList = usersArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.user_list_item, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        UserInLeaderboard user = usersArrayList.get(position);
        holder.username_txt.setText(user.getUsername());
        holder.score_txt.setText(user.getScore() +"");

        // https://www.youtube.com/watch?v=2O7i4KglRuw
    }

    @Override
    public int getItemCount() {
        return usersArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView username_txt;
        TextView score_txt;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            username_txt = itemView.findViewById(R.id.username_text);
            score_txt = itemView.findViewById(R.id.score_text);
        }
    }
}


