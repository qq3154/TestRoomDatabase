package com.example.testroomdatabase.sqLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.testroomdatabase.User;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "users";

    public static final String ID_COLUMN = "person_id";
    public static final String USERNAME_COLUMN = "username";
    public static final String ADDRESS_COLUMN = "address";

    private static final String DATABASE_CREATE = String.format(
            "CREATE TABLE %s (" +
                    "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "%s TEXT, " +
                    "%s TEXT)",
            TABLE_NAME, ID_COLUMN, USERNAME_COLUMN, ADDRESS_COLUMN
    );

    private static final String DATABASE_DELETE_ALL = String.format(
            "DELETE FROM %s;",
            TABLE_NAME
    );


    private SQLiteDatabase database;

    public DatabaseHelper(Context context){
        super(context, TABLE_NAME, null, 1);
        database = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        Log.v(this.getClass().getName(), TABLE_NAME +
                "database upgrade to version" + newVersion + " - old data lost"
        );
        onCreate(db);
    }

    public long insertUser(String username, String address){
        ContentValues rowValues = new ContentValues();

        rowValues.put(USERNAME_COLUMN, username);
        rowValues.put(ADDRESS_COLUMN, address);

        return database.insertOrThrow(TABLE_NAME, null, rowValues);
    }

    public List<User> getUsers(){
        List<User> mUserList = new ArrayList<>();

        Cursor results = database.query(TABLE_NAME,
                new String[]{ID_COLUMN, USERNAME_COLUMN, ADDRESS_COLUMN},
                null, null, null, null, USERNAME_COLUMN
        );
        String resultText ="";

        results.moveToFirst();
        while(!results.isAfterLast()){
            int id = results.getInt(0);
            String username = results.getString(1);
            String address = results.getString(2);

            User user = new User(id, username, address);
            mUserList.add(user);
            //resultText += id + " " + username + " " + address + " " + "\n";

            results.moveToNext();
        }

        return  mUserList;
    }


    public Boolean checkUserExist(String username){
        List<User> mUserList = getUsers();
        for (User user : mUserList) {
            String myUsername = user.getUsername();
            if(myUsername.equals(username)){
                return true;
            }
        } 

        return false;
    }

    public void deleteAllUsers(){
        database.execSQL(DATABASE_DELETE_ALL);
    }

    public void updateUser(User user){


        String DATABASE_UPDATE = String.format("UPDATE %s SET %s = '%s', %s = '%s' WHERE %s = %s;",
                TABLE_NAME, USERNAME_COLUMN, user.getUsername(), ADDRESS_COLUMN, user.getAddress(), ID_COLUMN, user.getId());

        //String a = "UPDATE users SET username = a, address = b";
        //database.execSQL(a);
        database.execSQL(DATABASE_UPDATE);
    }

    public void deleteUser(User user){
        String DATABASE_DELETE = String.format("DELETE FROM %s  WHERE %s = %s;",
                TABLE_NAME, ID_COLUMN, user.getId());
        database.execSQL(DATABASE_DELETE);
    }
}
