package com.example.shaym.minesweeper.Storage;

import android.location.Location;

public class Record implements Comparable {
    private int mRawtime;
    private int mLvl;
    private String mName;
    private String mTime;
    private Location mLocation;

    public Record() {
    }


    public Record(int rawtime, int lvl, String name, String time, Location location) {
        this.mRawtime = rawtime;
        this.mLvl = lvl;
        this.mName = name;
        this.mTime = time;
        this.mLocation = location;
    }

    public Location getLocation() {
        return mLocation;
    }

    public void setLocation(final Location location) {
        mLocation = location;
    }

    public int getLvl() {
        return mLvl;
    }

    public void setLvl(final int lvl) {
        mLvl = lvl;
    }

    public int getRawtime() {
        return mRawtime;
    }

    public void setRawtime(final int rawtime) {
        mRawtime = rawtime;
    }

    public String getName() {
        return mName;
    }

    public void setName(final String name) {
        mName = name;
    }

    public String getTime() {
        return mTime;
    }

    public void setTime(final String time) {
        mTime = time;
    }

    // Compares the records by raw time
    @Override
    public int compareTo(final Object o) {
        int cmp = ((Record) o).getRawtime();
        /* For Ascending order*/
        return cmp - this.getRawtime();
    }
}