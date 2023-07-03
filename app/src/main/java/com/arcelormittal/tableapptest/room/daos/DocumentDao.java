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
    @Query("SELECT * FROM Document")
    List<Document> findAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Document... documents);

    @Delete
    void delete(Document document);
}
