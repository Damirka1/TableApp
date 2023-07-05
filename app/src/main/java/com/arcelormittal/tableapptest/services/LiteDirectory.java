package com.arcelormittal.tableapptest.services;


import com.arcelormittal.tableapptest.dtos.MapDto;
import com.arcelormittal.tableapptest.room.RoomDb;
import com.arcelormittal.tableapptest.room.entities.Document;
import com.arcelormittal.tableapptest.room.entities.Map;
import com.arcelormittal.tableapptest.room.entities.MapTile;
import com.arcelormittal.tableapptest.room.entities.Point;

import java.util.List;
import java.util.Objects;

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

    public boolean needUpdate(MapDto mapDto) {
        Map map = roomDb.mapDao().findOne(mapDto.name);

        if(Objects.isNull(map))
            return true;

        return map.getCreated().before(mapDto.created);
    }

    public Map saveShaft(MapDto mapDto) {
        Map map = roomDb.mapDao().findOne(mapDto.name);

        if(Objects.isNull(map))
            map = new Map(mapDto);
        else
            map.setCreated(mapDto.created);

        roomDb.mapDao().insertAll(map);
        map = roomDb.mapDao().findOne(mapDto.name);

        return map;
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

    public void saveMapTiles(List<MapTile> mapTiles) {
        roomDb.mapTileDao().insertAll(mapTiles);
    }

    public Document getDocumentByShaftIdAndName(long shaftId, String name) {
        return roomDb.documentDao().findOneByShaftId(shaftId, name);
    }

    public MapTile getMapTileByShaftIdAndIndex(long shaftId, int index) {
        return roomDb.mapTileDao().findOneByShaftIdAndIndex(shaftId, index);
    }

    public List<Document> getDocumentsByShaftId(long shaftId) {
        return roomDb.documentDao().findByShaftId(shaftId);
    }

    public void forceClearAll() {
        roomDb.documentDao().removeAll();
        roomDb.mapDao().removeAll();
        roomDb.pointDao().removeAll();
        roomDb.mapTileDao().removeAll();
    }

}
