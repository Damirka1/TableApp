package com.arcelormittal.tableapptest.room.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.arcelormittal.tableapptest.room.entities.Point;

import java.util.List;

@Dao
public interface PointDao {

    @Query("SELECT * FROM Point")
    List<Point> findAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Point... points);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Point> points);

    @Delete
    void delete(Point point);
}
