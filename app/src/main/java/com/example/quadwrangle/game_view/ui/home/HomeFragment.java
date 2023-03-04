package com.example.quadwrangle.game_view.ui.home;


import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quadwrangle.R;
import com.example.quadwrangle.databinding.FragmentSavedGamesBinding;
import com.example.quadwrangle.game_model.database.GameInSavedGamesPage;
import com.example.quadwrangle.game_model.database.SavedGame;
import com.example.quadwrangle.game_view.Adapters.MySavedGamesAdapter;
import com.example.quadwrangle.game_view.ui.game.GameFragment;
import com.example.quadwrangle.game_view.ui.game.GameViewModel;
import com.example.quadwrangle.game_view_model.UserDbLeaderboardConnector;
import com.example.quadwrangle.game_view_model.SavedGamesDbConnector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

public class HomeFragment extends Fragment implements IClickable{ /// saved games activity table

    private FragmentSavedGamesBinding binding;
    private RecyclerView savedGamesRecyclerView;
    private ArrayList<GameInSavedGamesPage> gamesArrayList;
    private MySavedGamesAdapter myAdapter;
    private UserDbLeaderboardConnector userDbLeaderboardConnector;
    private SavedGamesDbConnector savedGamesDbConnector;
    private GameViewModel gameViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        gameViewModel = new ViewModelProvider(requireActivity()).get(GameViewModel.class);
        //
        //gameViewModel = new ViewModelProvider(requireActivity()).get(GameViewModel.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_saved_games,container, false);
        View root = binding.getRoot();
        binding.setLifecycleOwner(this);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // initialize userDbLeaderboardConnector
        userDbLeaderboardConnector = new UserDbLeaderboardConnector(this.getContext());
        // and saved games connector
        savedGamesDbConnector = new SavedGamesDbConnector(getContext());

        savedGamesRecyclerView = view.findViewById(R.id.savedGamesRecyclerView);
        savedGamesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        savedGamesRecyclerView.setHasFixedSize(true);

        gamesArrayList = new ArrayList<GameInSavedGamesPage>();

        myAdapter = new MySavedGamesAdapter(getContext(), gamesArrayList, this);
        savedGamesRecyclerView.setAdapter(myAdapter);
        getData();
        // notify the adapter that there has been a change
        myAdapter.notifyDataSetChanged();
    }


    private void getData() {
        GameInSavedGamesPage[] savedGamesForPage = savedGamesDbConnector.getAllSavedGamesForPage(userDbLeaderboardConnector.getMyId());
        gamesArrayList.addAll(Arrays.asList(savedGamesForPage));
        // sort from high to low by scores
        gamesArrayList.sort(Comparator.comparing(GameInSavedGamesPage::getDate));
        Collections.reverse(gamesArrayList);

    }



    // position has to be correct - > checked.
    @Override
    public void onItemClick(int position) {
        final Dialog dialog = new Dialog(this.getContext());
        dialog.setCancelable(true); // make it cancellable
        // connect to the layout
        dialog.setContentView(R.layout.load_game_dialog);
        GameInSavedGamesPage currentClicked = gamesArrayList.get(position);
        // set load name text
        TextView load_name = dialog.findViewById(R.id.load_name);
        load_name.setText("Name: " + currentClicked.getName());
        // set load type
        TextView load_type = dialog.findViewById(R.id.load_type);
        load_type.setText("Type: " + currentClicked.getType());
        // set load date
        TextView load_date = dialog.findViewById(R.id.load_date);
        load_date.setText("From " + currentClicked.getDate());
        // button to load:
        TextView load_game = dialog.findViewById(R.id.load_yes);
        load_game.setOnClickListener((V) -> {
            SavedGame extraData = savedGamesDbConnector.getExtraGameData(currentClicked.getId());
            if (extraData == null)
                Toast.makeText(this.getContext(), "Error Loading", Toast.LENGTH_SHORT).show();
            else {
                Toast.makeText(this.getContext(), "Loading game...", Toast.LENGTH_SHORT).show();
                // navigate to the game fragment

                Navigation.findNavController(this.requireView()).navigate(R.id.nav_game);
                System.out.println(Arrays.deepToString(gameViewModel.getBoard()));
                gameViewModel.loadGame(extraData);
                Toast.makeText(this.getContext(), "Game Loaded Successfully", Toast.LENGTH_SHORT).show();
                //mCallback.loadGame(extraData);
                //Navigation.findNavController(V).navigate(R.id.nav_game);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

}
