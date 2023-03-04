package com.example.quadwrangle.game_view.ui.saved_games;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.quadwrangle.game_model.database.SavedGame;

public class SavedGamesFragmentViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public SavedGamesFragmentViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void loadGame(SavedGame extraData) {

    }
}