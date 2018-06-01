package com.example.shaym.minesweeper.GameLogic;

import java.util.Random;

public class Game {
    private Board mBoard;
    private String mLevel;
    private int mNumOfFlags = 0;
    private boolean mLoss = false;
    private boolean mWin = false;
    private Boolean mineAdded = false;
    private Random random;

    public Game(String lvl) {
        mLevel = lvl;
        mBoard = new Board(mLevel);
    }

    public Board getBoard() {
        return mBoard;
    }

    public void tileClicked(int loc) {
        //setWin(true);
        if (mBoard.getTile(loc).isFlagged() || mBoard.getTile(loc).isVisible())
            return;

        else if (mBoard.getTile(loc).isMine()) {
            setLose(true);
            return;
        }

        makeStep(loc);
    }

    // make step by clicked tile
    private void makeStep(int loc) {

        if (mBoard.getTile(loc).getValue() != Tile.TileState.NONE.val()) {
            mBoard.getTile(loc).setVisible(true);
            mBoard.increaseRevealedTilesAmount();

            if (mBoard.getRevealedTilesAmount() == mBoard.getNumOfTiles() - mBoard.getNumofmines())
                setWin(true);

            return;
        }

        if (mBoard.getTile(loc).getValue() == Tile.TileState.NONE.val()) {
            mBoard.getTile(loc).setVisible(true);
            mBoard.increaseRevealedTilesAmount();

            int row = mBoard.getTile(loc).getRow();
            int col = mBoard.getTile(loc).getCol();

            for (int i = row - 1; i <= row + 1; i++) {
                for (int j = col - 1; j <= col + 1; j++) {

                    if (((j >= 0 && j < mBoard.getBoardSize()) && (i >= 0 && i < mBoard.getBoardSize()))) {

                        int newPosition = getBoard().getTileLocation(i, j);
                        tileClicked(newPosition);

                    }
                }
            }
        }
    }

    public void markTileAsFlagged(int loc) {
        Tile tile = mBoard.getTile(loc);

        if (!tile.isFlagged()) {
            tile.flagTile(true);
            increaseNumOfFlags();
        } else {
            tile.flagTile(false);
            decreaseNumOfFlags();
        }
    }

    public void increaseNumOfFlags() {
        mNumOfFlags++;
    }

    public void decreaseNumOfFlags() {
        mNumOfFlags--;
    }

    public boolean isWin() {
        return mWin;
    }

    public void setWin(boolean win) {
        mWin = win;
    }

    public boolean isLoss() {
        return mLoss;
    }

    private void setLose(boolean loss) {
        mLoss = loss;
        if (mLoss)
            mBoard.revealAll();
    }

    public int getNumOfFlags() {
        return mNumOfFlags;
    }

    public void setNumOfFlags(int numOfFlags) {
        mNumOfFlags = numOfFlags;
    }

    public String getLevel() {
        return mLevel;
    }

    public void addMines() {
        mineAdded = false;
        Boolean newSpotFound = false;
        random = new Random();
        int mineLoc = 0;
        while (!mineAdded) {

            while (!newSpotFound) {
                // Checkes for new place to place mine
                mineLoc = random.nextInt(mBoard.getNumOfTiles() - 1);

                if (mBoard.getTile(mineLoc).getValue() != Tile.TileState.MINE.val()) {
                    mBoard.getTile(mineLoc).setValue(Tile.TileState.MINE.val());
                    newSpotFound = true;
                    if (mBoard.getTile(mineLoc).isVisible())
                        mBoard.getTile(mineLoc).setVisible(false);
                }
            }

            int row = mBoard.getTile(mineLoc).getRow();
            int col = mBoard.getTile(mineLoc).getCol();
    // iterate on neighbours and inc values accordingly
            for (int i = row - 1; i <= row + 1; i++) {
                for (int j = col - 1; j <= col + 1; j++) {
                    if (((j >= 0 && j < mBoard.getBoardSize()) && (i >= 0 && i < mBoard.getBoardSize()))) {

                        int newPos = getBoard().getTileLocation(i, j);
                        if (mBoard.getTile(newPos).getValue() != Tile.TileState.MINE.val()) {
                            mBoard.getTile(newPos).setValueIfNotMine();
                            if (mBoard.getTile(newPos).isVisible()) {
                                mBoard.getTile(newPos).setVisible(false);
                                mBoard.decreaseRevealedTilesAmount();
                            }
                        }

                    }
                }
            }

            this.getBoard().setNumofmines(this.getBoard().getNumofmines() + 1);
            mineAdded = true;
        }
    }


}



