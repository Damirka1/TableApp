package com.arcelormittal.tableapptest.room.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.arcelormittal.tableapptest.room.entities.Document;

import java.util.List;

@Dao
public interface DocumentDao {
    @Query("SELECT * FROM Document WHERE mapId = :shaftId AND name = :name")
    Document findOneByShaftId(long shaftId, String name);

    @Query("SELECT * FROM Document WHERE mapId = :shaftId")
    List<Document> findByShaftId(long shaftId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Document... documents);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Document> documents);

    @Delete
    void delete(Document document);

    @Query("DELETE FROM Document")
    void removeAll();
}
