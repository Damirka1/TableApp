package com.arcelormittal.tableapptest.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcService {

    private Connection connection;

    public JdbcService() {
        try {
//            Class.forName("org.postgresql.Driver");

            connection = DriverManager.getConnection("jdbc:postgresql://home.damirka.space:5431/pla", "postgres", "SUPERHELLOWORDL123@");
//        } catch (ClassNotFoundException e) {
//            System.out.println(e.getMessage());
//            throw new RuntimeException(e);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
        Statement st = null;
        try {
            st = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String sql;
        sql = "SELECT name FROM SHAFT";
        try {
            ResultSet rs = st.executeQuery(sql);
            if(rs.next()) {
                System.out.println(rs.getString(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}
