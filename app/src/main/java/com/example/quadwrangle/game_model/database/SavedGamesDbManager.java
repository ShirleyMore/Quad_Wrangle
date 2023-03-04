package com.example.quadwrangle.game_model.database;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SavedGamesDbManager extends SQLiteOpenHelper {

    public static String TABLE_PRODUCT = "SavedGame";

    // column names
    public static final String COLUMN_GAME_ID = "Game_Id";
    public static final String COLUMN_USER_ID = "User_Id";
    public static final String COLUMN_SAVE_NAME = "Save_Name";
    public static final String COLUMN_SAVE_DATE = "Date_Saved";
    public static final String COLUMN_BOARD = "Board";
    public static final String COLUMN_NEXT_PLAYER = "Next_Player";
    public static final String COLUMN_GAME_TYPE = "Game_Type";

    // table name -> represents a class of students
    public static final String TableName = "SavedGames";

    public static final String CREATE_TABLE="CREATE TABLE IF NOT EXISTS " + TABLE_PRODUCT + "(" + COLUMN_GAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_USER_ID + " INTEGER," + COLUMN_SAVE_NAME + " VARCHAR," + COLUMN_SAVE_DATE + " VARCHAR," + COLUMN_BOARD + " VARCHAR," + COLUMN_NEXT_PLAYER +
            " VARCHAR," + COLUMN_GAME_TYPE + " VARCHAR" + ");";

    String[] allColumns = {SavedGamesDbManager.COLUMN_GAME_ID, SavedGamesDbManager.COLUMN_USER_ID
            , SavedGamesDbManager.COLUMN_SAVE_NAME, SavedGamesDbManager.COLUMN_SAVE_DATE, SavedGamesDbManager.COLUMN_BOARD
            , SavedGamesDbManager.COLUMN_NEXT_PLAYER, SavedGamesDbManager.COLUMN_GAME_TYPE};

    public SavedGamesDbManager(Context context) {
        super(context, TableName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // sqLiteDatabase.execSQL: Execute a single SQL statement that is NOT a SELECT or any other SQL statement that returns data.
        sqLiteDatabase.execSQL(CREATE_TABLE);
        System.out.println("SAVED GAMES DATABASE onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        System.out.println("SAVED GAMES DATABASE onUpgrade");
    }

    public SQLiteDatabase open() {
        System.out.println("SAVED GAMES DATABASE open");
        return this.getWritableDatabase();
    }

    public void closeDatabase() {
        close();
        System.out.println("SAVED GAMES DATABASE closed");
    }

    public SavedGame createSavedGame(SavedGame game) {
        SQLiteDatabase database = open();
        ContentValues values = new ContentValues();
        // Notice - we should NOT set AutoInc fields!!
        // values.put(SavedGamesDbManager.COLUMN_GAME_ID, game.getGameId());
        values.put(SavedGamesDbManager.COLUMN_USER_ID, game.getUserId());
        values.put(SavedGamesDbManager.COLUMN_SAVE_NAME, game.getSaveName());
        values.put(SavedGamesDbManager.COLUMN_SAVE_DATE, game.getDateSaved());
        values.put(SavedGamesDbManager.COLUMN_BOARD, game.getBoard());
        values.put(SavedGamesDbManager.COLUMN_NEXT_PLAYER, game.getNextPlayer());
        values.put(SavedGamesDbManager.COLUMN_GAME_TYPE, game.getGameType());
        long insertId = database.insert(SavedGamesDbManager.TABLE_PRODUCT, null, values);
        game.setId(insertId);
        System.out.println("DATABASE game inserted, id = " + insertId);
        return game;
    }

    public ArrayList<SavedGame> getAllGamesForUser(long userId) {
        SQLiteDatabase database = open();
        ArrayList<SavedGame> arr = new ArrayList<>();
        Cursor cursor = database.query(SavedGamesDbManager.TABLE_PRODUCT, allColumns, null, null, null ,null, null);
        System.out.println("Count: " + cursor.getCount());
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) { // moveToNext starts before the first row
                long user_id = cursor.getLong(cursor.getColumnIndexOrThrow(SavedGamesDbManager.COLUMN_USER_ID));
                if (user_id == userId) {// if its the right user's game
                    long game_id = cursor.getLong(cursor.getColumnIndexOrThrow(SavedGamesDbManager.COLUMN_GAME_ID));
                    String save_name = cursor.getString(cursor.getColumnIndexOrThrow(SavedGamesDbManager.COLUMN_SAVE_NAME));
                    String saved_date = cursor.getString(cursor.getColumnIndexOrThrow(SavedGamesDbManager.COLUMN_SAVE_DATE));
                    String board = cursor.getString(cursor.getColumnIndexOrThrow(SavedGamesDbManager.COLUMN_BOARD));
                    String next_player = cursor.getString(cursor.getColumnIndexOrThrow(SavedGamesDbManager.COLUMN_NEXT_PLAYER));
                    String game_type = cursor.getString(cursor.getColumnIndexOrThrow(SavedGamesDbManager.COLUMN_GAME_TYPE));
                    SavedGame game = new SavedGame(game_id, user_id, save_name, saved_date, board, next_player, game_type);
                    //System.out.println(user);
                    arr.add(game);
                }
            }
        }
        closeDatabase();
        return arr;
    }

    //table: the name of the table you want to query
    //columns: the column names that you want returned. Don't return data that you don't need.
    //selection: the row data that you want returned from the columns (This is the WHERE clause.)
    //selectionArgs: This is substituted for the ? in the selection String above.
    //groupBy and having: This groups duplicate data in a column with data having certain conditions. Any unneeded parameters can be set to null.
    //orderBy: sort the data
    //limit: limit the number of results to return
    public SavedGame getGameById(long gameId) {
        SQLiteDatabase database = open();
        Cursor cursor = database.query(SavedGamesDbManager.TABLE_PRODUCT, allColumns, COLUMN_GAME_ID + " =?", new String[]{gameId + ""}, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToNext(); // move to 1
            long user_id = cursor.getLong(cursor.getColumnIndexOrThrow(SavedGamesDbManager.COLUMN_USER_ID));
            String save_name = cursor.getString(cursor.getColumnIndexOrThrow(SavedGamesDbManager.COLUMN_SAVE_NAME));
            String saved_date = cursor.getString(cursor.getColumnIndexOrThrow(SavedGamesDbManager.COLUMN_SAVE_DATE));
            String board = cursor.getString(cursor.getColumnIndexOrThrow(SavedGamesDbManager.COLUMN_BOARD));
            String next_player = cursor.getString(cursor.getColumnIndexOrThrow(SavedGamesDbManager.COLUMN_NEXT_PLAYER));
            String game_type = cursor.getString(cursor.getColumnIndexOrThrow(SavedGamesDbManager.COLUMN_GAME_TYPE));
            return new SavedGame(gameId, user_id, save_name, saved_date, board, next_player, game_type);
        }
        return null;
    }
}
