package com.arcelormittal.tableapptest.room.entities;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.arcelormittal.tableapptest.dtos.DocumentDto;

@Entity
public class Document {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private long mapId;

    private String name;

    private byte[] file;

    public Document() {

    }

    public Document(DocumentDto documentDto) {
        this.id = documentDto.id;
        this.mapId = documentDto.shaftId;
        this.name = documentDto.name;
        this.file = documentDto.data;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getMapId() {
        return mapId;
    }

    public void setMapId(long mapId) {
        this.mapId = mapId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }
}
