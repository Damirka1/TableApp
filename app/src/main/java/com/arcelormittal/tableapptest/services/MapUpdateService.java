package com.arcelormittal.tableapptest.services;

import android.content.res.AssetManager;

import com.arcelormittal.tableapptest.dtos.MapDto;
import com.arcelormittal.tableapptest.dtos.PointDto;
import com.arcelormittal.tableapptest.room.entities.Map;
import com.arcelormittal.tableapptest.room.entities.Point;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
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
        JdbcService jdbcService = new JdbcService();
        List<MapDto> shafts = jdbcService.listAllShafts();

        for(MapDto shaft : shafts) {
            LiteDirectory ld = LiteDirectory.getInstance();

            PointDto pointDto = jdbcService.findPointsByMap(shaft.id);
            List<Point> points = getPoints(pointDto);

            try {
                ld.saveShaft(new Map(shaft));
                ld.removePointsByShaft(shaft.id);
                ld.savePoints(points);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }

        downloading = false;
    }

    public MapUpdateService(MapListService mapListService) {
        this.mapListService = mapListService;

        if(mapListService.getMapList().size() >= 0) {
            downloading = true;
            new Thread(this::downloadAllMaps).start();
        }
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
