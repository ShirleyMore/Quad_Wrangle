package com.example.quadwrangle.game_view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.quadwrangle.R;

public class AnimationActivity extends AppCompatActivity {

    final int numOfImages = 32;
    boolean stopAnimation = false;
    private int[] animationId = new int[numOfImages];
    AnimationActivity.MyHandler handler;
    ImageView background;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);
        background = findViewById(R.id.background);
        initAnimation(); // put the images into the array
        // handler for animation transformer
        handler = new AnimationActivity.MyHandler();
        AnimatedGif animatedGif = new AnimatedGif("Opening");
        animatedGif.start();
    }

    public void initAnimation() { // puts the images into the array
        for( int i = 0; i < numOfImages; i++) {
            this.animationId[i] = this.getResources().getIdentifier
                    ("vertical_image_" + i, "drawable", this.getPackageName());
        }
    }

    public class AnimatedGif extends Thread{
        private int i; // the number of the current photo
        private String name;

        public AnimatedGif(String name)  {
            this.name = name;
            this.i = 0; // starting at photo 0
        }

        // I used a thread so that I could stop
        // the animation of the button is clicked
        public void run()  {
            while (this.i < numOfImages & !stopAnimation)   {
                try { // wait a little between each photo
                    Thread.sleep(100);
                } catch (InterruptedException ex)  {
                    ex.printStackTrace();
                }
                // send the information to the activity to display
                sendCounter2Activity(i);
                this.i++; // next photo
            }
        }
        private void sendCounter2Activity(int index)
        {
            // sending the handler the message -> the index
            // of the current photo to display
            Message msg = handler.obtainMessage();
            Bundle data = msg.getData();
            data.putInt("index", index);
            handler.sendMessage(msg);
        }
    }


    public class MyHandler extends Handler
    {

        @Override
        public void handleMessage(Message msg)
        {
            // getting the index from the message
            Bundle data = msg.getData();
            int count = data.getInt("index");
            // setting background to the correct image
            background.setImageResource(animationId[count]);
            // if we finished the animation - go to the next screen
            if (count == numOfImages-1)
                start();
        }

    }

    public void start() { // starting the next screen
        Intent intent = new Intent(this, DrawerActivity.class);
        // remove the small pop up animation so that my animation will appear smoother
        overridePendingTransition(0, 0);
        startActivity(intent);
    }

    // on the current thread - click to skip animation
    public void onClick(View view) {
        stopAnimation = true; // stop animation
        start(); // start the game
    }
}


