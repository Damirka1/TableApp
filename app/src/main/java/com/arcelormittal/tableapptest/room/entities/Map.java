package com.arcelormittal.tableapptest.room.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.arcelormittal.tableapptest.dtos.MapDto;
import com.arcelormittal.tableapptest.room.convertors.TimestampConverter;

import java.time.Instant;

@Entity
public class Map {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;
    @TypeConverters({TimestampConverter.class})
    private Instant created;

    public Map() {
    }

    public Map(MapDto mapDto) {
        this.name = mapDto.name;
        this.created = mapDto.created;
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

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }
}
