package com.example.ns;

import lombok.SneakyThrows;

import java.sql.*;
import java.util.Optional;

public class Database {
    private static final String databaseURL = "jdbc:sqlite:database.sqlite";
    private final Connection connection;
    private static Database instance;

    public static Database getInstance() {
        if (instance == null)
            instance = new Database();
        return instance;
    }

    @SneakyThrows
    public Database() {
        connection = DriverManager.getConnection(databaseURL);

        connection.createStatement().execute("""
                CREATE TABLE IF NOT EXISTS config (
                key TEXT UNIQUE PRIMARY KEY,
                value TEXT
                );""");
    }

    @SneakyThrows
    public Optional<String> getConfig(String key) {
        PreparedStatement statement = connection.prepareStatement("SELECT VALUE FROM config WHERE key = ?");
        statement.setString(1, key);
        ResultSet rs = statement.executeQuery();

        if (rs.next())
            return Optional.of(rs.getString("value"));
        else
            return Optional.empty();
    }

    @SneakyThrows
    public void setConfig(String key, String value) {
        PreparedStatement statement = connection.prepareStatement(
                "REPLACE INTO config(key, value) VALUES(?, ?)");
        statement.setString(1, key);
        statement.setString(2, value);
        statement.executeUpdate();
    }
}
