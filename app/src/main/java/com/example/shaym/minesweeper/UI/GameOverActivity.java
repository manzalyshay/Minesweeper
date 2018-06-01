package com.example.shaym.minesweeper.UI;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shaym.minesweeper.Constant;
import com.example.shaym.minesweeper.LeaderBoards.RecordsManager;
import com.example.shaym.minesweeper.R;
import com.example.shaym.minesweeper.Storage.Record;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

public class GameOverActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final String KEY_IS_WIN = "is_win";
    private static final String KEY_SCORE = "score";
    private static final String KEY_RECORD = "record";
    private static final String KEY_LEVEL = "lvl";
    private AlertDialog.Builder mTextDialog;
    private static String mRecordName = "";
    private static Location mUserLocation = null;
    private Handler mTimerHandler = new Handler();
    private GoogleApiClient mGoogleApiClient;
    private GoogleApiClient client;
    private static final int PERMISSION_ACCESS_COARSE_LOCATION = 1;
    private static RecordsManager mRecordsManager;
    private static int mLvl;
    private EditText input;
    private static final String TAG = GameOverActivity.class.getSimpleName();
    private int rawtime;
    private boolean isWin;
    private String timeStr;


    // Inits game over screen by passing latest game status, score, level
    public static void startActivity(Context context, boolean isWin, String record, int score, String lvl) {
        final Intent intent = new Intent(context, GameOverActivity.class);
        intent.putExtra(KEY_IS_WIN, isWin);
        intent.putExtra(KEY_RECORD, record);
        intent.putExtra(KEY_SCORE, score);
        intent.putExtra(KEY_LEVEL, lvl);
        mRecordsManager = MainActivity.getRecordsManager();
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        // connect location services
        mGoogleApiClient.connect();
        // inits records dialog
        initDialog();
        setContentView(R.layout.activity_game_over);

        TextView yourScoreView = (TextView) findViewById(R.id.score);

        boolean isWin = getIntent().getBooleanExtra(KEY_IS_WIN, false);
        this.isWin = isWin;
        final String record = getIntent().getStringExtra(KEY_RECORD);
        timeStr = record;
        int score = getIntent().getIntExtra(KEY_SCORE, 0);
        rawtime = score;
        final String lvl = getIntent().getStringExtra(KEY_LEVEL);
        switch (lvl) {
            case Constant.EASY_LVL:
                mLvl = 1;
                break;
            case Constant.MEDUIM_LVL:
                mLvl = 2;
                break;
            case Constant.HARD_LVL:
                mLvl = 3;
                break;
        }
        // display win\lost pic
        ImageView status = (ImageView) findViewById(R.id.status);
        if (isWin) {
            status.setImageResource(R.drawable.win);
            yourScoreView.setText("Your record is: " + record);
            if (mRecordsManager.getListbyLvl(mLvl).size() < Constant.MAX_RECORDS_ALLOWED || mRecordsManager.getWorst(mLvl).getRawtime() > rawtime) {
                mTimerHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // pops the dialog only if we have place on db or if better than worst record in division
                        mTextDialog.show();
                    }
                }, 3000);
            }
        } else {
            status.setImageResource(R.drawable.losepic);
            yourScoreView.setText("");
        }

        // starts a new game by last level chosen
        Button tryagain = (Button) findViewById(R.id.tryAgain);
        tryagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                GameActivity.start(GameOverActivity.this, lvl);
                finish();
            }
        });


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.


    }

    // inits recors dialog
    public void initDialog() {
        mTextDialog = new AlertDialog.Builder(GameOverActivity.this);
        mTextDialog.setTitle("Your'e in the record books!\n" +
                "Please enter your name:");

// Set up the input
        input = new EditText(GameOverActivity.this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        mTextDialog.setView(input);

// Set up the buttons
        mTextDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                mRecordName = input.getText().toString();
                addRecord();
            }
        });

        Log.i(TAG, mRecordName.toString());


    }

    @Override
    public void onStart() {
        super.onStart();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        mGoogleApiClient.connect();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();// ATTENTION: This was auto-generated to implement the App Indexing API.
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
    // add record to database
    public void addRecord() {
        Record temp = new Record(rawtime, mLvl, mRecordName, timeStr, mUserLocation);
        if (mRecordsManager.getListbyLvl(mLvl).size() == Constant.MAX_RECORDS_ALLOWED) {
            mRecordsManager.removeRecord(mRecordsManager.getWorst(mLvl));
            mRecordsManager.addRecord(temp);
        } else
            mRecordsManager.addRecord(temp);
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("GameOver Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }
    // samples location
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "Location services connected.");
        // ask permission
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSION_ACCESS_COARSE_LOCATION);
        }

        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (lastLocation != null) {
            this.mUserLocation = lastLocation;
        }
        if (mUserLocation == null)
            Log.i(TAG, "Location not sampled");
    }

    @Override
    public void onConnectionSuspended(final int i) {
        Log.i(TAG, "Location services suspended. Please reconnect.");
    }

    @Override
    public void onConnectionFailed(@NonNull final ConnectionResult connectionResult) {

    }


}

