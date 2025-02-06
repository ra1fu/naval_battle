package data;

import data.interfaces.IDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresDB implements IDB {
    private static PostgresDB instance;
    private final String host;
    private final String username;
    private final String password;
    private final String dbName;
    private Connection connection;

    private PostgresDB(String host, String username, String password, String dbName) {
        this.host = host;
        this.username = username;
        this.password = password;
        this.dbName = dbName;
    }

    public static PostgresDB getInstance(String host, String username, String password, String dbName) {
        if (instance == null) {
            synchronized (PostgresDB.class) {
                if (instance == null) {
                    instance = new PostgresDB(host, username, password, dbName);
                }
            }
        }
        return instance;
    }

    @Override
    public Connection getConnection() {
        String connectionUrl = "jdbc:postgresql://" + host + "/" + dbName;
        try {
            if (connection != null && !connection.isClosed()) {
                return connection;
            }

            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(connectionUrl, username, password);
            return connection;
        } catch (Exception e) {
            System.out.println("Failed to connect to PostgreSQL: " + e.getMessage());
            return null;
        }
    }

    public void close() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database connection closed.");
            } catch (SQLException ex) {
                System.out.println("Connection close error: " + ex.getMessage());
            }
        }
    }
}
