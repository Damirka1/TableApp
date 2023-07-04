package com.arcelormittal.tableapptest.room.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.arcelormittal.tableapptest.dtos.MapDto;

import java.time.LocalDateTime;

@Entity
public class Map {
    @PrimaryKey
    private long id;
    private String name;
//    private LocalDateTime created;

    public Map() {
    }

    public Map(MapDto mapDto) {
        this.id = mapDto.id;
        this.name = mapDto.name;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
