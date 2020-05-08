package edu.pja.kasia;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class Database {

    private String path;
    private Connection conn = null;

    private static Database instance = null;
    public static Database getInstance() {
        if(instance == null) {
            instance = new Database("database.db");
        }
        return instance;
    }

    public Database(String path) {
        this.path = path;
    }

    public void connect() {
        try {
            // db parameters
            String url = "jdbc:sqlite:" + this.path;
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            System.out.println("Connection to SQLite has been established.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void createInitTables() {
        // SQLite connection string
        String url = "jdbc:sqlite:" + this.path;

        // SQL statement for creating a new table
        String scores_sql = "CREATE TABLE IF NOT EXISTS scores (\n"
                + "    id integer PRIMARY KEY,\n"
                + "    scorePoints real NOT NULL,\n"
                + "    size integer NULL,\n"
                + "    time real NULL,\n"
                + "    name text NOT NULL"
                + ");";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(scores_sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void saveToDatabase (String msg) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            insertScore(mapper.readValue(msg, Score.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void insertScore(Score score) {
        String sql = "INSERT INTO scores (scorePoints, size, time, name) VALUES(?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, String.valueOf(score.scorePoints));
            pstmt.setString(2, String.valueOf(score.size));
            pstmt.setString(3, String.valueOf(score.time));
            pstmt.setString(4, score.name);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public String getJsonScores () {
        try {
            ArrayList<Score> scores = selectAllScores();
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(scores);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ArrayList<Score> selectAllScores(){
        ArrayList<Score> scores = new ArrayList<>();
        String sql = "SELECT scorePoints, size, time, name FROM scores";

        try (Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            while (rs.next()) {
                Score score = new Score(rs.getInt("size"), rs.getInt("time"), rs.getString("name"));
                scores.add(score);
            }
            return scores;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return new ArrayList<>();
    }

   /* public void closeConnection() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }*/
}