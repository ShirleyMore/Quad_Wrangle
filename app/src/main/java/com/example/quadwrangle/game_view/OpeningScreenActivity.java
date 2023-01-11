package com.example.quadwrangle.game_view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.quadwrangle.R;

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
        Animation.AnimationListener animListener = new Animation.AnimationListener(){

            // Required to change the image
            int count = 0;

            @Override
            public void onAnimationStart(Animation animation) {
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


    public void openMenu(View txt) {

    }
}