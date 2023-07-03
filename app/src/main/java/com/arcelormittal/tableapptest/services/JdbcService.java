package com.arcelormittal.tableapptest.services;

import com.arcelormittal.tableapptest.dtos.MapDto;
import com.arcelormittal.tableapptest.dtos.PointDto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

public class JdbcService {

    private final Connection connection;

    public JdbcService() {
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://192.168.1.30:5431/pla", "postgres", "SUPERHELLOWORDL123@");
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
        String sql = "SELECT id, name FROM SHAFT";

        List<MapDto> maps = new LinkedList<>();

        try {
            ResultSet rs = st.executeQuery(sql);
            if(rs.next()) {
                MapDto map = new MapDto();
                map.id = rs.getInt(1);
                map.name = rs.getString(2);

                maps.add(map);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return maps;
    }

    public PointDto findPointsByMap(long shaftId) {
        Statement st = null;
        try {
            st = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String sql = "SELECT id, coord_and_contain, shaft_id FROM POINT WHERE shaft_id = " + shaftId;

        PointDto pointDto = new PointDto();

        try {
            ResultSet rs = st.executeQuery(sql);
            if(rs.next()) {
                pointDto.id = rs.getInt(1);
                pointDto.data = rs.getBytes(2);
                pointDto.shaftId = rs.getLong(3);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return pointDto;
    }

}
