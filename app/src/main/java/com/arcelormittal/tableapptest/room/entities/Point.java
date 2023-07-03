package com.arcelormittal.tableapptest.room.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Point {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private long mapId;

    private int x;
    private int y;
    private int r;

    private String text;

    public Point(int x, int y, int r, String text, long mapId) {
        this.x = x;
        this.y = y;
        this.r = r;

        this.text = text;
        this.mapId = mapId;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getR() {
        return r;
    }

    public String getText() {
        return text;
    }

    public long getMapId() {
        return mapId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMapId(long mapId) {
        this.mapId = mapId;
    }
}
