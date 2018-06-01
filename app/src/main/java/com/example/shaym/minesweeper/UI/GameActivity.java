package com.example.shaym.minesweeper.UI;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shaym.minesweeper.GameLogic.Game;
import com.example.shaym.minesweeper.R;
import com.example.shaym.minesweeper.UI.Adapter.TileAdapter;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class GameActivity extends AppCompatActivity implements SensorEventListener {

    public static final String LVL = "LVL";
    private Game mGame;
    private Timer myTimer;
    private TextView timerTxt;
    private GridView gridview;
    private String mRecord;
    private Handler timerHandler = new Handler();
    private TileAdapter mAdapter;
    private TextView minesLeft;
    private static ImageView smile;
    private static ImageView loser;
    private static Animation blink;
    private ImageView youwin;
    private ImageView mexicansmiley;
    private int seconds;
    private int minutes;
    private final int LIMIT = 10;
    private Animation sequental;
    private Animation rotate;
    private MediaPlayer mWinPlayer;
    private boolean isWarned = false;
    private SensorManager sensorManager;
    private long lastUpdate;
    private Boolean bool = false, bool1 = false;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient mClient;

    // starts a new game by level chosen
    public static void start(Activity context, String lvl) {
        final Intent intent = new Intent(context, GameActivity.class);
        intent.putExtra(LVL, lvl);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        final Bundle extras = getIntent().getExtras();
        final String level = extras.getString(LVL);

        mGame = new Game(level);

        InitView();

        initSensor();

        // Starts timer
        myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() { // Background Thread
                timerHandler.post(new Runnable() {
                    @Override
                    public void run() { // UI Thread
                        seconds++;
                        updateTime();
                    }
                });
            }

        }, 0, TimeUnit.SECONDS.toMillis(1));

        gridview.setOnItemClickListener(getOnGridClickListener());

        gridview.setOnItemLongClickListener(getOnLongClickListener());
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mClient = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

    }

    private void initSensor() {
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        lastUpdate = System.currentTimeMillis();
    }

    private void InitView() {
        // inits game bar
        smile = (ImageView) findViewById(R.id.smile);
        loser = (ImageView) findViewById(R.id.gameover);
        youwin = (ImageView) findViewById(R.id.youwin);
        mexicansmiley = (ImageView) findViewById(R.id.mexicansmiley);
        minesLeft = (TextView) findViewById(R.id.minesLeft);
        timerTxt = (TextView) findViewById(R.id.timerTxt);
        updateMineLeft();

        //inits animations
        blink = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
        rotate = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.rotate);
        sequental = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.sequental);
        mWinPlayer = MediaPlayer.create(this, R.raw.win);

        //inits gridview
        gridview = (GridView) findViewById(R.id.gridView);
        gridview.setNumColumns(mGame.getBoard().getBoardSize());
        mAdapter = new TileAdapter(getApplicationContext(), mGame);
        gridview.setAdapter(mAdapter);
    }

    // mark tile as flagged
    @NonNull
    private AdapterView.OnItemLongClickListener getOnLongClickListener() {
        return new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (!mGame.getBoard().getTile(position).isVisible()) {
                    mGame.markTileAsFlagged(position);

                    ((TileAdapter) gridview.getAdapter()).notifyDataSetChanged();

                    updateMineLeft();

                    return true;
                }
                return false;
            }
        };
    }

    private void updateMineLeft() {
        minesLeft.setText("" + (mGame.getBoard().getNumofmines() - mGame.getNumOfFlags()));
    }

    @NonNull
    private AdapterView.OnItemClickListener getOnGridClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {

                mGame.tileClicked(position);
                mAdapter.notifyDataSetChanged();
                if (mGame.isLoss())
                    finishGame();
                else if (mGame.isWin()) {
                    winCeremony();
                    finishGame();
                }
            }


        };
    }

    private void winCeremony() {
        youwin.startAnimation(blink);

        //Plays music on diffrent thread
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                mWinPlayer.start();
            }
        });
        t.start();

        //Start victory lap
        gridview.setVisibility(View.INVISIBLE);
        mexicansmiley.startAnimation(rotate);
        mexicansmiley.startAnimation(sequental);
    }

    private void finishGame() {
        gridview.setEnabled(false);
        myTimer.cancel();
        // Creates a delay for the user, finishes game, passing scores and intent
        timerHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (mGame.isWin()) {
                    int rawtime = seconds + minutes * 60;
                    GameOverActivity.startActivity(GameActivity.this, true, mRecord, rawtime, mGame.getLevel());
                } else {
                    mRecord = "";
                    seconds = 0;
                    GameOverActivity.startActivity(GameActivity.this, false, mRecord, seconds, mGame.getLevel());
                }
                finish();
            }
        }, 7000);

    }

    // timer settings
    private void updateTime() {
        String minutesString = "";
        String secondsString = "";

        if (seconds % 60 == 0) {
            seconds = 0;
            minutes++;
        }

        if (minutes < LIMIT)
            minutesString = "0" + minutes;
        else
            minutesString = "" + minutes;

        if (seconds < LIMIT)
            secondsString = "0" + seconds;
        else
            secondsString = "" + seconds;
        mRecord = minutesString + ":" + secondsString;
        timerTxt.setText(mRecord);
    }

    public static ImageView getSmile() {
        return smile;
    }

    public static ImageView getLoser() {
        return loser;
    }

    public static Animation getBlink() {
        return blink;
    }

    @Override
    protected void onStop() {
        sensorManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).size()!=0){
            Sensor s = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
            sensorManager.registerListener(this,s, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onAccuracyChanged(final Sensor sensor, final int accuracy) {

    }

    // On sensor event actions
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float[] values = event.values;
            // Movement
            float x = values[0];
            float y = values[1];
            float z = values[2];

            float accelationSquareRoot = (x * x + y * y + z * z)
                    / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
            System.out.println("accelationSquareRoot is:- "
                    + accelationSquareRoot);
            long actualTime = System.currentTimeMillis();
            if (accelationSquareRoot >= 5) //
            {
                bool1 = false;
                if (bool == false) {
                    if (actualTime - lastUpdate < 1000) {
                        return;
                    }
                    lastUpdate = actualTime;
                    if(!isWarned) { //
                        Toast.makeText(this, "Watch out! Device is moving, Danger!!", Toast.LENGTH_SHORT).show();
                        isWarned = true;
                    }else{
                        Toast.makeText(this, "Mines added!!", Toast.LENGTH_SHORT).show();
                        mGame.addMines();
                        updateMineLeft();
                        mAdapter.notifyDataSetChanged();
                    }
                    bool = true;
                } else {
                    return;
                }
            } else {
                bool = false;
                System.out.println("Jay Not Stuffing");

                if (bool1 == false) {
                    if (actualTime - lastUpdate < 4000) {
                        return;
                    }
                    lastUpdate = actualTime;
                    //Here Define Method for OnStopShaking
                    bool1 = true;
                } else {
                    return;
                }
            }
        }
    }


}
