package com.example.shaym.minesweeper.UI;

import android.Manifest;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.shaym.minesweeper.Constant;
import com.example.shaym.minesweeper.LeaderBoards.RecordsManager;
import com.example.shaym.minesweeper.R;
import com.example.shaym.minesweeper.UI.LeaderBoards.LeaderBoardsActivity;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button playGame;
    private Button leaderBoards;
    private static final int PERMISSION_ACCESS_COARSE_LOCATION = 1;
    private static RecordsManager mRecordsManager;
    final static String PREF_NAME = "Game_Stats";
    final static String PREF_LEVEL_KEY = "lvl_key";
    private String mChosenLevel;
    private RadioGroup mLevelRadio;
    private RadioButton mEasyBtn;
    private RadioButton mMedBtn;
    private RadioButton mHardBtn;
    private SharedPreferences.Editor editor;
    SharedPreferences mySharedPref;


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient mClient;

    public static RecordsManager getRecordsManager() {
        return mRecordsManager;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSION_ACCESS_COARSE_LOCATION);
        }
        this.mRecordsManager = new RecordsManager(MainActivity.this);

        initButtons();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mClient = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void initButtons() {
        playGame = (Button) findViewById(R.id.plyGame);
        leaderBoards = (Button) findViewById(R.id.ldrbords);
        playGame.setOnClickListener(this);
        leaderBoards.setOnClickListener(this);
        mySharedPref = getSharedPreferences(PREF_NAME, 0);
        mChosenLevel = mySharedPref.getString(PREF_LEVEL_KEY, null);
        mLevelRadio = (RadioGroup) findViewById(R.id.radioGrp);
        mEasyBtn = (RadioButton) findViewById(R.id.easyBtn);
        mMedBtn = (RadioButton) findViewById(R.id.MedBtn);
        mHardBtn= (RadioButton) findViewById(R.id.HardBtn);
        if (mChosenLevel != null){
            switch (mChosenLevel){
                case Constant.EASY_LVL:
                    mLevelRadio.check(R.id.easyBtn);
                    break;
                case Constant.MEDUIM_LVL:
                    mLevelRadio.check(R.id.MedBtn);
                    break;
                case Constant.HARD_LVL:
                    mLevelRadio.check(R.id.HardBtn);
                    break;
            }
        }

    }

    // start game by level chosen
    @Override
    public void onClick(final View view) {
        SharedPreferences mySharedPref = getSharedPreferences(PREF_NAME, 0);
        SharedPreferences.Editor editor = mySharedPref.edit();
        switch (view.getId()) {
            case R.id.plyGame: {
                switch (mLevelRadio.getCheckedRadioButtonId()){
                    case R.id.easyBtn:
                        editor.putString(PREF_LEVEL_KEY, Constant.EASY_LVL);
                        editor.commit();
                        GameActivity.start(this, Constant.EASY_LVL);
                        break;
                    case R.id.MedBtn:
                        editor.putString(PREF_LEVEL_KEY, Constant.MEDUIM_LVL);
                        editor.commit();
                        GameActivity.start(this, Constant.MEDUIM_LVL);
                        break;
                    case R.id.HardBtn:
                        editor.putString(PREF_LEVEL_KEY, Constant.HARD_LVL);
                        editor.commit();
                        GameActivity.start(this, Constant.HARD_LVL);
                        break;
                }
                break;
            }
            case R.id.ldrbords: {
                LeaderBoardsActivity.start(this, 47.17, 27.5699);
                break;
            }

        }
    }


    protected void onResume() {
        super.onResume();


    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mClient.connect();
        AppIndex.AppIndexApi.start(mClient, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(mClient, getIndexApiAction());
        mClient.disconnect();
    }
}
