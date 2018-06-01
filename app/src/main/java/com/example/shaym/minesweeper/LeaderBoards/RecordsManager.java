package com.example.shaym.minesweeper.LeaderBoards;

import android.content.Context;
import android.location.Location;

import com.example.shaym.minesweeper.Storage.DatabaseHandler;
import com.example.shaym.minesweeper.Storage.Record;

import java.util.ArrayList;
import java.util.List;

public class RecordsManager {
    private DatabaseHandler db;
    private List<Record> allRecords;
    private List<Record> easyList;
    private List<Record> mediumList;
    private List<Record> hardList;

    public RecordsManager(Context context) {
        this.db = new DatabaseHandler(context);
        // gets an arraylist of all records in DB and divide them by lvl
        this.allRecords = db.getAllRecords();
        initRecordLists();
    }

    public List<Record> getEasyList() {
        return easyList;
    }

    public List<Record> getMediumList() {
        return mediumList;
    }

    public List<Record> getHardList() {
        return hardList;
    }

    public List<Record> getAllRecords() {
        return allRecords;
    }

    public List<Record> getListbyLvl(int lvl){
        switch (lvl){
            case 1:
                return getEasyList();
            case 2:
                return getMediumList();
            case 3:
                return getHardList();
        }
        return null;
    }
    // init lists according to lvl
    private void initRecordLists(){
        easyList = new ArrayList<>();
        mediumList = new ArrayList<>();
        hardList = new ArrayList<>();

        for (int i = 0; i<allRecords.size(); i++){
            switch(allRecords.get(i).getLvl()){
                case 1:
                    easyList.add(allRecords.get(i));
                    break;
                case 2:
                    mediumList.add(allRecords.get(i));
                    break;
                case 3:
                    hardList.add(allRecords.get(i));
                    break;
            }
        }
    }
        // adding rec to DB
    public void addRecord(Record record){
        db.addRecord(record);
        switch (record.getLvl()){
            case 1:
                    easyList.add(record);
                break;
            case 2:
                    mediumList.add(record);
                break;
            case 3:
                    hardList.add(record);
                break;
        }

    }
    // Removes record from DB
    public void removeRecord(Record record){
        db.deleteRecord(record);
        switch (record.getLvl()){
            case 1:
                easyList.remove(record);
                break;
            case 2:
                mediumList.remove(record);
                break;
            case 3:
                hardList.remove(record);
                break;
        }
    }
        // Getting the worst record (slowest)
    public Record getWorst (int lvl){

        switch (lvl){
            case 1:
                if (easyList.size() > 0)
                    return easyList.get(easyList.size()-1);
                break;
            case 2:
            if (mediumList.size() > 0)
                return mediumList.get(mediumList.size()-1);
            break;
            case 3:
            if (hardList.size() > 0)
                return hardList.get(hardList.size()-1);
            break;

        }

       return null;
    }




}
