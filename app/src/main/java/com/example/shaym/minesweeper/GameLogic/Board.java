package com.example.shaym.minesweeper.GameLogic;

import com.example.shaym.minesweeper.Constant;

import java.util.ArrayList;

public class Board {

    private String mLvl;
    private int mBoardSize;
    private int mTilesAmount;
    private static int mNumofmines;
    private int mRevealedTilesAmount = 0;
    private Tile[][] mBoard;

    public Board(String lvl) {
        mLvl = lvl;
        initBoard();
    }

    public static int getmNumofmines() {
        return mNumofmines;
    }

    public static void setNumofmines(final int numofmines) {
        mNumofmines = numofmines;
    }

    // Inits the board with the current level, adjust board size and mines amount
    private void initBoard() {
        switch (mLvl) {
            case Constant.EASY_LVL:
                mBoardSize = Constant.EASY_LEVEL_BOARD_SIZE;
                mNumofmines = Constant.EASY_LEVEL_MINES_AMOUNT;
                break;
            case Constant.MEDUIM_LVL:
                mBoardSize = Constant.MEDUIM_LEVEL_BOARD_SIZE;
                mNumofmines = Constant.MEDUIM_LEVEL_MINE_SIZE;
                break;
            case Constant.HARD_LVL:
                mBoardSize = Constant.HARD_LEVEL_BOARD_SIZE;
                mNumofmines = Constant.HARD_LEVEL_MINE_SIZE;
                break;
        }

        mTilesAmount = mBoardSize * mBoardSize;
        mBoard = new Tile[mBoardSize][mBoardSize];

        for (int i = 0; i < mBoardSize; i++)
            for (int j = 0; j < mBoardSize; j++)
                mBoard[i][j] = new Tile(i, j);

        initMines();
        initCellValues();
    }

    // plants the mines in random locations
    private void initMines() {
        int rand;
        ArrayList<Integer> location = new ArrayList<Integer>();

        while (location.size() < mNumofmines) {
            rand = (int) (Math.random() * (mTilesAmount));

            if (!location.contains(rand))
                location.add(rand);
        }

        for (int j : location)
            getTile(j).setValue(Tile.TileState.MINE.val());
    }

    public int getNumofmines() {
        return mNumofmines;
    }

    // initialize the rest of the cells in the board (those who are not mines)
    private void initCellValues() {
        for (int i = 0; i < mBoardSize; i++) {
            for (int j = 0; j < mBoardSize; j++) {
                if (mBoard[i][j].getValue() == Tile.TileState.MINE.val()) {

                    if (i == 0) {
                        mBoard[i + 1][j].setValueIfNotMine();
                        if (j == 0) {
                            mBoard[i][j + 1].setValueIfNotMine();
                            mBoard[i + 1][j + 1].setValueIfNotMine();


                        } else if (j == mBoardSize - 1) {
                            mBoard[i][j - 1].setValueIfNotMine();
                            mBoard[i + 1][j - 1].setValueIfNotMine();


                        } else {
                            mBoard[i][j + 1].setValueIfNotMine();
                            mBoard[i][j - 1].setValueIfNotMine();
                            mBoard[i + 1][j + 1].setValueIfNotMine();
                            mBoard[i + 1][j - 1].setValueIfNotMine();


                        }
                    } else if (i == mBoardSize - 1) {
                        mBoard[i - 1][j].setValueIfNotMine();

                        if (j == 0) {
                            mBoard[i][j + 1].setValueIfNotMine();
                            mBoard[i - 1][j + 1].setValueIfNotMine();


                        } else if (j == mBoardSize - 1) {
                            mBoard[i][j - 1].setValueIfNotMine();
                            mBoard[i - 1][j - 1].setValueIfNotMine();

                        } else {
                            mBoard[i][j + 1].setValueIfNotMine();
                            mBoard[i][j - 1].setValueIfNotMine();
                            mBoard[i - 1][j - 1].setValueIfNotMine();
                            mBoard[i - 1][j + 1].setValueIfNotMine();

                        }
                    } else if (j == 0) {
                        mBoard[i][j + 1].setValueIfNotMine();
                        mBoard[i + 1][j + 1].setValueIfNotMine();
                        mBoard[i + 1][j].setValueIfNotMine();
                        mBoard[i - 1][j].setValueIfNotMine();
                        mBoard[i - 1][j + 1].setValueIfNotMine();


                    } else if (j == mBoardSize - 1) {
                        mBoard[i][j - 1].setValueIfNotMine();
                        mBoard[i + 1][j - 1].setValueIfNotMine();
                        mBoard[i + 1][j].setValueIfNotMine();
                        mBoard[i - 1][j - 1].setValueIfNotMine();
                        mBoard[i - 1][j].setValueIfNotMine();


                    } else {
                        mBoard[i][j + 1].setValueIfNotMine();
                        mBoard[i][j - 1].setValueIfNotMine();
                        mBoard[i + 1][j + 1].setValueIfNotMine();
                        mBoard[i + 1][j - 1].setValueIfNotMine();
                        mBoard[i + 1][j].setValueIfNotMine();
                        mBoard[i - 1][j - 1].setValueIfNotMine();
                        mBoard[i - 1][j].setValueIfNotMine();
                        mBoard[i - 1][j + 1].setValueIfNotMine();


                    }
                }

            }
        }
    }

    public Tile getTile(int loc) {
        int rowIndex, colIndex;

        rowIndex = loc / mBoardSize;
        colIndex = loc % mBoardSize;

        return mBoard[rowIndex][colIndex];
    }

    public int getTileLocation(int row, int col) {
        return (row * mBoardSize) + col;
    }

    public void revealAll() {
        for (int i = 0; i < mBoardSize; i++) {
            for (int j = 0; j < mBoardSize; j++) {

                int loc = getTileLocation(i, j);
                Tile tile = getTile(loc);

                if (tile.getValue() == Tile.TileState.MINE.val())
                    tile.setVisible(true);
                else
                    tile.reveal();
            }
        }
    }

    public int getRevealedTilesAmount() {
        return mRevealedTilesAmount;
    }

    public void increaseRevealedTilesAmount() {
        mRevealedTilesAmount++;
    }

    public void decreaseRevealedTilesAmount() {
        mRevealedTilesAmount--;
    }

    public int getNumOfTiles() {
        return mTilesAmount;
    }

    public int getBoardSize() {
        return mBoardSize;
    }
}