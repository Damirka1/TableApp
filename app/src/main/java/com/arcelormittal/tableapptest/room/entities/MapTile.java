package com.arcelormittal.tableapptest.room.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class MapTile {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private long mapId;

    private int index;

    private byte[] file;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getMapId() {
        return mapId;
    }

    public void setMapId(long mapId) {
        this.mapId = mapId;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }
}
