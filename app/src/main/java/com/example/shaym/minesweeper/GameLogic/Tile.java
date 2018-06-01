
package com.example.shaym.minesweeper.GameLogic;

public class Tile {

    private int mRow;
    private int mCol;
    private boolean mVisible = false;
    private boolean mIsFlagged = false;
    private int value = TileState.NONE.val();

    public Tile(int i, int j) {
        this.mRow = i;
        this.mCol = j;
    }

    public boolean isVisible() {
        return mVisible;
    }
    // set a tile as visible
    public void setVisible(boolean visible) {
        mVisible = visible;
    }

    public int getRow() {
        return mRow;
    }

    public void setRow(int row) {
        mRow = row;
    }

    public int getCol() {
        return mCol;
    }

    public void setCol(int col) {
        mCol = col;
    }

    public boolean isMine() {
        return getValue() == TileState.MINE.val();
    }

    public boolean isFlagged() {
        return mIsFlagged;
    }

    public void flagTile(boolean flagged) {
        mIsFlagged = flagged;
        setValue(mIsFlagged ? Tile.TileState.FLAG.val() : Tile.TileState.NONE.val());
    }

    public int getValue() {
        return value;
    }

    public void setValue(final int value) {
        this.value = value;
    }
    // sets value according to location
    public void setValueIfNotMine() {
        if (this.getValue() != Tile.TileState.MINE.val())
            this.incrementValue();
    }

    public void incrementValue() {
        value++;
    }

    public void reveal() {
        setVisible(true);
    }

    public String getValueString() {
        if (isVisible() && value != TileState.NONE.val()) {
            return "" + value;
        }
        return "";
    }
    //tile optional states and values
    public enum TileState {
        NONE(0), ONE(1), TWO(2), THREE(3), FOURE(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8), FLAG(-2), MINE(-1),;

        private int mValue;

        TileState(int value) {
            mValue = value;
        }

        public int val() {
            return mValue;
        }

        @Override
        public String toString() {
            switch (this) {

                case NONE:
                default:
                    return "";
                case ONE:
                    return "1";
                case TWO:
                    return "2";
                case THREE:
                    return "3";
                case FOURE:
                    return "4";
                case FIVE:
                    return "5";
                case SIX:
                    return "6";
                case SEVEN:
                    return "7";
                case EIGHT:
                    return "8";
                case FLAG:
                    return "";
                case MINE:
                    return "-1";

            }
        }
    }


}
