package com.arcelormittal.tableapptest.room.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private boolean firstStartup = true;

    private String code;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isFirstStartup() {
        return firstStartup;
    }

    public void setFirstStartup(boolean firstStartup) {
        this.firstStartup = firstStartup;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
