package com.arcelormittal.tableapptest.room.entities;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Document {

    @PrimaryKey
    private int id;

    private long mapId;

    private String name;

    private byte[] file;

    public int getId() {
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
