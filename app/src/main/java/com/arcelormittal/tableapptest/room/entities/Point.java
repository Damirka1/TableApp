package com.arcelormittal.tableapptest.room.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Point {

    @PrimaryKey
    private int id;

    private long mapId;

    private int x;
    private int y;
    private int r;

    private String text;

    public Point(int x, int y, int r, String text) {
        this.x = x;
        this.y = y;
        this.r = r;

        this.text = text;
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
