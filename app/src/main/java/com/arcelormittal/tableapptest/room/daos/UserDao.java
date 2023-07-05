package com.arcelormittal.tableapptest.room.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.arcelormittal.tableapptest.room.entities.User;

@Dao
public interface UserDao {

    @Query("SELECT * FROM User")
    User getUser();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User user);

    @Delete
    void delete(User user);

    @Query("DELETE FROM User")
    void removeAll();
}

