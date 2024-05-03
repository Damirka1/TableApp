package com.arcelormittal.tableapptest.services;

import com.arcelormittal.tableapptest.dtos.MapDto;
import com.arcelormittal.tableapptest.dtos.PointDto;
import com.arcelormittal.tableapptest.room.entities.Document;
import com.arcelormittal.tableapptest.room.entities.Map;
import com.arcelormittal.tableapptest.room.entities.MapTile;
import com.arcelormittal.tableapptest.room.entities.Point;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class MapUpdateService {

    private MapListService mapListService;
    private List<String> mapsToUpdate = new LinkedList<>();

    private JdbcService jdbcService;

    private boolean downloading = false;
    private int progress = 0;

    private List<Point> getPoints(PointDto pointDto) {
        List<Point> pointList = null;
        try {
            pointList = new LinkedList<>();
            BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(pointDto.data)));
            String line = reader.readLine();

            while (line != null) {
                String[] res = line.split(" ");

                Point point = new Point(Integer.parseInt(res[0]), Integer.parseInt(res[1]), Integer.parseInt(res[2]), res[3].toLowerCase(), pointDto.shaftId);
                pointList.add(point);

                // read next line
                line = reader.readLine();
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        pointList.sort(Comparator.comparing(Point::getText));
        return pointList;
    }
    private void downloadMap() {
        String name = UserService.getInstance().getCode();

        if(Objects.isNull(name))
            return;

        downloading = true;
        progress = 0;

        MapDto shaft = jdbcService.findShaftByName(name);
        LiteDirectory ld = LiteDirectory.getInstance();

        // TODO: Optimize this
        try {
            Map map;
            // Saving shaft
            {
                map = ld.saveShaft(shaft);
                progress += 10;
                ld.removePointsByShaft(map.getId());
                progress += 10;
            }

            // Saving points of shaft
            Thread t1 = new Thread(() -> {
                PointDto pointDto = jdbcService.findPointsByMap(map, shaft.id);
                progress += 10;
                List<Point> points = getPoints(pointDto);
                ld.savePoints(points);
                progress += 10;
            });

            t1.start();

            // Saving map tiles of shaft
            Thread t2 = new Thread(() -> {
                List<MapTile> mapTiles = jdbcService.findMapTilesByMap(map, shaft.id);
                progress += 10;
                ld.saveMapTiles(mapTiles);
                progress += 10;
            });
            t2.start();

            // Saving documents of shaft
            Thread t3 = new Thread(() -> {
                List<Document> documents = jdbcService.findDocumentsByMap(map, shaft.id);
                progress += 30;
                ld.saveDocuments(documents);
                progress += 10;
            });
            t3.start();

            t3.join();
            t2.join();
            t1.join();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        downloading = false;
        progress = 0;
    }

    private void clearAllDb() {
        LiteDirectory ld = LiteDirectory.getInstance();
        ld.forceClearAll();
    }

    public boolean needWait() {
        return Objects.isNull(jdbcService);
    }

    public boolean hasConnection() {
        return jdbcService.isConnected();
    }

    public boolean checkUpdate() {
        LiteDirectory ld = LiteDirectory.getInstance();
        List<Map> maps = ld.getShafts();

        if(maps.isEmpty())
            return true;

        String name = UserService.getInstance().getCode();

        if(Objects.isNull(name))
            return false;

        try {
            MapDto shaft = jdbcService.findShaftByName(name);

            return ld.needUpdate(shaft);
        } catch (Exception e) {
            return false;
        }
    }

    public MapUpdateService() {
        new Thread(() -> jdbcService = new JdbcService()).start();
    }

    public void forceClear() {
        new Thread(this::clearAllDb).start();
    }

    public void forceDownload() {
        downloading = true;
        new Thread(this::downloadMap).start();
    }

    public boolean isDownloading() {
        return downloading;
    }

    public int getProgress() { return progress; }

    public void updateMaps(List<String> maps) {
        mapsToUpdate = maps;
    }

    public List<String> getMapsToUpdate() {
        return mapsToUpdate;
    }
}
