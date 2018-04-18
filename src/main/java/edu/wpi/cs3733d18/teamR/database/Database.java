package edu.wpi.cs3733d18.teamR.database;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

abstract public class Database {
    Connection connection;

    abstract public void initialize();
    abstract public void close();

    protected final void runSQL(String sql){
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.err.println("Could not find data");
            e.printStackTrace();
        }
    }

    protected final void closeFW(FileWriter fw) {
        try {
            fw.flush();
            fw.close();
        } catch (IOException e) {
            System.err.println("Error while flushing/closing fileWriter");
            e.printStackTrace();
        }
    }
}

