package com.arcelormittal.tableapptest.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.arcelormittal.tableapptest.room.daos.DocumentDao;
import com.arcelormittal.tableapptest.room.daos.MapDao;
import com.arcelormittal.tableapptest.room.daos.MapTileDao;
import com.arcelormittal.tableapptest.room.daos.PointDao;
import com.arcelormittal.tableapptest.room.daos.UserDao;
import com.arcelormittal.tableapptest.room.entities.Document;
import com.arcelormittal.tableapptest.room.entities.Map;
import com.arcelormittal.tableapptest.room.entities.MapTile;
import com.arcelormittal.tableapptest.room.entities.Point;
import com.arcelormittal.tableapptest.room.entities.User;

@Database(entities = {Map.class, Document.class, MapTile.class, Point.class, User.class}, version = 6)
public abstract class RoomDb extends RoomDatabase {
    public abstract MapDao mapDao();
    public abstract DocumentDao documentDao();
    public abstract MapTileDao mapTileDao();
    public abstract PointDao pointDao();
    public abstract UserDao userDao();

}
