package com.epicodus.bowloregon.models;

/**
 * Created by Guest on 5/6/16.
 */
public class Game {
    private double score;
    private String date;
    private String alleyId;

    public Game() {}

    public Game(double score, String date, String alleyId) {
        this.score = score;
        this.date = date;
        this.alleyId = alleyId;
    }

    public String getDate() {
        return date;
    }

    public double getScore() {
        return score;
    }
    public String getAlleyId() {
        return alleyId;
    }

    public void setAlleyId(String pushId) {
        this.alleyId = alleyId;
    }
}
