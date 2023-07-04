package com.arcelormittal.tableapptest.room.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.arcelormittal.tableapptest.room.entities.MapTile;

import java.util.List;

@Dao
public interface MapTileDao {

    @Query("SELECT * FROM MapTile")
    List<MapTile> findAll();

    @Query("SELECT * FROM MapTile WHERE mapId = :shaftId AND `index` = :index")
    MapTile findOneByShaftIdAndIndex(long shaftId, int index);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(MapTile... tiles);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<MapTile> tiles);

    @Delete
    void delete(MapTile tile);

    @Query("DELETE FROM MapTile")
    void removeAll();
}
