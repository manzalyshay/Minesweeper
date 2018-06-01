package com.example.shaym.minesweeper.UI.Adapter;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.shaym.minesweeper.GameLogic.Board;
import com.example.shaym.minesweeper.GameLogic.Game;
import com.example.shaym.minesweeper.GameLogic.Tile;
import com.example.shaym.minesweeper.UI.GameActivity;
import com.example.shaym.minesweeper.UI.Views.TileView;

import java.util.Random;

import static com.example.shaym.minesweeper.R.drawable;
import static com.example.shaym.minesweeper.R.raw;

public class TileAdapter extends BaseAdapter {

    private Board mBoard;
    private Game mGame;
    private Context mContext;
    private MediaPlayer mLosePlayer;
    private static final int animDuration = 3700;
    private int maxRand = 3300;
    private int minRand = 180;
    private ValueAnimator Xanim;
    private ValueAnimator Yanim;
    private Handler timerHandler = new Handler();

    public TileAdapter(Context context, Game game) {
        mGame = game;
        mBoard = game.getBoard();
        mLosePlayer = MediaPlayer.create(context, raw.lose);
        mContext = context;
    }

    @Override
    public int getCount() {
        return mBoard.getNumOfTiles();
    }

    // returns tile according to location
    @Override
    public Object getItem(int position) {
        return mBoard.getTile(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TileView tileView;
        tileView = (TileView) convertView;
        if (tileView == null) {
            tileView = new TileView(mContext);
            Log.v("Tile Adapter", "creating new view for index " + position);
        } else {
            Log.e("Tile Adapter", "RECYCLING view for index " + position);
        }

        Tile tile = mBoard.getTile(position);

        if (tile.isVisible()) {
            if (tile.isMine()) {
                tileView.setBackgroundResource(drawable.mine);
            } else {
                tileView.setBackgroundResource(drawable.pressed_tile);
            }
        } else {
            if (tile.isFlagged() == false)
                tileView.setBackgroundResource(drawable.tile);
            else {
                tileView.setBackgroundResource(drawable.flag);
            }
        }

         int row = mBoard.getTile(position).getRow();
         int col = mBoard.getTile(position).getCol();

        if (mGame.isLoss())
            loseCeremony(tileView, row, col);

        if (!tile.isMine() && !tile.isFlagged())
            tileView.setText(tile.getValueString());

        return tileView;
    }

    private void loseCeremony(final TileView tileView, final int row, final int col) {
        GameActivity.getSmile().setImageResource(drawable.smiley3);

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                mLosePlayer.start();
                timerHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        GameActivity.getLoser().startAnimation(GameActivity.getBlink());
                        initLoseAnimByPos(tileView, row, col);
                    }
                }, 2000);
            }
        });
        t.start();
    }

    private void initLoseAnimByPos(final TileView tileView, final int row, final int col) {
        Random random = new Random();
        int addCurve = random.nextInt(maxRand) + minRand;

        // split board 
        int midW = (mBoard.getBoardSize()) / 2;
        int midH = (mBoard.getBoardSize()) / 2;

        // get current position of the tile
        int curX = tileView.getHeight() - 100;
        int curY = tileView.getWidth() - 100;
        int newX = 0, newY = 2 * getScrHeight();

        if ((row >= midH && col >= midW) || (row < midH && col < midW))
            newX = getScrWidth() + addCurve;
        else
            newX = -getScrWidth() - addCurve;

        // start tile animation
        startLoseAnimation(tileView, curX, curY, newX, newY);
    }

    private void startLoseAnimation(final TileView tileView, final int curX, final int curY, final int newX, final int newY) {
        Xanim = ValueAnimator.ofFloat(curX, newX);
        Yanim = ValueAnimator.ofFloat(curY, newY);

        // set animation for X value
        Xanim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float val = (float) animation.getAnimatedValue();

                tileView.setTranslationX(val);
            }
        });

        // set animation for Y value
        Yanim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float val = (float) animation.getAnimatedValue();

                tileView.setTranslationY(val);
            }
        });

        ObjectAnimator rotate = ObjectAnimator.ofFloat(tileView, "rotation", 0, 270f);

        // set animSet for connection between *position* and *rotation* of the tile
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(Xanim).with(rotate);
        animSet.play(Yanim).with(rotate);

        animSet.setDuration(animDuration);
        animSet.start();
    }

    // return screen height
    public static int getScrHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    // return screen width
    public static int getScrWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }
}
