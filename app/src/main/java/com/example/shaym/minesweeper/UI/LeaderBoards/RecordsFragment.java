package com.example.shaym.minesweeper.UI.LeaderBoards;

/**
 * Created by shaym on 13/01/2017.
 */

import android.app.ListFragment;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.shaym.minesweeper.R;
import com.example.shaym.minesweeper.UI.MainActivity;

public class RecordsFragment extends ListFragment implements View.OnClickListener {
    private String[] records;
    private Button easyBtn;
    private Button mediumBtn;
    private Button HardBtn;
    private Location mChosenLocation;
    private int mLvl = 1;
    ArrayAdapter<String> adapter;

    // init listview
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_records_fragment, container, false);
        easyBtn = (Button) view.findViewById(R.id.easyRecBtn);
        mediumBtn = (Button) view.findViewById(R.id.MedRecBtn);
        HardBtn = (Button) view.findViewById(R.id.HardRecBtn);
        easyBtn.setOnClickListener(this);
        mediumBtn.setOnClickListener(this);
        HardBtn.setOnClickListener(this);
        initRecords();
        adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, records);
        setListAdapter(adapter);

        return view;

    }

        // inits records from DB, display in list form (tab view)
    public void initRecords() {
        int tempsize;
        switch (mLvl) {
            case 1:
                tempsize = MainActivity.getRecordsManager().getEasyList().size();
                this.records = new String[tempsize];
                for (int i = 0; i < records.length; i++)
                    this.records[i] = "" + (i + 1) + " |\t\t" + MainActivity.getRecordsManager().getEasyList().get(i).getName() + " |\t\t" + MainActivity.getRecordsManager().getEasyList().get(i).getTime();
                break;
            case 2:
                tempsize = MainActivity.getRecordsManager().getMediumList().size();
                this.records = new String[tempsize];
                for (int i = 0; i < records.length; i++)
                    this.records[i] = "" + (i + 1) + " |\t\t" + MainActivity.getRecordsManager().getMediumList().get(i).getName() + " |\t\t" + MainActivity.getRecordsManager().getMediumList().get(i).getTime();
                break;
            case 3:
                tempsize = MainActivity.getRecordsManager().getHardList().size();
                this.records = new String[tempsize];
                for (int i = 0; i < records.length; i++)
                    this.records[i] = "" + (i + 1) + " |\t\t" + MainActivity.getRecordsManager().getHardList().get(i).getName() + " |\t\t" + MainActivity.getRecordsManager().getHardList().get(i).getTime();
                break;
        }
    }

    //tab view on click
    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.easyRecBtn:
                this.mLvl = 1;
                initRecords();
                break;
            case R.id.MedRecBtn:
                this.mLvl = 2;
                initRecords();
                break;
            case R.id.HardRecBtn:
                this.mLvl = 3;
                initRecords();
                break;

        }
        ArrayAdapter <String> newadp = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, records);
        setListAdapter(newadp);

    }

    // record on click, each click sets new location to the map (record's location)
    @Override
    public void onListItemClick(ListView l, View v, int pos, long id) {
        super.onListItemClick(l, v, pos, id);
        if (pos < records.length) {
            mChosenLocation = MainActivity.getRecordsManager().getListbyLvl(mLvl).get(pos).getLocation();
            LeaderBoardsActivity.start(getActivity(), mChosenLocation.getLatitude(), mChosenLocation.getLongitude());
        } else
            mChosenLocation = null;
        Toast.makeText(getActivity(), "New location set", Toast.LENGTH_SHORT).show();
    }


}