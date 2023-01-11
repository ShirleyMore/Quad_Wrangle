package com.example.quadwrangle.game_view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import com.example.quadwrangle.R;

public class AnimationActivity extends AppCompatActivity {

    final int numOfImages = 32;
    private int[] animationId = new int[numOfImages];
    AnimationActivity.MyHandler handler;
    ImageView background;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);
        background = findViewById(R.id.background);
        initAnimation();
        // handler for animation transformer
        handler = new AnimationActivity.MyHandler();
        AnimatedGif animatedGif = new AnimatedGif("Opening");
        //animatedGif.start();


        int seconds_delay = (int)(0.5 * 1000);
        new CountDownTimer(seconds_delay*numOfImages, seconds_delay) {

            int i = 0;

            @Override
            public void onTick(long millisUntilFinished) {
                background.setImageResource(animationId[i]);
                i++;
            }

            @Override
            public void onFinish() {
                System.out.println("FINISHED!!!");
            }
        }.start();
    }

    public void initAnimation() {
        for( int i = 0; i < numOfImages; i++) {
            this.animationId[i] = this.getResources().getIdentifier("vertical_image_" + i, "drawable", this.getPackageName());
        }
    }

    public class AnimatedGif extends Thread{
        private int i;
        private String name;

        public AnimatedGif(String name)  {
            this.name = name;
            this.i = 0;
        }

        public void run()  {
            while (this.i < numOfImages)   {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex)  {
                    ex.printStackTrace();
                }
                sendCounter2Activity(i);
                this.i++;
            }
        }
        private void sendCounter2Activity(int index)
        {
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
            Bundle data = msg.getData();
            int count = data.getInt("index");

            background.setImageResource(animationId[count]);
            // update the Timer TextView

        } // handleMessage(...)

    }
}


