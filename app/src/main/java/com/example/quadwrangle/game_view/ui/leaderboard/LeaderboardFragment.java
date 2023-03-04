package com.example.quadwrangle.game_view.ui.leaderboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quadwrangle.R;
import com.example.quadwrangle.databinding.FragmentLeaderboardBinding;
import com.example.quadwrangle.game_model.database.User;
import com.example.quadwrangle.game_model.database.UserDbManager;
import com.example.quadwrangle.game_model.database.UserInLeaderboard;
import com.example.quadwrangle.game_view.Adapters.MyUsernameAdapter;
import com.example.quadwrangle.game_view.ui.leaderboard.LeaderboardViewModel;
import com.example.quadwrangle.game_view_model.UserDbLeaderboardConnector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class LeaderboardFragment extends Fragment {

    private FragmentLeaderboardBinding binding;
    private RecyclerView recyclerView;
    private ArrayList<UserInLeaderboard> usersArrayList;
    private MyUsernameAdapter myAdapter;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        LeaderboardViewModel slideshowViewModel =
                new ViewModelProvider(this).get(LeaderboardViewModel.class);

        binding = FragmentLeaderboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        createUserTest();
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        usersArrayList = new ArrayList<UserInLeaderboard>();

        myAdapter = new MyUsernameAdapter(getContext(), usersArrayList);
        recyclerView.setAdapter(myAdapter);

        getData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    private void getData() {
        UserDbLeaderboardConnector connector = new UserDbLeaderboardConnector(getContext());
        UserInLeaderboard[] usersForLeaderboard = connector.getAllUsersInLeaderboard();
        usersArrayList.addAll(Arrays.asList(usersForLeaderboard));
        // sort from high to low by scores
        usersArrayList.sort(Comparator.comparing(UserInLeaderboard::getScore));
        Collections.reverse(usersArrayList);
        // notify the adapter that there has been a change
        myAdapter.notifyDataSetChanged();
    }

    private void createUserTest() {
        System.out.println("yes");
        UserDbManager manager = new UserDbManager(getContext());
        User user = new User("chen", "shadwad", 0);
        /*
        User myUser = manager.logIn(name_txt.getText().toString(), password_txt.getText().toString());
        System.out.println(myUser);
            */
        user = manager.createUser(user);
        System.out.println(user);
    }
}