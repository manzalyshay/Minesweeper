package com.example.shaym.minesweeper.UI.Views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;


public class TileView extends TextView {
    // inits tile view and appearance
    public TileView(Context context) {
        super(context);

        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setLayoutParams(layoutParams);

        setGravity(Gravity.CENTER_HORIZONTAL);
        setTextSize(20);
        setTextColor(Color.BLACK);

        setBackgroundColor(Color.GRAY);
    }

    public TileView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public TileView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

}
