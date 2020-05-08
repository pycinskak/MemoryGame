package edu.pja.kasia;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;

public class Score implements Serializable, Comparable<Score>{
    double scorePoints;
    int size;
    double time;
    String name;
    public Score(){}
    public Score(int size, double time, String name){
        this.size = size;
        this.time = time;
        this.scorePoints = size/time;
        this.name = name;
    }

    public String toJson () {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public double getScorePoints() {
        return scorePoints;
    }

    public int getSize() {
        return size;
    }

    public double getTime() {
        return time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScorePoints(double scorePoints) {
        this.scorePoints = scorePoints;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setTime(int time) {
        this.time = time;
    }

    @Override
    public int compareTo(Score o) {
        if(this.scorePoints-o.scorePoints<0)
            return 1;
        else return -1;
    }
}
