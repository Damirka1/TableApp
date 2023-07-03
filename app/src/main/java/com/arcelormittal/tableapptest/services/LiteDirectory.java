package com.arcelormittal.tableapptest.services;


import com.arcelormittal.tableapptest.room.RoomDb;
import com.arcelormittal.tableapptest.room.entities.Document;
import com.arcelormittal.tableapptest.room.entities.Map;
import com.arcelormittal.tableapptest.room.entities.Point;

import java.util.List;

public class LiteDirectory {
    private static LiteDirectory INSTANCE;
    private final RoomDb roomDb;

    public static void createInstance(RoomDb roomDb) {
        INSTANCE = new LiteDirectory(roomDb);
    }

    public static LiteDirectory getInstance() {
        return INSTANCE;
    }

    public LiteDirectory(RoomDb roomDb) {
        this.roomDb = roomDb;
    }

    public void saveShaft(Map map) {
        roomDb.mapDao().insertAll(map);
    }

    public List<Map> getShafts() {
        return roomDb.mapDao().findAll();
    }

    public List<Point> getPointsByShaft(long shaftId) {
        return roomDb.pointDao().findAllByShaftId(shaftId);
    }

    public void removePointsByShaft(long shaftId) {
        roomDb.pointDao().deleteAllByShaftId(shaftId);
    }

    public void savePoints(List<Point> points) {
        roomDb.pointDao().insertAll(points);
    }

    public void saveDocuments(List<Document> documents) {
        roomDb.documentDao().insertAll(documents);
    }

    public Document getDocumentByShaftIdAndName(long shaftId, String name) {
        return roomDb.documentDao().findOneByShaftId(shaftId, name);
    }



}
