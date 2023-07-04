package com.arcelormittal.tableapptest.services;

import com.arcelormittal.tableapptest.dtos.DocumentDto;
import com.arcelormittal.tableapptest.dtos.MapDto;
import com.arcelormittal.tableapptest.dtos.PointDto;
import com.arcelormittal.tableapptest.room.entities.Document;
import com.arcelormittal.tableapptest.room.entities.Map;
import com.arcelormittal.tableapptest.room.entities.Point;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

public class MapUpdateService {

    private MapListService mapListService;
    private List<String> mapsToUpdate = new LinkedList<>();

    private boolean downloading = false;

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
        return pointList;
    }
    private void downloadAllMaps() {
        downloading = true;

        JdbcService jdbcService = new JdbcService();
        List<MapDto> shafts = jdbcService.listAllShafts();

        for(MapDto shaft : shafts) {
            LiteDirectory ld = LiteDirectory.getInstance();
            try {

                // Saving shaft
                {
                    ld.saveShaft(new Map(shaft));
                    ld.removePointsByShaft(shaft.id);
                }

                // Saving points of shaft
                {
                    PointDto pointDto = jdbcService.findPointsByMap(shaft.id);
                    List<Point> points = getPoints(pointDto);
                    ld.savePoints(points);
                }

                // Saving documents of shaft
                {
                    List<Document> documents = jdbcService.findDocumentsByMap(shaft.id);
                    ld.saveDocuments(documents);
                }

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }

        downloading = false;
    }

    private void clearAllDb() {
        LiteDirectory ld = LiteDirectory.getInstance();
        ld.forceClearAll();
    }

    private void checkUpdate() {
        List<Map> maps = LiteDirectory.getInstance().getShafts();

        if(maps.size() == 0) {
            new Thread(this::downloadAllMaps).start();
        }
    }

    public MapUpdateService(MapListService mapListService) {
        this.mapListService = mapListService;

        new Thread(this::checkUpdate).start();
    }

    public void forceClear() {
        new Thread(this::clearAllDb).start();
    }

    public void forceDownload() {
        new Thread(this::downloadAllMaps).start();
    }

    public boolean isDownloading() {
        return downloading;
    }

    public void updateMaps(List<String> maps) {
        mapsToUpdate = maps;
    }

    public List<String> getMapsToUpdate() {
        return mapsToUpdate;
    }
}
