package com.arcelormittal.tableapptest.services;

import com.arcelormittal.tableapptest.dtos.DocumentDto;
import com.arcelormittal.tableapptest.dtos.MapDto;
import com.arcelormittal.tableapptest.dtos.PointDto;
import com.arcelormittal.tableapptest.room.entities.Document;
import com.arcelormittal.tableapptest.room.entities.Map;
import com.arcelormittal.tableapptest.room.entities.MapTile;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;

import kotlin.NotImplementedError;

public class JdbcService {

    private Connection connection;

    public JdbcService() {
        try {
//            connection = DriverManager.getConnection("jdbc:postgresql://home.damirka.space:5431/pla", "postgres", "SUPERHELLOWORDL123@");
            connection = DriverManager.getConnection("jdbc:postgresql://130.61.79.90:5432/pla", "root", "EasingObliviousGarbageEntwineRemissionReactorPending");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public List<MapDto> listAllShafts() {
        Statement st = null;
        try {
            st = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String sql = "select max(id), name, max(created) as created from shaft s group by name";

        List<MapDto> maps = new LinkedList<>();

        try {
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()) {
                MapDto map = new MapDto();
                map.id = rs.getLong(1);
                map.name = rs.getString(2);
                map.created = rs.getTimestamp(3).toInstant();

                maps.add(map);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return maps;
    }

    public MapDto findShaftByName(String name) {
        Statement st = null;
        try {
            st = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String sql = "select max(id), name, max(created) as created from shaft " + "where name = " + '\'' + name + '\''  + " group by name";

        MapDto map = new MapDto();

        try {
            ResultSet rs = st.executeQuery(sql);
            if(rs.next()) {
                map.id = rs.getLong(1);
                map.name = rs.getString(2);
                map.created = rs.getTimestamp(3).toInstant();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return map;
    }

    public PointDto findPointsByMap(Map map, long shaftId) {
        Statement st = null;
        try {
            st = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String sql = "SELECT data FROM point WHERE shaft_id = " + shaftId;

        PointDto pointDto = new PointDto();

        try {
            ResultSet rs = st.executeQuery(sql);
            if(rs.next()) {
                pointDto.data = rs.getBytes(1);
                pointDto.shaftId = map.getId();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return pointDto;
    }

    private int countDocumentsByMap(long shaftId) {
        throw new NotImplementedError();
    }

    public List<Document> findDocumentsByMap(Map map, long shaftId) {
        Statement st = null;
        try {
            st = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // TODO: make downloading with paging and threads

//        int count = countDocumentsByMap(shaftId);

        String sql = "SELECT name, doc_file FROM doc WHERE shaft_id = " + shaftId; // + "OFFSET 0 FETCH NEXT 5 ROWS ONLY";

        List<Document> documentDtos = new LinkedList<>();

        try {
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()) {
                Document document = new Document();
                document.setMapId(map.getId());

                // remove ext from filename
                String name = rs.getString(1);
                name = name.substring(0, name.lastIndexOf('.'));

                document.setName(name);
                document.setFile(rs.getBytes(2));
                documentDtos.add(document);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return documentDtos;
    }

    public List<MapTile> findMapTilesByMap(Map map, long shaftId) {
        Statement st = null;
        try {
            st = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String sql = "SELECT index, map_file FROM map WHERE shaft_id = " + shaftId;

        List<MapTile> mapTiles = new LinkedList<>();

        try {
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()) {
                MapTile mapTile = new MapTile();
                mapTile.setMapId(map.getId());

                mapTile.setIndex(rs.getInt(1));
                mapTile.setFile(rs.getBytes(2));
                mapTiles.add(mapTile);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return mapTiles;
    }
}
