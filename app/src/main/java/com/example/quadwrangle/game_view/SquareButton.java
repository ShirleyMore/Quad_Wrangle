package com.example.quadwrangle.game_view;

import android.annotation.SuppressLint;
import android.content.Context;

import com.example.quadwrangle.R;

// this is a class made to give image views more parameters like row , col
@SuppressLint("ViewConstructor")
public class SquareButton extends androidx.appcompat.widget.AppCompatImageView {
    private static final int black_img = R.drawable.disc_black;
    private static final int white_img = R.drawable.disc_white;
    private static final int black_img_glow = R.drawable.disc_black_glow;
    private static final int white_img_glow = R.drawable.disc_white_glow;

    private final int row;
    private final int col;

    public int getRow() {
        return this.row;
    }
    public int getCol() {
        return this.col;
    }

    public SquareButton(Context context, int row, int col) {
        super(context); // הסופר ביקש קונטקסט
        this.row = row;
        this.col = col;
        this.setHovered(true);
        this.setPadding(10,10,10,10);
    }

    public void setPressedImg(int player) {
        if (player == 1)
            this.setImageResource(black_img_glow);
        else
            this.setImageResource(white_img_glow);
    }

    public void setPlayerImg(int player) {
        if (player == 1) {
            this.setImageResource(black_img);
        }
        else if (player == -1)
            this.setImageResource(white_img);
        else {
            // empty image
            this.setImageDrawable(null);
            this.setEnabled(true);
        }
    }

}
