package net.intra.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://tramway.proxy.rlwy.net:43112/railway";
    private static final String USER = "root";
    private static final String PASSWORD = "kYEpNbsPNHtxWqqVsAadCFwzUfvyAnJx";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
