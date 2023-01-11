package com.example.quadwrangle.game_view.ui.home;

import static com.example.quadwrangle.game_model.database.SavedGame.board_toString;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quadwrangle.R;
import com.example.quadwrangle.databinding.FragmentHomeBinding;
import com.example.quadwrangle.game_model.database.GameInSavedGamesPage;
import com.example.quadwrangle.game_model.database.SavedGame;
import com.example.quadwrangle.game_model.database.SavedGamesDbManager;
import com.example.quadwrangle.game_view.Adapters.MySavedGamesAdapter;
import com.example.quadwrangle.game_view_model.savedGamesDbConnector;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class HomeFragment extends Fragment { /// saved games activity table

    private FragmentHomeBinding binding;
    private RecyclerView savedGamesRecyclerView;
    private ArrayList<GameInSavedGamesPage> gamesArrayList;
    private MySavedGamesAdapter myAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home,container, false);
        View root = binding.getRoot();
        binding.setLifecycleOwner(this);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        savedGamesRecyclerView = view.findViewById(R.id.savedGamesRecyclerView);
        savedGamesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        savedGamesRecyclerView.setHasFixedSize(true);

        gamesArrayList = new ArrayList<GameInSavedGamesPage>();

        myAdapter = new MySavedGamesAdapter(getContext(), gamesArrayList);
        savedGamesRecyclerView.setAdapter(myAdapter);
        getData();
    }



    private void getData() {
        savedGamesDbConnector connector = new savedGamesDbConnector(getContext()); // todo: id =1 for now
        GameInSavedGamesPage[] savedGamesForPage = connector.getAllSavedGamesForPage(1);
        gamesArrayList.addAll(Arrays.asList(savedGamesForPage));
        System.out.println(gamesArrayList.size());
        // sort from high to low by scores
        gamesArrayList.sort(Comparator.comparing(GameInSavedGamesPage::getDate));
        Collections.reverse(gamesArrayList);
        // notify the adapter that there has been a change
        myAdapter.notifyDataSetChanged();
    }

    public void testSaveGame() {
        // date
        Date now = new Date();
        long time = now.getTime();
        String date = date_ToString(time);
        // board
        int[][] board = new int[7][7];
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                board[i][j] = i*j;
            }
        }
        SavedGamesDbManager manager = new SavedGamesDbManager(this.getContext());
        String str = board_toString(board);
        SavedGame game = new SavedGame(1, "Save 2", date, str, "white", "2player");
        SavedGame game2 = new SavedGame(1, "Save 3", date, str, "black", "2player");
        manager.createSavedGame(game);
        manager.createSavedGame(game2);

    }
    public static String date_ToString(long date) {
        String pattern = "dd/MM/yyyy HH:mm:ss";
        @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat(pattern);
        return df.format(date);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}