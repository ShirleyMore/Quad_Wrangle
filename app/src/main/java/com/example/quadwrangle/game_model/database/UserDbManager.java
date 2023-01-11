package com.example.quadwrangle.game_model.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class UserDbManager extends SQLiteOpenHelper {

    public static String TABLE_PRODUCT = "User";

    // column names
    public static final String COLUMN_ID = "Id";
    public static final String COLUMN_NAME = "Name";
    public static final String COLUMN_PASSWORD = "Password";
    public static final String COLUMN_HIGH_SCORE = "HighScore";

    // table name -> represents a class of students
    public static final String TableName = "Users";

    public static final String CREATE_TABLE="CREATE TABLE IF NOT EXISTS " + TABLE_PRODUCT + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_NAME + " VARCHAR," + COLUMN_PASSWORD + " VARCHAR," + COLUMN_HIGH_SCORE + " INTEGER" + ");";

    String[] allColumns = {UserDbManager.COLUMN_ID, UserDbManager.COLUMN_NAME, UserDbManager.COLUMN_PASSWORD, UserDbManager.COLUMN_HIGH_SCORE};

    public UserDbManager(Context context) {
        super(context, TableName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
        System.out.println("DATABASE onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT);
        //onCreate(open());
        System.out.println("DATABASE onUpgrade");
    }

    public void updateScore(int newScore, User user) {
        SQLiteDatabase database = open();
        ContentValues values = new ContentValues();
        values.put(UserDbManager.COLUMN_HIGH_SCORE, newScore);
        database.update(UserDbManager.TABLE_PRODUCT, values, "id=" + user.getId(), null);
        System.out.println("DATABASE updateScore USER " + user.getId());
        database.close();
    }

    public SQLiteDatabase open() {
        System.out.println("DATABASE open");
        return this.getWritableDatabase();
    }

    public void closeDatabase() {
        close();
        System.out.println("DATABASE closed");
    }

    // returns null if a user with the same name exists
    public User createUser(User user) {
        SQLiteDatabase database = open();
        // if a user already has this name -> null
        if (doesExistWithTheSameName(user.getName())) {
            return null;
        }
        ContentValues values = new ContentValues();
        values.put(UserDbManager.COLUMN_NAME, user.getName());
        values.put(UserDbManager.COLUMN_PASSWORD, user.getPassword());
        values.put(UserDbManager.COLUMN_HIGH_SCORE, user.getHighScore());
        long insertId = database.insert(UserDbManager.TABLE_PRODUCT, null, values);
        user.setId(insertId);
        System.out.println("DATABASE user inserted, id = " + insertId);
        return user;
    }

    public ArrayList<User> getAllUsers() {
        SQLiteDatabase database = open();
        ArrayList<User> arr = new ArrayList<>();
        Cursor cursor = database.query(UserDbManager.TABLE_PRODUCT, allColumns, null, null, null ,null, null);
        System.out.println("Count: " + cursor.getCount());
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) { // moveToNext starts before the first row
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(UserDbManager.COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(UserDbManager.COLUMN_NAME));
                String password = cursor.getString(cursor.getColumnIndexOrThrow(UserDbManager.COLUMN_PASSWORD));
                int high_score = cursor.getInt(cursor.getColumnIndexOrThrow(UserDbManager.COLUMN_HIGH_SCORE));
                User user = new User(id, name, password, high_score);
                //System.out.println(user);
                arr.add(user);
            }
        }
        closeDatabase();
        return arr;
    }

    public boolean doesExistWithTheSameName(String name) {
        SQLiteDatabase database = open();
        Cursor cursor = database.query(UserDbManager.TABLE_PRODUCT, allColumns, null, null, null, null, null);
        while (cursor.moveToNext()) { // moveToNext starts before the first row
            if (cursor.getString(cursor.getColumnIndexOrThrow(UserDbManager.COLUMN_NAME)).equals(name)) {
                return true;
            }
        }
        return false;
    }

    public User logIn(String username, String password) {
        SQLiteDatabase database = open();
        User myUser = null;
        Cursor cursor = database.query(UserDbManager.TABLE_PRODUCT, allColumns, null, null, null, null, null);
        while (cursor.moveToNext()) { // moveToNext starts before the first row
            if (cursor.getString(cursor.getColumnIndexOrThrow(UserDbManager.COLUMN_PASSWORD))
                    .equals(password) && cursor.getString(cursor.getColumnIndexOrThrow(UserDbManager.COLUMN_NAME)).equals(username)) {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(UserDbManager.COLUMN_NAME));
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(UserDbManager.COLUMN_ID));
                int high_score = cursor.getInt(cursor.getColumnIndexOrThrow(UserDbManager.COLUMN_HIGH_SCORE));
                User user = new User(id, name, password, high_score);
                //System.out.println(user);
                myUser = user;
                break;
            }
        }
        return myUser;

    }
}
