package com.arcelormittal.tableapptest.room.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.arcelormittal.tableapptest.room.entities.Map;

import java.util.List;

@Dao
public interface MapDao {

    @Query("SELECT * FROM Map")
    List<Map> findAll();

    @Query("SELECT * FROM Map WHERE name = :name")
    Map findOne(String name);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Map... maps);

    @Delete
    void delete(Map map);

    @Query("DELETE FROM Map")
    void removeAll();
}
