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

    @Query("SELECT * FROM Point WHERE mapId = :shaftId")
    List<Point> findAllByShaftId(long shaftId);

    @Query("DELETE FROM Point WHERE mapId = :shaftId")
    void deleteAllByShaftId(long shaftId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Point... points);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Point> points);

    @Delete
    void delete(Point point);
}
