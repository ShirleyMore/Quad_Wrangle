package com.example.quadwrangle.game_view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.quadwrangle.R;
import com.example.quadwrangle.game_model.MyService;

public class OpeningScreenActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening_screen);
        ImageView start_txt = findViewById(R.id.start_bttn);
        Animation animationFadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation animationFadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);

        // adding fade animation for txt
        // animation fade in and out
        Animation.AnimationListener animListener = new Animation.AnimationListener() {

            // Required to change the image
            int count = 0;

            @Override
            public void onAnimationStart(Animation animation) {
                onAnimationRepeat(animationFadeIn);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (animation == animationFadeIn) {

                    // Start fade-out animation
                    start_txt.startAnimation(animationFadeOut);

                } else if (animation == animationFadeOut) {

                    start_txt.startAnimation(animationFadeIn);
                }
            }
        };

        // Set listener to animation
        animationFadeIn.setAnimationListener(animListener);
        animationFadeOut.setAnimationListener(animListener);

        // Start fade-in animation
        start_txt.startAnimation(animationFadeIn);
    }


    public void startOnClick() {
        Intent intent = new Intent(this, ConnectionActivity.class);
        // remove the small pop up animation so that my animation will appear smoother
        overridePendingTransition(0, 0);
        startActivity(intent);
    }


    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        startOnClick();
    }
}