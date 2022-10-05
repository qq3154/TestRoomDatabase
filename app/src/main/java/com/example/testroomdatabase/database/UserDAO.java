package com.example.testroomdatabase.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.testroomdatabase.User;

import java.util.List;

//@Dao
public interface UserDAO {

    //@Insert
    void insertUser(User user);

    //@Query("SELECT * FROM user")
    List<User> getListUser();

    //Query("SELECT * FROM USER WHERE username = :username" )
    List<User> checkUser(String username);

    //@Update
    void updateUser(User user);
}
