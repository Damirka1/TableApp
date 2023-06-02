package com.arcelormittal.tableapptest.entities;

public class Point {

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
}
